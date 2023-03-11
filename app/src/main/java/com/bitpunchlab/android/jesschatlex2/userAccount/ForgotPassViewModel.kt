package com.bitpunchlab.android.jesschatlex2.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ForgotPassViewModel : ViewModel() {

    private val _verificationCode = MutableStateFlow<String>("")
    val verificationCode : StateFlow<String> = _verificationCode.asStateFlow()

    private val _email = MutableStateFlow<String>("")
    val email : StateFlow<String> = _email.asStateFlow()

    private val _newPassword = MutableStateFlow<String>("")
    val newPassword : StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPass = MutableStateFlow<String>("")
    val confirmPass : StateFlow<String> = _confirmPass

    private val _newPassError = MutableStateFlow<String>(" ")
    val newPassError : StateFlow<String> = _newPassError

    private val _emailError = MutableStateFlow<String>(" ")
    val emailError : StateFlow<String> = _emailError

    private val _codeError = MutableStateFlow<String>(" ")
    val codeError : StateFlow<String> = _codeError.asStateFlow()

    private val _confirmPassError = MutableStateFlow<String>(" ")
    val confirmPassError : StateFlow<String> = _confirmPassError

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    private val _readyReset = MutableStateFlow<Boolean>(false)
    val readyReset : StateFlow<Boolean> = _readyReset.asStateFlow()

    private val _readyRequest = MutableStateFlow<Boolean>(false)
    val readyRequest : StateFlow<Boolean> = _readyRequest.asStateFlow()

    private val _resetPasswordStatus = MutableStateFlow<Int>(0)
    val resetPasswordStatus : StateFlow<Int> = _resetPasswordStatus.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(emailError, newPassError, confirmPassError, codeError) { email, pass, confirm, code ->
                _readyReset.value = email == "" && pass == "" && code == "" && confirm == ""
                _readyRequest.value = email == ""
            }.collect()
        }

    }

    fun updateVerificationCode(newValue: String) {
        _verificationCode.value = newValue
        _codeError.value = InputValidation.verifyCode(newValue)
    }

    fun updateEmail(newValue: String) {
        _email.value = newValue
        _emailError.value = InputValidation.verifyEmail(newValue)
    }

    fun updateNewPassword(newValue: String) {
        _newPassword.value = newValue
        _newPassError.value = InputValidation.verifyPassword(newValue)
    }

    fun updateConfirmPassword(newValue: String) {
        _confirmPass.value = newValue
        _confirmPassError.value = InputValidation.verifyConfirmPass(newPassword.value, newValue)
    }

    fun requestCode(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //if (CognitoClient.recoverUser(email)) {
            if (MobileClient.forgotPassword(email)) {
                Log.i("password reset", "code sent success")
                _resetPasswordStatus.value = 3
                resetFields()
            } else {
                Log.i("password reset", "error")
                _resetPasswordStatus.value = 4
                resetFields()
            }
        }
    }

    fun verifyCodeAndChangePassword(email: String, newPass: String, code: String) {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            //if (CognitoClient.confirmResetPassword(email, newPass, code)) {
            if (MobileClient.confirmForgotPassword(email, newPass, code)) {
                // display alert
                _loadingAlpha.value = 0f
                _resetPasswordStatus.value = 1
                resetFields()
            } else {
                _loadingAlpha.value = 0f
                _resetPasswordStatus.value = 2
                resetFields()
            }
        }
    }

    fun updateResetPasswordStatus(newValue: Int) {
        _resetPasswordStatus.value = newValue
    }

    private fun resetFields() {
        _email.value = ""
        _verificationCode.value = ""
        _newPassword.value = ""
        _confirmPass.value = ""
        _emailError.value = " "
        _codeError.value = " "
        _newPassError.value = " "
        _confirmPassError.value = " "
    }
}