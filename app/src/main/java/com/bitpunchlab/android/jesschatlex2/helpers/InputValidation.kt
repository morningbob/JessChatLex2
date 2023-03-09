package com.bitpunchlab.android.jesschatlex2.helpers

import android.util.Log

object InputValidation {

    fun verifyName(inputName: String) : String {
        return when (inputName) {
            "" -> {
                "Name must not be blank."
            }
            else -> {
                ""
            }
        }
    }

    fun verifyEmail(inputEmail: String) : String {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            return "Email is invalid."
        }
        return ""
    }

    fun verifyPassword(inputPassword: String) : String {
        if (inputPassword.length < 8) return "Password must contain at least 8 characters."
        if (inputPassword.filter { !it.isLetter() }.firstOrNull() == null)
            return "Password must contain a special character or a number."
        return ""
    }

    fun verifyConfirmPass(password: String, confirmPassword: String) : String {
        if (password != confirmPassword) {
            return "Password and confirm password must be the same."
        }
        return ""
    }

    fun verifyCode(code: String) : String {
        if (code.length < 5 || code.length > 8) {
            return "Invalid verification code"
        }
        try {
            val num = code.toInt()
            return ""
        } catch (exception: NumberFormatException) {
            Log.i("verify code", "code is not a number")
            return "Wrong verification code"
        }
    }
}