package com.bitpunchlab.android.jesschatlex2.userAccount

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

@Composable
fun ForgotPasswordScreen(navController: NavHostController,
    forgotPassViewModel: ForgotPassViewModel = viewModel()) {

    val verificationCode by forgotPassViewModel.verificationCode.collectAsState()
    val email by forgotPassViewModel.email.collectAsState()
    val newPassword by forgotPassViewModel.newPassword.collectAsState()
    val confirmPass by forgotPassViewModel.confirmPass.collectAsState()
    val codeError by forgotPassViewModel.codeError.collectAsState()
    val emailError by forgotPassViewModel.emailError.collectAsState()
    val newPassError by forgotPassViewModel.newPassError.collectAsState()
    val confirmPassError by forgotPassViewModel.confirmPassError.collectAsState()
    val readyReset by forgotPassViewModel.readyReset.collectAsState()
    val readyRequest by forgotPassViewModel.readyRequest.collectAsState()
    val resetPasswordStatus by forgotPassViewModel.resetPasswordStatus.collectAsState()

    var chosenOption by remember {
        mutableStateOf(0)
    }

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_BROWN
        }
        return ColorMode.DARK_BROWN
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        ) {
        val mode = chooseMode()

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .background(JessChatLex.getColor(mode, Element.BACKGROUND)),) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(JessChatLex.getColor(mode, Element.BANNER)),

                ) {
                TitleText(
                    title = stringResource(R.string.forgot_password),
                    modifier = Modifier
                        .padding(top = dimensionResource(id = R.dimen.header_title_padding),
                            bottom = dimensionResource(id = R.dimen.header_title_padding)))
            }
            Text(
                text = stringResource(R.string.reset_password_desc),
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.bit_more_space),
                        bottom = dimensionResource(id = R.dimen.bit_more_space),
                        start = dimensionResource(id = R.dimen.app_button_left_padding),
                        end = dimensionResource(id = R.dimen.app_button_right_padding)),
                color = JessChatLex.getColor(mode, Element.TEXT)
            )
            if (chosenOption == 0) {
                AppButton(
                    title = stringResource(R.string.have_code),
                    onClick = {
                        chosenOption = 1
                              },
                    shouldEnable = true,
                    buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                    buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                    buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.app_button_left_padding),
                            end = dimensionResource(id = R.dimen.app_button_right_padding),
                        )
                )
                Spacer(modifier = Modifier.width(50.dp))
                AppButton(
                    title = stringResource(R.string.donot_have_code),
                    onClick = {
                        chosenOption = 2
                              },
                    shouldEnable = true,
                    buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                    buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                    buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(id = R.dimen.app_button_left_padding),
                            end = dimensionResource(id = R.dimen.app_button_left_padding),
                        )
                )
            }

            if (chosenOption == 1) {
                ResetPasswordWidget(navigateCode = { chosenOption = 2 }, email = email, emailError = emailError, code = verificationCode,
                    codeError = codeError, newPassword = newPassword, newPassError = newPassError, 
                    confirmPass = confirmPass, confirmPassError = confirmPassError, readyReset = readyReset,
                    forgotPassViewModel = forgotPassViewModel, mode = mode)
            } else if (chosenOption == 2) {
                RequestCodeWidget(navigateRequest = { chosenOption = 1 }, email = email,
                    emailError = emailError, readyRequest = readyRequest,
                    forgotPassViewModel =forgotPassViewModel, mode = mode)
            }

        } // column
        when (resetPasswordStatus) {
            // change password succeeded
            1 -> {
                ResetPasswordSucceededDialog(forgotPassViewModel = forgotPassViewModel, mode = mode)
            }
            // change password failed
            2 -> {
                ResetPasswordFailedDialog(forgotPassViewModel = forgotPassViewModel, mode = mode)
            }
            // request verification code succeeded
            3 -> {
                SendCodeSucceededDialog(forgotPassViewModel = forgotPassViewModel, mode = mode)
            }
            // request verification code failed
            4 -> {
                SendCodeFailedDialog(forgotPassViewModel = forgotPassViewModel, mode = mode)
            }
        }
    } // surface
}

