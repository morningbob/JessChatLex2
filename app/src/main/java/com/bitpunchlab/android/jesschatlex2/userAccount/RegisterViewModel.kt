package com.bitpunchlab.android.jesschatlex2.userAccount

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


class RegisterViewModel() : ViewModel() {

    private val _nameState = MutableStateFlow<String>("")
    val nameState : StateFlow<String> = _nameState.asStateFlow()

    private val _emailState = MutableStateFlow<String>("")
    val emailState : StateFlow<String> = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow<String>("")
    val passwordState : StateFlow<String> = _passwordState.asStateFlow()

    private val _confirmPassState = MutableStateFlow<String>("")
    val confirmPassState : StateFlow<String> = _confirmPassState.asStateFlow()

    // default the errors with space, that distinguish when the app starts,
    // shouldn't let send button enabled.

    private val _nameErrorState = MutableStateFlow<String>(" ")
    val nameErrorState : StateFlow<String> = _nameErrorState.asStateFlow()

    private val _emailErrorState = MutableStateFlow<String>(" ")
    val emailErrorState : StateFlow<String> = _emailErrorState.asStateFlow()

    private val _passwordErrorState = MutableStateFlow<String>(" ")
    val passwordErrorState : StateFlow<String> = _passwordErrorState.asStateFlow()

    private val _confirmPassErrorState = MutableStateFlow<String>(" ")
    val confirmPassErrorState : StateFlow<String> = _confirmPassErrorState.asStateFlow()

    private val _loadingAlpha = MutableStateFlow<Float>(0f)
    val loadingAlpha: StateFlow<Float> = _loadingAlpha.asStateFlow()

    var _readyRegister = MutableStateFlow<Boolean>(false)
    val readyRegister: StateFlow<Boolean> = _readyRegister.asStateFlow()

    private val _shouldRedirectLogin = MutableStateFlow<Boolean>(false)
    val shouldRedirectLogin: StateFlow<Boolean> = _shouldRedirectLogin.asStateFlow()

    private val _showRegistrationStatusDialog = MutableStateFlow<Int>(0)
    val showRegistrationStatusDialog : StateFlow<Int> = _showRegistrationStatusDialog.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            combine(nameErrorState, emailErrorState, passwordErrorState, confirmPassErrorState)
            { name, email, password, confirm ->
                _readyRegister.value = name == "" && email == "" && password == "" && confirm == ""
            }.collect {
                Log.i("combine", "readyRegister: $readyRegister")
            }
        }
    }

    fun updateName(inputName: String) {
        _nameState.value = inputName
        _nameErrorState.value = InputValidation.verifyName(inputName)
    }
    fun updateEmail(inputEmail: String) {
        _emailState.value = inputEmail
        _emailErrorState.value = InputValidation.verifyEmail(inputEmail)
    }
    fun updatePassword(inputPass: String) {
        _passwordState.value = inputPass
        _passwordErrorState.value = InputValidation.verifyPassword(inputPass)
    }
    fun updateConfirmPassword(inputConfirm: String) {
        _confirmPassState.value = inputConfirm
        _confirmPassErrorState.value = InputValidation.verifyConfirmPass(passwordState.value, inputConfirm)
    }


    fun registerUser() {
        _loadingAlpha.value = 1f
        CoroutineScope(Dispatchers.IO).launch {
            if (MobileClient.signUp(nameState.value, emailState.value, passwordState.value)) {
                _showRegistrationStatusDialog.value = 1
                _loadingAlpha.value = 0f
                resetFields()
            } else {
                _showRegistrationStatusDialog.value = 2
                _loadingAlpha.value = 0f
                resetFields()
            }
        }

    }

    private fun resetFields() {
        _nameState.value = ""
        _emailState.value = ""
        _passwordState.value = ""
        _confirmPassState.value = ""
        _nameErrorState.value = " "
        _emailErrorState.value = " "
        _passwordErrorState.value = " "
        _confirmPassErrorState.value = " "
    }

    fun updateRegistrationStatusDialog(newValue: Int) {
        _showRegistrationStatusDialog.value = newValue
    }
}
