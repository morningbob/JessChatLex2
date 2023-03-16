package com.bitpunchlab.android.jesschatlex2.awsClient

import android.annotation.SuppressLint
import android.content.Context
import android.telecom.Call
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.SignOutOptions
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.UserStateListener
import com.amazonaws.mobile.client.results.*
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.Response
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.mobileconnectors.lex.interactionkit.continuations.LexServiceContinuation
import com.amazonaws.mobileconnectors.lex.interactionkit.listeners.InteractionListener
import com.amazonaws.regions.Regions
import com.amazonaws.services.lexrts.model.DialogState
import com.bitpunchlab.android.jesschatlex2.ForgotPassword
import com.bitpunchlab.android.jesschatlex2.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import org.json.JSONObject

object MobileClient {

    @SuppressLint("StaticFieldLeak")
    var lexClient : InteractionClient? = null

    private val _messageState = MutableStateFlow<String>("")
    val messageState : StateFlow<String> = _messageState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn : StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _lexError = MutableStateFlow<Boolean?>(null)
    val lexError : StateFlow<Boolean?> = _lexError.asStateFlow()

    private val _lexNull = MutableStateFlow<Boolean?>(null)
    val lexNull : StateFlow<Boolean?> = _lexNull.asStateFlow()

    private val _lexError2 = MutableStateFlow<String?>(null)
    val lexError2 : StateFlow<String?> = _lexError2.asStateFlow()

    fun initializeMobileClient(context: Context) {

            AWSMobileClient.getInstance().initialize(context, object :
                Callback<UserStateDetails> {
                override fun onResult(result: UserStateDetails?) {
                    // part 1
                    Log.d("AWSClient", "initialize.onResult, userState: " + result?.getUserState().toString())
                    val userStateDetails = result?.userState
                    when (userStateDetails) {
                        UserState.SIGNED_IN -> {
                            Log.i("Mobile Client", "state as signed in")
                            _isLoggedIn.value = true
                        }
                        UserState.SIGNED_OUT -> {
                            Log.i("Mobile Client", "state as signed out")
                            _isLoggedIn.value = false
                        }
                        UserState.GUEST -> {
                            Log.i("Mobile Client", "state as guest")
                            _isLoggedIn.value = false
                        }
                        else -> {
                            _isLoggedIn.value = false
                        }
                    }

                    val id = AWSMobileClient.getInstance().identityId
                    Log.i("AWSClient", "id: $id")
                    var botName = context.getString(R.string.bot_name)
                    var botAlias = ""
                    var botRegion = ""
                    var lexConfig : JSONObject? = null

                    try {
                        lexConfig = AWSMobileClient.getInstance().configuration.optJsonObject("Lex")
                        lexConfig = lexConfig.getJSONObject(lexConfig.keys().next())

                        botAlias = lexConfig.getString("Alias")
                        botRegion = lexConfig.getString("Region")
                        Log.i("name", botName)
                        Log.i("alias", botAlias)
                        Log.i("Region", botRegion)
                    } catch (e: JSONException) {
                        Log.i("AWSClient", "parse JSOn error")
                    }

                    val lexInterConfig = InteractionConfig(
                        botName,
                        botAlias,
                        id
                    )

                    lexClient = InteractionClient(
                        context,
                        AWSMobileClient.getInstance(),
                        Regions.fromName(botRegion),
                        lexInterConfig
                    )

                    // part 3
                    lexClient?.setInteractionListener(interactionListener)
                }

                override fun onError(e: Exception?) {
                    Log.i("AWSClient", "error initializing")
                }
            })
    }

    fun listenUserState() {
        AWSMobileClient.getInstance().addUserStateListener(UserStateListener { details: UserStateDetails? ->
            when (details?.userState) {
                UserState.SIGNED_IN -> {
                    Log.i("mobile client user state", "signed in detected")
                    _isLoggedIn.value = true
                }
                UserState.SIGNED_OUT -> {
                    Log.i("mobile client user state", "signed out")
                    _isLoggedIn.value = false
                }
                UserState.SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> {
                    Log.i("mobile client user state", "invalid token")
                    _isLoggedIn.value = false
                }
                else -> {
                    _isLoggedIn.value = false
                }
            }
        })
    }

    suspend fun getUserNameEmail() : Pair<String, String>? =
        suspendCancellableCoroutine<Pair<String, String>?> { cancellableContinuation ->
            try {
                val attributes = AWSMobileClient.getInstance().userAttributes
                var name : String? = attributes["name"]
                var email : String? = attributes["email"]
                if (!name.isNullOrEmpty() && !email.isNullOrEmpty()) {
                    cancellableContinuation.resume(Pair(name, email)) {}
                } else {
                    cancellableContinuation.resume(null) {}
                }
            } catch (e: Exception) {
                Log.i("mobile client", "there is error getting user name. $e")
                cancellableContinuation.resume(null) {}
            }
        }
    