@Composable
fun ResetPasswordWidget(navigateCode: () -> Unit, email: String, emailError: String, code: String, codeError: String,
                        newPassword: String, newPassError: String, confirmPass: String,
                        confirmPassError: String, readyReset: Boolean, forgotPassViewModel: ForgotPassViewModel,
                        mode: ColorMode) {
    Column() {
        UserInputTextField(
            title = stringResource(R.string.email),
            content = email,
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            textBorder = JessChatLex.getColor(mode, Element.BANNER),
            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 70.dp, end = 70.dp),
            call = { forgotPassViewModel.updateEmail(it) })
        ErrorText(
            error = emailError,
            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
            )

        UserInputTextField(
            title = stringResource(R.string.enter_code),
            content = code,
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            textBorder = JessChatLex.getColor(mode, Element.BANNER),
            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 70.dp, end = 70.dp),
            call = { forgotPassViewModel.updateVerificationCode(it) })
        ErrorText(
            error = codeError,
            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp)
        )


        UserInputTextField(
            title = stringResource(R.string.enter_new_pass),
            content = newPassword,
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            textBorder = JessChatLex.getColor(mode, Element.BANNER),
            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
            hide = false,
            modifier = Modifier
                .padding(top = 4.dp, start = 70.dp, end = 70.dp),
            call = { forgotPassViewModel.updateNewPassword(it) })

        ErrorText(
            error = newPassError,
            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
        )


        UserInputTextField(
            title = stringResource(R.string.confirm_pass),
            content = confirmPass,
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            textBorder = JessChatLex.getColor(mode, Element.BANNER),
            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
            hide = false,
            modifier = Modifier
                .padding(top = 4.dp, start = 70.dp, end = 70.dp),
            call = { forgotPassViewModel.updateConfirmPassword(it) })

        ErrorText(
            error = confirmPassError,
            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
        )

        Spacer(modifier = Modifier.width(5.dp))

        AppButton(
            title = stringResource(R.string.change_pass),
            onClick = {
                forgotPassViewModel.verifyCodeAndChangePassword(
                    email,
                    newPassword,
                    code
                )
            },
            shouldEnable = readyReset,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            modifier = Modifier
        )

        Text(
            text = stringResource(R.string.no_code),
            fontSize = 18.sp,
            color = JessChatLex.getColor(mode, Element.CLICKABLE),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 50.dp)
                .clickable(enabled = true) {
                    navigateCode.invoke()
                },
            textAlign = TextAlign.Center

        )
    }
    
}

@Composable
fun RequestCodeWidget(navigateRequest: () -> Unit, email: String, emailError: String, readyRequest: Boolean,
                      forgotPassViewModel: ForgotPassViewModel, mode: ColorMode) {
    Column() {
        UserInputTextField(
            title = stringResource(R.string.email),
            content = email,
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            textBorder = JessChatLex.getColor(mode, Element.BANNER),
            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
            hide = false,
            modifier = Modifier
                .padding(top = 10.dp, start = 70.dp, end = 70.dp),
            call = { forgotPassViewModel.updateEmail(it) })

        ErrorText(
            error = emailError,
            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
            modifier = Modifier
                .padding(top = 2.dp, start = 30.dp, end = 30.dp),
        )

        Spacer(modifier = Modifier.width(5.dp))

        AppButton(
            title = stringResource(R.string.request_code),
            onClick = {
                forgotPassViewModel.requestCode(email)
            },
            shouldEnable = readyRequest,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            modifier = Modifier
        )

        Text(
            text = stringResource(R.string.have_code),
            fontSize = 18.sp,
            color = JessChatLex.getColor(mode, Element.CLICKABLE),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 50.dp)
                .clickable(enabled = true) {
                    navigateRequest.invoke()
                },
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun ResetPasswordSucceededDialog(forgotPassViewModel: ForgotPassViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.reset_pass_success_title),
        message = stringResource(R.string.reset_pass_success_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { _, _ -> forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun ResetPasswordFailedDialog(forgotPassViewModel: ForgotPassViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.reset_pass_failed_title),
        message = stringResource(R.string.reset_pass_failed_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0)},
        okOnClick = { _, _ -> forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun SendCodeSucceededDialog(forgotPassViewModel: ForgotPassViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.code_sent_title),
        message = stringResource(R.string.code_sent_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { _, _ -> forgotPassViewModel.updateResetPasswordStatus(0) })
}

@Composable
fun SendCodeFailedDialog(forgotPassViewModel: ForgotPassViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.code),
        message = stringResource(R.string.send_code_error_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { forgotPassViewModel.updateResetPasswordStatus(0) },
        okOnClick = { _, _ -> forgotPassViewModel.updateResetPasswordStatus(0) })
}

