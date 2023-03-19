package com.bitpunchlab.android.jesschatlex2.userAccount

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.database.ChatDatabase
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex2.models.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    var currentMessageList = ArrayList<Message>()

    @OptIn(InternalCoroutinesApi::class)
    val database = ChatDatabase.getInstance(application.applicationContext)

    val _allMessages = MutableStateFlow<List<Message>>(emptyList())
    var allMessages : StateFlow<List<Message>> = _allMessages.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    val _userName = MutableStateFlow<String>("Loading...")
    var userName : StateFlow<String> = _userName.asStateFlow()

    val _userEmail = MutableStateFlow<String>("Loading...")
    var userEmail : StateFlow<String> = _userEmail.asStateFlow()


    // listen to login status
    init {
        listenLexMessages()
        // I listen to sign in state, even before the AWS Mobile Client is initialized
        // so, it can detect the sign in from very early on
        MobileClient.listenUserState()
        // we watch when isLogged in is true, we get the name
        listenLoginState()
        listenLexError()
    }

    private fun listenLoginState() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileClient.isLoggedIn.collect() { it ->
                if (it == true) {
                    requestUserNameEmail()
                }
            }
        }
    }

    private fun listenLexMessages() {
        CoroutineScope(Dispatchers.IO).launch {

            MobileClient.messageState.collect() {
                if (it != "") {
                    val message = Message(
                        UUID.randomUUID().toString(),
                        WhoSaid.Lex,
                        it
                    )
                    currentMessageList.add(message)
                    insertMessage(message)
                    _loadingAlpha.value = 0f
                }
            }
        }
    }

    private fun listenLexError() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileClient.lexError2.collect() { it ->
                if (it != null) {
                    _loadingAlpha.value = 0f
                }
            }
        }
    }

    fun requestUserNameEmail() {
        CoroutineScope(Dispatchers.IO).launch {
            val pair = MobileClient.getUserNameEmail()
            pair?.let {
                _userName.value = pair.first
                _userEmail.value = pair.second
            }
        }
    }

    fun sendMessage(messageString: String) {
        if (messageString != "") {
            _loadingAlpha.value = 1f
            MobileClient.sendMessage(messageString, getApplication<Application>().applicationContext)
            val message = Message(
                UUID.randomUUID().toString(),
                WhoSaid.User,
                messageString
            )
            currentMessageList.add(message)
            CoroutineScope(Dispatchers.IO).launch {
                insertMessage(message)
            }
        }
    }

    fun getAllMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            database.chatDAO.getAllMessage().collect() { messages ->
                _allMessages.value = messages
            }
        }
    }

    private fun insertMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            database.chatDAO.insertMessages(message)
        }
    }

    fun logoutUser() {
        CoroutineScope(Dispatchers.IO).launch {
            MobileClient.signOut()
        }
    }
}

class MainViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}