    suspend fun signUp(name: String, email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean> {  cancellableContinuation ->
            val attributes = HashMap<String, String>()
            attributes.put("name", name)

            AWSMobileClient.getInstance().signUp(email, password, attributes, null, object : Callback<SignUpResult> {
                override fun onResult(result: SignUpResult?) {
                    if (result?.confirmationState == false) {
                        Log.i("mobile client", "??")
                        cancellableContinuation.resume(false) {}
                    } else {
                        Log.i("mobile client", "sign up done")
                        cancellableContinuation.resume(true) {}
                    }
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("mobile client", "error sign up $e")
                    cancellableContinuation.resume(false) {}
                }

            })
    }

    suspend fun confirmSignUp(email: String, code: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            AWSMobileClient.getInstance().confirmSignUp(email, code, object : Callback<SignUpResult>{
                override fun onResult(result: SignUpResult?) {
                    if (result?.confirmationState == false) {
                        Log.i("mobile client", "confirm code ??")
                        cancellableContinuation.resume(false) {}
                    } else {
                        Log.i("mobile client", "confirm code succeeded")
                        cancellableContinuation.resume(true) {}
                    }
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("mobile client", "confirm code error $e")
                    cancellableContinuation.resume(false) {}
                }

            })
    }

    suspend fun resendConfirmationCode(email: String) : Boolean =
        suspendCancellableCoroutine {  cancellableContinuation ->
            AWSMobileClient.getInstance().resendSignUp(email, object : Callback<SignUpResult>{
                override fun onResult(result: SignUpResult?) {
                    Log.i("mobile client", "resend code sent")
                    cancellableContinuation.resume(true) {}
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("mobile client", "resend code failed $e")
                    cancellableContinuation.resume(false) {}
                }

            })
    }

    suspend fun signIn(email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            AWSMobileClient.getInstance()
                .signIn(email, password, null, object : Callback<SignInResult> {
                    override fun onResult(result: SignInResult?) {
                        when (result?.signInState) {
                            SignInState.DONE -> {
                                Log.i("mobile client", "sign in done")
                                cancellableContinuation.resume(true) {}
                            }
                            else -> {
                                Log.i("mobile client", "sign in failed")
                                cancellableContinuation.resume(false) {}
                            }
                        }
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Log.i("mobile client", "sign in error $e")
                        cancellableContinuation.resume(false) {}
                    }

                })
    }

    suspend fun forgotPassword(email: String) : Boolean =
        suspendCancellableCoroutine<Boolean> {  cancellableContinuation ->
            AWSMobileClient.getInstance().forgotPassword(email, object : Callback<ForgotPasswordResult>{
                override fun onResult(result: ForgotPasswordResult?) {
                    when (result?.state) {
                        ForgotPasswordState.CONFIRMATION_CODE -> {
                            Log.i("mobile client", "got confirmation code")
                            cancellableContinuation.resume(true) {}
                        }
                        else -> {
                            cancellableContinuation.resume(false) {}
                        }
                    }
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("mobile client", "forget password failed to get code")
                    cancellableContinuation.resume(false) {}
                }
            })
    }

    suspend fun confirmForgotPassword(email: String, newPassword: String, code: String) : Boolean =
        suspendCancellableCoroutine {  cancellableContinuation ->
            AWSMobileClient.getInstance().confirmForgotPassword(email, newPassword, code, object : Callback<ForgotPasswordResult>{
                override fun onResult(result: ForgotPasswordResult?) {
                    when (result?.state) {
                        ForgotPasswordState.DONE -> {
                            cancellableContinuation.resume(true) {}
                        }
                        else -> {
                            cancellableContinuation.resume(false) {}
                        }
                    }
                }

                override fun onError(e: java.lang.Exception?) {
                    cancellableContinuation.resume(false) {}
                }
            })
    }

    suspend fun changePassword(oldPassword: String, newPassword: String) : Boolean =
        suspendCancellableCoroutine { cancellableContinuation ->
            AWSMobileClient.getInstance().changePassword(oldPassword, newPassword, object : Callback<Void> {
                override fun onResult(result: Void?) {
                    Log.i("mobile client", "change password succeeded")
                    cancellableContinuation.resume(true) {}
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("mobile client", "change password failed, $e")
                    cancellableContinuation.resume(false) {}
                }
            })
    }

    // for just sign out locally, just call signout()
    fun signOut() {
        AWSMobileClient.getInstance().signOut(SignOutOptions.builder().invalidateTokens(true).build(), object : Callback<Void> {
            override fun onResult(result: Void?) {
                Log.i("mobile client", "signed out")
            }

            override fun onError(e: java.lang.Exception?) {
                Log.i("mobile client", "failed to sign out $e")
            }
        })
    }

    private val interactionListener = object : InteractionListener {
        override fun onReadyForFulfillment(response: Response?) {
            Log.i("lex listener", "transaction completed")
            val responseText = response?.textResponse
            responseText?.let {
                _messageState.value = responseText
            }
            Log.i("lex listener", "response (ready): $responseText")
        }

        override fun promptUserToRespond(response: Response?, continuation: LexServiceContinuation?) {
            val responseText = response?.textResponse
            responseText?.let {
                _messageState.value = responseText
            }
            Log.i("lex listener", "response (prompt): $responseText")
        }

        override fun onInteractionError(response: Response?, e: Exception?) {
            if (response != null) {
                if (DialogState.Failed.toString().equals(response.getDialogState())) {
                    Log.i("lex listener", "error detected")
                } else {
                    Log.i("lex listener", "please try again")
                }
            } else {
                Log.i("lex listener", "interaction error detected")
            }
            _lexError.value = true
        }

    }

    fun sendMessage(message: String) {
        // reset lexError for the next round
        _lexError.value = null
        if (lexClient != null) {
            lexClient?.textInForTextOut(message, null)
        } else {
            Log.i("lex client", "is null")
            _lexError2.value = "Trying to connect to the server..."
        }
    }

}
