package com.bitpunchlab.android.jesschatlex2.userAccount

import android.app.Application
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex2.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
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
    //var allMessages = database.chatDAO.getAllMessage()
    //val _newMessage = MutableStateFlow<Message?>(null)
    //var newMessage : StateFlow<Message?> = _newMessage
    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()
    val _userName = MutableStateFlow<String>("")
    var userName : StateFlow<String> = _userName.asStateFlow()
    val _userEmail = MutableStateFlow<String>("")
    var userEmail : StateFlow<String> = _userEmail.asStateFlow()



    // listen to login status
    init {
        listenLoginStatus()
        listenLexMessages()
        // we watch when isLogged in is true, we get the name
        CoroutineScope(Dispatchers.IO).launch {
            isLoggedIn.collect() { it ->
                if (it) {
                    //retrieveUserName()
                    val pair = CognitoClient.getUserNameEmail()
                    pair?.let {
                        _userName.value = it.first
                        _userEmail.value = it.second
                    }
                }
            }
        }
    }

    private fun listenLoginStatus() {
        CoroutineScope(Dispatchers.Default).launch {
            Amplify.Hub.subscribe(HubChannel.AUTH).collect {
                when (it.name) {
                    // this is the case when user logged in previously
                    // the app should automatically navigate to main page
                    InitializationStatus.SUCCEEDED.toString() -> {
                        Log.i("AuthQuickstart", "Auth successfully initialized")
                        val authSessionDeferred = CoroutineScope(Dispatchers.IO).async {
                            Amplify.Auth.fetchAuthSession()
                        }
                        val authSession = authSessionDeferred.await()
                        if (authSession.isSignedIn) {
                            Log.i("auth listener", "signed in")
                            _isLoggedIn.value = true
                            Log.i("isLoggedIn", "is passed to variable")
                        } else {
                            Log.i("auth listener", "not signed in")
                            _isLoggedIn.value = false
                        }
                    }
                    InitializationStatus.FAILED.toString() ->
                        Log.i("auth listener", "Auth failed to succeed")
                    else -> when (AuthChannelEventName.valueOf(it.name)) {
                        AuthChannelEventName.SIGNED_IN -> {
                            Log.i("auth listener", "Auth just became signed in.")
                            _isLoggedIn.value = true
                        }
                        AuthChannelEventName.SIGNED_OUT -> {
                            Log.i("auth listener", "Auth just became signed out.")
                            _isLoggedIn.value = false
                        }
                        AuthChannelEventName.SESSION_EXPIRED -> {
                            Log.i("auth listener", "Auth session just expired.")
                            _isLoggedIn.value = false
                        }
                        AuthChannelEventName.USER_DELETED -> {
                            Log.i("auth listener", "User has been deleted.")
                            _isLoggedIn.value = false
                        }
                    }
                }
            }
        }
    }

    private fun listenLexMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            AmazonLexClient.messageState.collect() {
                if (it != "") {
                    val message = Message(
                        UUID.randomUUID().toString(),
                        WhoSaid.Lex,
                        it
                    )
                    //_newMessage.value = message
                    currentMessageList.add(message)
                    insertMessage(message)
                    _loadingAlpha.value = 0f
                }
            }
        }
    }
    // if the name is not available from share preference
    // this function will send a request to get the name
    fun retrieveUserName() {
        AmazonLexClient.getName()
    }

    fun sendMessage(messageString: String) {
        if (messageString != "") {
            _loadingAlpha.value = 1f
            AmazonLexClient.sendMessage(messageString)
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
            //withContext(uiScope.coroutineContext) { // Use instead of the default context
            database.chatDAO.insertMessages(message)
        }
    }

    fun logoutUser() {
        CoroutineScope(Dispatchers.IO).launch {
            CognitoClient.logoutUser()
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