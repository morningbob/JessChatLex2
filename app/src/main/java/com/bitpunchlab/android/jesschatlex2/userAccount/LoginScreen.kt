package com.bitpunchlab.android.jesschatlex2.userAccount

import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable.Orientation
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.CreateAccount
import com.bitpunchlab.android.jesschatlex2.ForgotPassword
import com.bitpunchlab.android.jesschatlex2.Main
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex


@Composable
fun LoginScreen(navController: NavHostController,
                mainViewModel: MainViewModel,
                loginViewModel: LoginViewModel = LoginViewModel(),
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)
){
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_BLUE
        }
        return ColorMode.DARK_BLUE
    }

    val config = LocalConfiguration.current

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val emailErrorState by loginViewModel.emailErrorState.collectAsState()
    val passwordErrorState by loginViewModel.passwordErrorState.collectAsState()
    val loadingAlpha by loginViewModel.loadingAlpha.collectAsState()
    val loginState by MobileClient.isLoggedIn.collectAsState()
    val showFailureDialog by loginViewModel.showFailureDialog.collectAsState()
    val showConfirmEmailDialog by loginViewModel.showConfirmEmailDialog.collectAsState()
    val showConfirmEmailStatus by loginViewModel.showConfirmEmailStatus.collectAsState()
    val resendCodeStatus by loginViewModel.resendCodeStatus.collectAsState()
    
    val readyLogin by loginViewModel.readyLogin.collectAsState()

    val confirmEmailError by loginViewModel.confirmEmailError.collectAsState()
    val resendCodeEmailError by loginViewModel.resendCodeEmailError.collectAsState()

    // LaunchedEffect is used to run code that won't trigger recomposition of the view
    LaunchedEffect(key1 = loginState) {
        if (loginState == true) {
            navController.navigate(Main.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {

        val mode = chooseMode()

        val onSendClicked = {
            loginViewModel.loginUser()
        }
        val onSignUpClicked = {
            navController.navigate(CreateAccount.route)
        }
        val onForgotPasswordClicked = {
            navController.navigate(ForgotPassword.route)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                //.verticalScroll(rememberScrollState())
                .background(JessChatLex.getColor(mode, Element.BACKGROUND)),

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(JessChatLex.getColor(mode, Element.BANNER)),

                ) {
                TitleText(
                    title = stringResource(R.string.login),
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.header_title_padding),
                            bottom = dimensionResource(id = R.dimen.header_title_padding)))
            }

            // Portrait mode
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(horizontalAlignment = Alignment.Start) {

                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.bit_more_space)))
                    UserInputTextField(
                        title = stringResource(R.string.email),
                        content = emailState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                        textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.more_space),
                                start = dimensionResource(R.dimen.textfield_left_padding),
                                end = dimensionResource(R.dimen.textfield_right_padding)
                            ),

                        ) { loginViewModel.updateEmail(it) }
                    ErrorText(
                        error = emailErrorState,
                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensionResource(id = R.dimen.error_left_right_padding),
                                end = dimensionResource(id = R.dimen.error_left_right_padding)
                            )
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    UserInputTextField(
                        title = stringResource(R.string.password),
                        content = passwordState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                        textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                start = dimensionResource(R.dimen.textfield_left_padding),
                                end = dimensionResource(R.dimen.textfield_right_padding)
                            ),
                    ) { loginViewModel.updatePassword(it) }
                    ErrorText(
                        error = passwordErrorState,
                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = dimensionResource(id = R.dimen.error_left_right_padding),
                                end = dimensionResource(id = R.dimen.error_left_right_padding)
                            )
                    )
                }
                //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    AppButton(
                        title = stringResource(R.string.send),
                        onClick = onSendClicked,
                        shouldEnable = readyLogin,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                start = dimensionResource(id = R.dimen.app_button_left_padding),
                                end = dimensionResource(id = R.dimen.app_button_right_padding)
                            )

                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    AppButton(
                        title = stringResource(R.string.signUp),
                        onClick = onSignUpClicked,
                        shouldEnable = true,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                start = dimensionResource(id = R.dimen.app_button_left_padding),
                                end = dimensionResource(id = R.dimen.app_button_right_padding)
                            )
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    GeneralText(
                        textString = stringResource(R.string.forgot_password),
                        modifier = Modifier
                            .padding(
                                bottom = dimensionResource(id = R.dimen.general_space),
                                top = dimensionResource(id = R.dimen.bit_more_space)),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        textAlign = TextAlign.Center,
                        onClick = { onForgotPasswordClicked.invoke() }
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    GeneralText(
                        textString = stringResource(R.string.confirm_email),
                        modifier = Modifier
                            .padding(
                                bottom = dimensionResource(id = R.dimen.general_space),
                                top = dimensionResource(id = R.dimen.general_space),
                            ),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        textAlign = TextAlign.Center,
                        onClick = { loginViewModel.updateShowConfirmEmailDialog(true) }
                    )

                    // when code = 0, nothing happen
                    // 1 = dialog, get email
                    // 2 = code sent
                    // 3 = failed to send code
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    GeneralText(
                        textString = stringResource(R.string.resend_code),
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                bottom = dimensionResource(id = R.dimen.more_space)),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        textAlign = TextAlign.Center,
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        onClick = { // get email
                            loginViewModel.updateResendCodeStatus(1)
                        }
                    )
                }
            } else {// end of if portrait
                // landscape
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = dimensionResource(id = R.dimen.land_2_col_padding),
                            end = dimensionResource(id = R.dimen.land_2_col_padding)
                        )

                ) {
                    UserInputTextField(
                        title = stringResource(R.string.email),
                        content = emailState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.more_space),
                                //start = dimensionResource(R.dimen.textfield_left_padding),
                                //end = dimensionResource(R.dimen.textfield_right_padding)
                            )

                        ) { loginViewModel.updateEmail(it) }
                    ErrorText(
                        error = emailErrorState,
                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                //start = dimensionResource(id = R.dimen.error_left_right_padding),
                            // end = dimensionResource(id = R.dimen.error_left_right_padding)
                            )
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    UserInputTextField(
                        title = stringResource(R.string.password),
                        content = passwordState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                //start = dimensionResource(R.dimen.textfield_left_padding),
                                //end = dimensionResource(R.dimen.textfield_right_padding)
                            ),
                    ) { loginViewModel.updatePassword(it) }
                    ErrorText(
                        error = passwordErrorState,
                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                //end = dimensionResource(id = R.dimen.error_left_right_padding)
                            )
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.bit_more_space),
                            start = 0.dp,
                            end = dimensionResource(id = R.dimen.app_button_left_padding)
                        )

                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AppButton(
                        title = stringResource(R.string.send),
                        onClick = onSendClicked,
                        shouldEnable = readyLogin,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.bit_more_space),
                                //start = dimensionResource(id = R.dimen.app_button_left_padding),
                                //end = dimensionResource(id = R.dimen.app_button_right_padding)
                            )
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    AppButton(
                        title = stringResource(R.string.signUp),
                        onClick = onSignUpClicked,
                        shouldEnable = true,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(id = R.dimen.bit_more_space),
                                //start = dimensionResource(id = R.dimen.app_button_left_padding),
                                //end = dimensionResource(id = R.dimen.app_button_right_padding)
                            )
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    GeneralText(
                        textString = stringResource(R.string.forgot_password),
                        modifier = Modifier
                            .padding(bottom = dimensionResource(id = R.dimen.general_space),
                            top = dimensionResource(id = R.dimen.bit_more_space)),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        textAlign = TextAlign.Center,
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        onClick = { onForgotPasswordClicked.invoke() }
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))
                    GeneralText(
                        textString = stringResource(R.string.confirm_email),
                        modifier = Modifier
                            .padding(bottom = dimensionResource(id = R.dimen.general_space),
                            top = dimensionResource(id = R.dimen.general_space)),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        textAlign = TextAlign.Center,
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        onClick = { loginViewModel.updateShowConfirmEmailDialog(true) }
                    )
                    //Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.general_space)))

                    // when code = 0, nothing happen
                    // 1 = dialog, get email
                    // 2 = code sent
                    // 3 = failed to send code
                    GeneralText(
                        textString = stringResource(R.string.resend_code),
                        modifier = Modifier
                            .padding(top = dimensionResource(id = R.dimen.general_space),
                                bottom = dimensionResource(id = R.dimen.bit_more_space)),
                        textColor = JessChatLex.getColor(mode, Element.CLICKABLE),
                        textAlign = TextAlign.Center,
                        size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                        onClick = { // get email
                            loginViewModel.updateResendCodeStatus(1)
                        }
                    )
                    } // end of landscape, 2nd column
                    } // end of landscape body row
            }

        } // end of Body column
        if (showFailureDialog) {
            LoginFailureDialog(loginViewModel = loginViewModel, mode = mode)
        }

        if (showConfirmEmailDialog) {
            ConfirmEmailDialog(loginViewModel = loginViewModel, mode = mode,
                emailError = {
                    loginViewModel.updateConfirmEmailError(it)
                    Log.i("confirm email error", "passed back to main $it")
                    loginViewModel.updateShowConfirmEmailDialog(false)
                    loginViewModel.updateConfirmEmailStatus(3)},

            )
        }

        if (showConfirmEmailStatus != 0) {
            ConfirmEmailResultDialog(confirmEmailStatus = showConfirmEmailStatus,
                loginViewModel = loginViewModel, mode = mode, error = confirmEmailError
                )
        }

        if (resendCodeStatus == 1) {
            ResendCodeDialog(
                loginViewModel = loginViewModel,
                mode = mode,
                emailError = {
                    loginViewModel.updateResendCodeEmailError(it)
                    loginViewModel.updateResendCodeStatus(0)
                    loginViewModel.updateResendCodeStatus(4)
                })
        } else if (resendCodeStatus != 0) {
            ResendCodeResultDialog(
                status = resendCodeStatus,
                loginViewModel = loginViewModel,
                mode = mode, errorString = resendCodeEmailError)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

            ) {
            CustomCircularProgressBar()
        }
    }
}

