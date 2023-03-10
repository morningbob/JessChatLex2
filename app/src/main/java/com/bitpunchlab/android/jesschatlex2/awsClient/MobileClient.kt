package com.bitpunchlab.android.jesschatlex2.awsClient

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobile.client.UserStateListener
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.amazonaws.mobile.client.results.SignUpResult
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.regions.Regions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONException
import org.json.JSONObject

object MobileClient {

    var lexClient : InteractionClient? = null
    private val _signUpResult = MutableStateFlow<Boolean?>(null)
    val signUpResult : StateFlow<Boolean?> = _signUpResult.asStateFlow()

    private val _signInResult = MutableStateFlow<Boolean?>(null)
    val signInResult : StateFlow<Boolean?> = _signInResult.asStateFlow()

    fun initializeMobileClient(context: Context) {

            AWSMobileClient.getInstance().initialize(context, object :
                Callback<UserStateDetails> {
                override fun onResult(result: UserStateDetails?) {
                    Log.d("AWSClient", "initialize.onResult, userState: " + result?.getUserState().toString())
                    val userStateDetails = result?.userState
                    //val cred = AWSMobileClient.getInstance().awsCredentials
                    when (userStateDetails) {
                        UserState.SIGNED_IN -> {
                            Log.i("Mobile Client", "state as signed in")
                        }
                        UserState.SIGNED_OUT -> {
                            Log.i("Mobile Client", "state as signed out")
                        }
                        UserState.GUEST -> {
                            Log.i("Mobile Client", "state as guest")
                        }
                    }

                    val id = AWSMobileClient.getInstance().identityId
                    Log.i("AWSClient", "id: $id")
                    var botName = ""
                    var botAlias = ""
                    var botRegion = ""
                    var lexConfig : JSONObject? = null

                    try {
                        lexConfig = AWSMobileClient.getInstance().configuration.optJsonObject("Lex")
                        lexConfig = lexConfig.getJSONObject(lexConfig.keys().next())

                        botName = lexConfig.getString("Name")
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

                    //lexClient.setAudioPlaybackListener()
                    //lexClient!!.setInteractionListener(interactionListener)
                }

                override fun onError(e: Exception?) {
                    Log.i("AWSClient", "error initializing")
                }

            })

    }

    fun listenUserState() {
        AWSMobileClient.getInstance().addUserStateListener(UserStateListener { details: UserStateDetails? ->
            when (details?.userState) {
                UserState.SIGNED_OUT -> {
                    Log.i("mobile client user state", "signed out")
                }
                UserState.SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> {
                    Log.i("mobile client user state", "invalid token")
                }
            }
        })
    }

    fun signUp(name: String, email: String, password: String) {
        val attributes = HashMap<String, String>()
        attributes.put("name", name)

        AWSMobileClient.getInstance().signUp(email, password, attributes, null, object : Callback<SignUpResult> {
            override fun onResult(result: SignUpResult?) {
                if (result?.confirmationState == false) {
                    Log.i("mobile client", "??")
                    _signUpResult.value = false
                } else {
                    Log.i("mobile client", "sign up done")
                    _signUpResult.value = true
                }
            }

            override fun onError(e: java.lang.Exception?) {
                Log.i("mobile client", "error sign up $e")
                _signUpResult.value = false
            }

        })
    }

    fun signIn(email: String, password: String) {
        AWSMobileClient.getInstance().signIn(email, password, null, object : Callback<SignInResult>{
            override fun onResult(result: SignInResult?) {
                when (result?.signInState) {
                    SignInState.DONE -> {
                        _signInResult.value = true
                        Log.i("mobile client", "sign in done")
                    }
                    else -> {
                        Log.i("mobile client", "sign in failed")
                        _signInResult.value = false
                    }
                }
            }

            override fun onError(e: java.lang.Exception?) {
                _signInResult.value = false
                Log.i("mobile client", "sign in error $e")
            }

        })
    }

}