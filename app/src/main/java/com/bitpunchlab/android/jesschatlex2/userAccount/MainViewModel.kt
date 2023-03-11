package com.bitpunchlab.android.jesschatlex2.userAccount

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.material.ModalDrawer
import androidx.compose.runtime.currentRecomposeScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.lexrts.AmazonLexRuntime
import com.amazonaws.services.lexrts.AmazonLexRuntimeClient
import com.amazonaws.services.lexrts.model.PostTextRequest

import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex2.awsClient.AmazonLexClient
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.awsClient.LexClient
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.database.ChatDatabase
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex2.models.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var cognitoAuthSession = MutableLiveData<AWSCognitoAuthSession?>()

    val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    var currentMessageList = ArrayList<Message>()

    @OptIn(InternalCoroutinesApi::class)
    val database = ChatDatabase.getInstance(application.applicationContext)

    val _allMessages = MutableStateFlow<List<Message>>(emptyList())
    var allMessages : StateFlow<List<Message>> = _allMessages.asStateFlow()
    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()
    val _userName = MutableStateFlow<String>("")
    var userName : StateFlow<String> = _userName.asStateFlow()
    val _userEmail = MutableStateFlow<String>("")
    var userEmail : StateFlow<String> = _userEmail.asStateFlow()

    // listen to login status
    init {
        //listenLoginStatus()
        listenLexMessages()
        // I listen to sign in state, even before the AWS Mobile Client is initialized
        // so, it can detect the sign in from very early on
        MobileClient.listenUserState()
        // we watch when isLogged in is true, we get the name
        CoroutineScope(Dispatchers.IO).launch {
            isLoggedIn.collect() { it ->
                if (it) {
                    //retrieveUserName()
                    //val pair = CognitoClient.getUserNameEmail()
                    //pair?.let {
                    //    _userName.value = it.first
                    //    _userEmail.value = it.second
                    //}
                }
            }
        }
        cognitoAuthSession.observeForever(androidx.lifecycle.Observer { auth ->
            auth?.let {
                //LexClient.initializeLex(getApplication<Application>().applicationContext, auth)
            }
        })
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
    // if the name is not available from share preference
    // this function will send a request to get the name
    fun retrieveUserName() {
        //AmazonLexClient.getName()
    }

    fun sendMessage(messageString: String) {
        if (messageString != "") {
            _loadingAlpha.value = 1f
            //AmazonLexClient.sendMessage(messageString)
            //LexClient.sendMessage(messageString)
            MobileClient.sendMessage(messageString)
            //a(getApplication<Application>().applicationContext)
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
            //CognitoClient.logoutUser()
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