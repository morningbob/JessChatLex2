package com.bitpunchlab.android.jesschatlex2.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.base.DialogButton
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    val _shouldChangePassword = MutableStateFlow<Boolean>(false)
    var shouldChangePassword : StateFlow<Boolean> = _shouldChangePassword.asStateFlow()

    val _currentPassword = MutableStateFlow<String>("")
    var currentPassword : StateFlow<String> = _currentPassword.asStateFlow()

    val _newPassword = MutableStateFlow<String>("")
    var newPassword : StateFlow<String> = _newPassword.asStateFlow()

    val _confirmPassword = MutableStateFlow<String>("")
    var confirmPassword : StateFlow<String> = _confirmPassword.asStateFlow()

    val _currentPassError = MutableStateFlow<String>(" ")
    var currentPassError : StateFlow<String> = _currentPassError.asStateFlow()

    val _newPassError = MutableStateFlow<String>(" ")
    var newPassError : StateFlow<String> = _newPassError.asStateFlow()

    val _confirmPassError = MutableStateFlow<String>(" ")
    var confirmPassError : StateFlow<String> = _confirmPassError.asStateFlow()

    val _changePassResult = MutableStateFlow<Int>(0)
    var changePassResult : StateFlow<Int> = _changePassResult.asStateFlow()

    val _readyChange = MutableStateFlow<Boolean>(false)
    var readyChange : StateFlow<Boolean> = _readyChange.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(currentPassError, newPassError, confirmPassError) { current, new, confirm ->
                if (current == "" && new == "" && confirm == "") {
                    _readyChange.value = true
                }
            }.collect()
        }
    }

    fun updateShouldChangePassword(newValue: Boolean) {
        _shouldChangePassword.value = newValue
    }

    fun updateCurrentPassword(newValue: String) {
        _currentPassword.value = newValue
        _currentPassError.value = InputValidation.verifyPassword(newValue)
    }

    fun updateNewPassword(newValue: String) {
        _newPassword.value = newValue
        _newPassError.value = InputValidation.verifyPassword(newValue)
    }

    fun updateConfirmPassword(newValue: String) {
        _confirmPassword.value = newValue
        _confirmPassError.value = InputValidation.verifyConfirmPass(newPassword.value, newValue)
    }

    fun updateChangePassResult(newValue: Int) {
        _changePassResult.value = newValue
        //_confirmPassError.value = InputValidation.verifyConfirmPass(newPassword.value, newValue)
    }

    // 1 is succeed, 2 is failed, corresponding dialogs will be displayed
    fun updatePassword(oldPassword: String, newPassword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _loadingAlpha.value = 1f
            if (CognitoClient.updatePassword(oldPassword, newPassword)) {
                Log.i("reset password", "success")
                // alert user
                _loadingAlpha.value = 0f
                _changePassResult.value = 1
                resetFields()
            } else {
                // alert user
                _loadingAlpha.value = 0f
                _changePassResult.value = 2
                resetFields()
            }
        }
    }

    fun resetFields() {
        _currentPassword.value = ""
        _newPassword.value = ""
        _confirmPassword.value = ""
        _currentPassError.value = " "
        _newPassError.value = " "
        _confirmPassError.value = " "
    }
}