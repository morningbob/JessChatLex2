package com.bitpunchlab.android.jesschatlex2.awsClient

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amazonaws.auth.AWSBasicCognitoIdentityProvider
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.mobileconnectors.lex.interactionkit.InteractionClient
import com.amazonaws.mobileconnectors.lex.interactionkit.Response
import com.amazonaws.mobileconnectors.lex.interactionkit.config.InteractionConfig
import com.amazonaws.mobileconnectors.lex.interactionkit.continuations.LexServiceContinuation
import com.amazonaws.mobileconnectors.lex.interactionkit.listeners.InteractionListener
import com.amazonaws.regions.Regions
import com.amazonaws.services.lexrts.model.DialogState
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LexClient {

    @SuppressLint("StaticFieldLeak")
    var lexClient : InteractionClient? = null

    private val _messageState = MutableStateFlow<String>("")
    val messageState : StateFlow<String> = _messageState.asStateFlow()

    fun initializeLex(context: Context, auth: AWSCognitoAuthSession) {
        val access = auth.awsCredentialsResult.value?.accessKeyId
        val secret = auth.awsCredentialsResult.value?.secretAccessKey
        val token = auth.userPoolTokensResult.value?.accessToken
        val region = "us-east-1"
        val id = auth.identityIdResult.value
        val credentials = object : AWSCredentials {
            override fun getAWSAccessKeyId(): String {
                return access!!
            }

            override fun getAWSSecretKey(): String {
                return secret!!
            }

        }
        val provider = object : AWSCredentialsProvider {
            override fun getCredentials(): com.amazonaws.auth.AWSCredentials {
                return credentials
            }
            override fun refresh() {

            }
        }
        //val basic = AWSBasicCredentials()

        val lexInterConfig = InteractionConfig(
            "BookTrip_dev",
            "JessChat",
            id
        )

        lexClient = InteractionClient(context, provider, Regions.fromName(region), lexInterConfig)
        lexClient?.let {
            Log.i("lex client", "got lex client not null")
        }
        lexClient?.setInteractionListener(interactionListener)
    }

    private val interactionListener = object : InteractionListener {
        override fun onReadyForFulfillment(response: Response?) {
            Log.i("lex listener", "transaction completed")
            val responseText = response?.textResponse
            responseText?.let {
                //messageList.add(responseText)
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
        }

    }

    fun sendMessage(message: String) {
        lexClient?.textInForTextOut(message, null)
    }
}