@Composable
fun LoginFailureDialog(loginViewModel: LoginViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.login_failure_title),
        message = stringResource(R.string.login_failure_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        onDismiss = { loginViewModel.updateShowDialog(false) },
        okOnClick = { _, _ -> loginViewModel.updateShowDialog(false) })
}

@Composable
fun ConfirmEmailDialog(loginViewModel: LoginViewModel, mode: ColorMode,
                       emailError: ((String) -> Unit)? = null,) {
    CustomDialog(
        title = stringResource(R.string.get_code_title),
        message = stringResource(R.string.get_code_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
        fieldOne = "Email",
        fieldTwo = "Confirmation Code",
        onDismiss = { loginViewModel.updateShowConfirmEmailDialog(false) },
        cancelOnClick = { _, _ ->
            loginViewModel.updateShowConfirmEmailDialog(false) },
        okOnClick = { email: String?, code: String? ->
            Log.i("login screen", "email: ${email}")
            Log.i("login screen", "code: ${code}")
            if (!email.isNullOrEmpty() && !code.isNullOrEmpty()) {
                val error = InputValidation.verifyEmail(email)
                if (error == "") {
                    loginViewModel.verifyConfirmCode(email, code)
                    loginViewModel.updateShowConfirmEmailDialog(false)
                } else {
                    emailError!!.invoke(error)
                }
            } else {
                emailError!!.invoke("Both fields are required.")
            }
        }
    )
}

@Composable
fun ConfirmEmailResultDialog(confirmEmailStatus: Int, error: String? = null, loginViewModel: LoginViewModel, mode: ColorMode) {
    if (confirmEmailStatus == 1) {
        CustomDialog(
            title = stringResource(R.string.email_verified_title),
            message = stringResource(R.string.email_verified_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },
        )
    } else if (confirmEmailStatus == 2) {
        CustomDialog(
            title = stringResource(R.string.email_verify_failed_title),
            message = stringResource(R.string.email_verified_failed_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },
        )
    } else if (confirmEmailStatus == 3 && !error.isNullOrEmpty()) {
        CustomDialog(
            title = stringResource(R.string.confirm_email_error),
            message = error,
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },)
    }
}

