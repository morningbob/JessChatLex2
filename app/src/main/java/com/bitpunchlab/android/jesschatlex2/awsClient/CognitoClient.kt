package com.bitpunchlab.android.jesschatlex2.awsClient

import android.util.Log
import aws.smithy.kotlin.runtime.ServiceException
//import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient
import com.amazonaws.services.cognitoidentity.model.CognitoIdentityProvider
import com.amazonaws.services.cognitoidentity.model.NotAuthorizedException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.*
import java.security.InvalidParameterException

object CognitoClient {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun loginUser(email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean>() { cancellableContinuation ->
            val map = HashMap<String, String>()
            map.put("email", email)
            val options = AWSCognitoAuthSignInOptions.builder()
                //.authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
                //.authFlowType()
                //.
                .metadata(map)
                .build()

            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result =  Amplify.Auth.signIn(username = email, password = password)
                    //val result1 =  AWSMobileClient.getInstance().signIn(email, password)
                    if (result.isSignedIn) {
                        Log.i("Cognito Sign in", "Sign in succeeded")
                        //AWSMobileClient.getInstance().signIn()
                        //val accessToken = AWSMobileClient.getInstance().tokens.accessToken
                        //val refreshToken = AWSMobileClient.getInstance().tokens.refreshToken
                        val authSession = Amplify.Auth.fetchAuthSession()
                        val auth = authSession as AWSCognitoAuthSession
                        val accessToken = auth.userPoolTokensResult.value?.accessToken
                        val refreshToken = auth.userPoolTokensResult.value?.refreshToken
                        Log.i("access token", accessToken.toString())
                        Log.i("refresh token", refreshToken.toString())
                        //AWSMobileClient.getInstance().
                        //val cognitoTokenProvider = result as Token
                        cancellableContinuation.resume(true) {}
                    } else if (!result.isSignedIn) {
                        Log.e("Cognito Sign in", "Sign in not complete")
                        cancellableContinuation.resume(false) {}
                    }
                    } catch (exception: AuthException) {
                        Log.e("Cognito Sign in", "Sign in failed", exception)
                        cancellableContinuation.resume(false) {}
                } catch (exception: InvalidParameterException) {
                    Log.i("Cognito Sign in", "inputs are invalid: $exception")
                    cancellableContinuation.resume(false) {}
                } catch (exception: NotAuthorizedException) {
                    Log.i("Cognito Sign in", "wrong email or password : $exception")
                    cancellableContinuation.resume(false) {}
                } catch (exception: Exception) {
                    Log.i("Cognito Sign in", "all exception: $exception")
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun registerUser(name: String, email: String, password: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            val options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .userAttribute(AuthUserAttributeKey.name(), name)
                .build()
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.signUp(username = email, password = password, options)
                    Log.i("registration", "Result: $result")
                    cancellableContinuation.resume(true) {}
                } catch (error: Exception) {
                    Log.e("registration", "Sign up failed", error)
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserNameEmail() : Pair<String, String>? =
        suspendCancellableCoroutine<Pair<String, String>?> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.fetchUserAttributes()
                    var name : String? = null
                    var email : String? = null
                    Log.i("Cognito Client, getUserName", "got back $result")
                    //parseUserName(result)
                    for (each in result) {
                        if (each.key == AuthUserAttributeKey.name()) {
                            Log.i("each, name value", each.value)
                            name = each.value
                        }
                        if (each.key == AuthUserAttributeKey.email()) {
                            Log.i("each, email value", each.value)
                            email = each.value
                        }
                        if (name != null && email != null) {
                            cancellableContinuation.resume(Pair(name, email)) {}
                        }
                    }
                    cancellableContinuation.resume(null) {}

                } catch (exception: Exception) {
                    Log.i("Cognito Client, getUserName", "failed")
                    cancellableContinuation.resume(null) {}
                }
            }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun recoverUser(email: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.resetPassword(username = email)
                    Log.i("password reset", "result: $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("password reset", "error: $exception")
                    //sendVerificationCode(email)
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    suspend fun updatePassword(oldPassword: String, newPassword: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.updatePassword(oldPassword, newPassword)
                    Log.i("password update", "success $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("password update", "error: $exception")
                    cancellableContinuation.resume(false) {}
                }
            }
        }

/*
    suspend fun resendVerificationCode(email: String) : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.resendSignUpCode(email)
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    cancellableContinuation.resume(false) {}
                }

            }
        }
*/
    suspend fun confirmResetPassword(email: String, newPassword: String, code: String) : Boolean =
        suspendCancellableCoroutine { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.confirmResetPassword(email, newPassword, code)
                    Log.i("confirm reset password", "success $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("confirm reset password", "failed")
                    cancellableContinuation.resume(false) {}
                }
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun confirmVerificationCodeSignUp(email: String, code: String) : Boolean =
        suspendCancellableCoroutine<Boolean> {  cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.confirmSignUp(email, code)

                    Log.i("send verification code", "result $result")
                    cancellableContinuation.resume(true) {}
                } catch (exception: Exception) {
                    Log.i("send verification code", "failed $exception")
                    cancellableContinuation.resume(false) {}
                }
            }
    }

    suspend fun logoutUser() {
        val signOutResult = Amplify.Auth.signOut()

        when(signOutResult) {
            is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                // Sign Out completed fully and without errors.
                Log.i("logout triggered", "Signed out successfully")
            }
            is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                // Sign Out completed with some errors. User is signed out of the device.
                signOutResult.hostedUIError?.let {
                    Log.e("logout triggered", "HostedUI Error", it.exception)
                    // Optional: Re-launch it.url in a Custom tab to clear Cognito web session.

                }
                signOutResult.globalSignOutError?.let {
                    Log.e("logout triggered", "GlobalSignOut Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.accessToken.
                }
                signOutResult.revokeTokenError?.let {
                    Log.e("logout triggered", "RevokeToken Error", it.exception)
                    // Optional: Use escape hatch to retry revocation of it.refreshToken.
                }
            }
            is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                // Sign Out failed with an exception, leaving the user signed in.
                Log.e("logout triggered", "Sign out Failed", signOutResult.exception)
            }
        }
    }

    suspend fun rememberDevice() : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    val result = Amplify.Auth.rememberDevice()
                    //val i = Amplify.Auth.fetchAuthSession()//.rememberDevice()

                    Log.i("remember device", "success: $result")
                } catch (exception: Exception) {
                    Log.i("remember device", "failed")
                }
            }
        }

    suspend fun getAccessToken() : Boolean =
        suspendCancellableCoroutine<Boolean> { cancellableContinuation ->
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                try {
                    //Amplify.Auth.fetchAuthSession { result ->

                    //}
                } catch (exception: Exception) {

                }
            }
        }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        //println("Handle $exception in CoroutineExceptionHandler")
        when (exception) {
            is NotAuthorizedException -> { Log.i("sign in exception", "not authorized")}
            is InvalidParameterException -> { Log.i("sign in exception", "invalid parameters") }
            is ServiceException -> { Log.i("sign in exception", "caught service exception")}
            is Exception -> {
                Log.i("sign in exception", "general exception : $exception")

            }
            else -> { Log.i("sign in exception", "default")}
        }
    }

}