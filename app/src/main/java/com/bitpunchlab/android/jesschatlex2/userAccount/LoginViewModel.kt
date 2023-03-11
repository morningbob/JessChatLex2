package com.bitpunchlab.android.jesschatlex2.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    private val _readyLogin = MutableStateFlow<Boolean>(false)
    val readyLogin : StateFlow<Boolean> = _readyLogin.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    private val _showFailureDialog = MutableStateFlow<Boolean>(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog.asStateFlow()

    private val _showConfirmEmailDialog = MutableStateFlow<Boolean>(false)
    val showConfirmEmailDialog: StateFlow<Boolean> = _showConfirmEmailDialog.asStateFlow()

    private val _showConfirmEmailStatus = MutableStateFlow<Int>(0)
    val showConfirmEmailStatus: StateFlow<Int> = _showConfirmEmailStatus.asStateFlow()

    private val _confirmEmail = MutableStateFlow<String>("")
    val confirmEmail : StateFlow<String> = _confirmEmail.asStateFlow()

    private val _confirmCode = MutableStateFlow<String>("")
    val confirmCode : StateFlow<String> = _confirmCode.asStateFlow()

    private val _confirmEmailError = MutableStateFlow<String>(" ")
    val confirmEmailError : StateFlow<String> = _confirmEmailError.asStateFlow()

    private val _confirmCodeError = MutableStateFlow<String>(" ")
    val confirmCodeError : StateFlow<String> = _confirmCodeError.asStateFlow()

    private val _resendCodeStatus = MutableStateFlow<Int>(0)
    val resendCodeStatus : StateFlow<Int> = _resendCodeStatus.asStateFlow()

    private val _resendCodeEmailError = MutableStateFlow<String>("")
    val resendCodeEmailError : StateFlow<String> = _resendCodeEmailError.asStateFlow()



    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(emailErrorState, passwordErrorState) { email, password ->
                _readyLogin.value = email == "" && password == ""
            }.collect()
        }
    }

    fun updateEmail(inputEmail: String) {
        _emailState.value = inputEmail
        _emailErrorState.value = InputValidation.verifyEmail(inputEmail)
    }

    fun updatePassword(inputPass: String) {
        _passwordState.value = inputPass
        _passwordErrorState.value = InputValidation.verifyPassword(inputPass)
    }

    fun loginUser() {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (MobileClient.signIn(emailState.value, passwordState.value)) {
                _loadingAlpha.value = 0f
                resetFields()
            } else {
                _showFailureDialog.value = true
                _loadingAlpha.value = 0f
                resetFields()
            }
        }

    }

    // 0 means not ready, 1 means success, 2 means failure
    fun verifyConfirmCode(email: String, code: String) {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (MobileClient.confirmSignUp(email, code)) {
                Log.i("verify code", "success")
                // show success alert
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 1
            } else {
                Log.i("verify code", "failed")
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 2
            }
        }
    }

    fun resendVerificationCode(email: String) {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (MobileClient.resendConfirmationCode(email)) {
                _loadingAlpha.value = 0f
                _resendCodeStatus.value = 2
            } else {
                _loadingAlpha.value = 0f
                _resendCodeStatus.value = 3
            }
        }
    }

    fun updateShowDialog(newValue: Boolean) {
        _showFailureDialog.value = newValue
    }

    fun updateShowConfirmEmailDialog(newValue: Boolean) {
        _showConfirmEmailDialog.value = newValue
    }

    fun updateConfirmEmailStatus(newValue: Int) {
        _showConfirmEmailStatus.value = newValue
    }

    fun updateResendCodeStatus(newValue: Int) {
        _resendCodeStatus.value = newValue
    }

    fun updateConfirmEmail(newValue: String) {
        _confirmEmail.value = newValue
    }

    fun updateConfirmCode(newValue: String) {
        _confirmCode.value = newValue
    }

    fun updateConfirmEmailError(newValue: String) {
        _confirmEmailError.value = newValue
    }

    fun updateResendCodeEmailError(newValue: String) {
        _resendCodeEmailError.value = newValue
    }

    private fun resetFields() {
        _emailState.value = ""
        _passwordState.value = ""
        _emailErrorState.value = " "
        _passwordState.value = " "
    }
}
/*
       CoroutineScope(Dispatchers.IO).launch {
           if (CognitoClient.loginUser(email = emailState.value, password = passwordState.value)) {
               // navigate to main
               // setting the alpha here and below, duplicately, because of timing issue
               // want to set to 0f only when login result came back
               _loadingAlpha.value = 0f
               resetFields()
               // after signin succeeded, if the checked value is true,
               // we remember the device
               if (CognitoClient.rememberDevice()) {
                   Log.i("remember device", "success")
               }
           } else {
               // display alert
               Log.i("login user", "failure")
               resetFields()
               _showFailureDialog.value = true
               _loadingAlpha.value = 0f
           }
       }

        */
/*
    fun recoverUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.recoverUser(email)) {
                Log.i("password reset", "success")
            } else {
                Log.i("password reset", "error")
            }
        }
    }
*/
/*
    fun sendVerificationCode(email: String) {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.resendVerificationCode(email)) {
                _loadingAlpha.value = 0f

            } else {
                _loadingAlpha.value = 0f
            }
        }
    }
    fun verifyConfirmCode1(email: String, code: String) {
        //_showConfirmEmailDialog.value = true
        Log.i("verify confirm code", "received: $code")
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (CognitoClient.confirmVerificationCodeSignUp(email, code)) {
                Log.i("verify code", "success")
                // show success alert
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 1
            } else {
                Log.i("verify code", "failed")
                _loadingAlpha.value = 0f
                _showConfirmEmailStatus.value = 2
            }
        }
    }
    private val _rememberDeviceCheckbox = MutableStateFlow<Boolean>(false)
    val rememberDeviceCheckbox : StateFlow<Boolean> = _rememberDeviceCheckbox.asStateFlow()
fun updateRememberDevice(newValue: Boolean) {
        _rememberDeviceCheckbox.value = newValue
    }
*/