@Composable
fun ResendCodeDialog(loginViewModel: LoginViewModel, emailError: ((String) -> Unit)? = null,
                     mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.resend_code),
        message = stringResource(R.string.resend_code_content),
        backgroundColor = JessChatLex.getColor(
            mode,
            Element.BACKGROUND
        ),//JessChatLex.lightBlueBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
        fieldOne = "Email",
        onDismiss = { loginViewModel.updateResendCodeStatus(0) },
        okOnClick = { email, _ ->
            Log.i("resend code dialog", "email: $email")
            if (!email.isNullOrEmpty()) {
                val error = InputValidation.verifyEmail(email)
                if (error == "") {
                    loginViewModel.resendVerificationCode(email)
                    loginViewModel.updateResendCodeStatus(0)
                } else {
                    emailError?.invoke(error)
                }
            } else {
                emailError?.invoke("Email is required.")
            }
        },
        cancelOnClick = { _, _ ->
            loginViewModel.updateResendCodeStatus(0)
        },
    )
}

@Composable
fun ResendCodeResultDialog(status: Int, loginViewModel: LoginViewModel, mode: ColorMode,
    errorString: String? = null) {
    if (status == 2) {
        CustomDialog(
            title = stringResource(R.string.resend_code),
            message = stringResource(R.string.resend_code_sent_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { loginViewModel.updateResendCodeStatus(0)},
            okOnClick = { _, _ -> loginViewModel.updateResendCodeStatus(0)},
            )
    } else if (status == 3) {
        CustomDialog(
            title = stringResource(R.string.resend_code),
            message = stringResource(R.string.resend_code_error_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { loginViewModel.updateResendCodeStatus(0)},
            okOnClick = { _, _ -> loginViewModel.updateResendCodeStatus(0)})
    } else if (status == 4 && !errorString.isNullOrEmpty()) {
        CustomDialog(
            title = stringResource(R.string.resend_code_error_title),
            message = errorString,
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { loginViewModel.updateResendCodeStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateResendCodeStatus(0)})
    }
}
/*
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                    paddingTop = 30, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 30, paddingBottom = 30)
            }
             */
