package com.bitpunchlab.android.jesschatlex2.userAccount

import android.graphics.fonts.FontStyle
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.CreateAccount
import com.bitpunchlab.android.jesschatlex2.ForgotPassword
import com.bitpunchlab.android.jesschatlex2.Main
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.helpers.InputValidation
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.math.log


@Composable
fun LoginScreen(navController: NavHostController,
                mainViewModel: MainViewModel,
                loginViewModel: LoginViewModel = LoginViewModel(),
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_BLUE
        }
        return ColorMode.DARK_BLUE
    }

    val emailState by loginViewModel.emailState.collectAsState()
    val passwordState by loginViewModel.passwordState.collectAsState()
    val emailErrorState by loginViewModel.emailErrorState.collectAsState()
    val passwordErrorState by loginViewModel.passwordErrorState.collectAsState()
    val loadingAlpha by loginViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()
    val showFailureDialog by loginViewModel.showFailureDialog.collectAsState()
    val showConfirmEmailDialog by loginViewModel.showConfirmEmailDialog.collectAsState()
    val showConfirmEmailStatus by loginViewModel.showConfirmEmailStatus.collectAsState()
    
    val readyLogin by loginViewModel.readyLogin.collectAsState()

    val confirmEmail by loginViewModel.confirmEmail.collectAsState()
    val confirmCode by loginViewModel.confirmCode.collectAsState()
    val confirmEmailError by loginViewModel.confirmEmailError.collectAsState()
    val confirmCodeError by loginViewModel.confirmCodeError.collectAsState()

    val rememberDeviceCheckbox by loginViewModel.rememberDeviceCheckbox.collectAsState()

    // LaunchedEffect is used to run code that won't trigger recomposition of the view
    LaunchedEffect(key1 = loginState) {
        if (loginState) {
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
            //verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(JessChatLex.getColor(mode, Element.BACKGROUND)),

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.padding(top = 0.dp, bottom = 30.dp, end = 80.dp)
                    .background(JessChatLex.getColor(mode, Element.BANNER)),//JessChatLex.blueBackground),

                ) {
                TitleText(title = "Login", paddingTop = 100, paddingBottom = 100)
            }
            /*
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderImage(resource = R.mipmap.login, description = "Login Icon",
                    paddingTop = 30, paddingBottom = 0)
                TitleText(title = "Login", paddingTop = 30, paddingBottom = 30)
            }
             */
            Column(horizontalAlignment = Alignment.Start) {
                //TextField(value = loginViewModel.email, onValueChange = { loginViewModel.updateEmail(it) })
                UserInputTextField(
                    title = "Email",
                    content = emailState,
                    textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
                    textBorder = JessChatLex.getColor(mode, Element.BANNER),
                    fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                    fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                    hide = false,
                    modifier = Modifier.padding(top = 30.dp, start = 50.dp, end = 50.dp),

                ) { loginViewModel.updateEmail(it) }
                ErrorText(error = emailErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp))
                UserInputTextField(
                    title = "Password",
                    content = passwordState,
                    textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
                    textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.blueBackground,
                    fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                    fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                    hide = true,
                    modifier = Modifier.padding(top = 10.dp, start = 50.dp, end = 50.dp),
                ) { loginViewModel.updatePassword(it) }
                ErrorText(error = passwordErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 50.dp, end = 50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = rememberDeviceCheckbox,
                        onCheckedChange = {
                            loginViewModel.updateRememberDevice(it)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = JessChatLex.getColor(mode, Element.BANNER),
                            uncheckedColor = Color.White,

                            ),
                    )
                    GeneralText(
                        textString = "Remember me",
                        modifier = Modifier
                            .padding(0.dp),
                        textColor = JessChatLex.getColor(mode, Element.TEXT)
                    )
                    //Spacer(modifier = Modifier.weight(1f))
                }
            }
            AppButton(
                title = "Send",
                onClick = onSendClicked,
                shouldEnable = readyLogin,
                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightBlueBackground,
                buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                modifier = Modifier
            )
            AppButton(
                title = "Sign Up",
                onClick = onSignUpClicked,
                shouldEnable = true,
                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightBlueBackground,
                buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                modifier = Modifier
            )
            GeneralText(
                textString = "Forgot Password",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp),
                textColor = JessChatLex.getColor(mode, Element.CLICKABLE),//JessChatLex.blueBackground,
                textAlign = TextAlign.Center,
                onClick = { onForgotPasswordClicked.invoke() }
            )
            GeneralText(
                textString = "Confirm Email",
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 5.dp),
                textColor = JessChatLex.getColor(mode, Element.CLICKABLE),//JessChatLex.blueBackground,
                textAlign = TextAlign.Center,
                onClick = { loginViewModel.updateShowConfirmEmailDialog(true) }
            )

        }
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


                errorString = confirmEmailError
            )
        }

        if (showConfirmEmailStatus != 0) {
            ConfirmEmailResultDialog(confirmEmailStatus = showConfirmEmailStatus,
                loginViewModel = loginViewModel, mode = mode, error = confirmEmailError
                )
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
        title = "Login Failure",
        message = "Login failed.  Please make sure you have wifi.  Other than that, maybe the email or the password is not correct.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        onDismiss = { loginViewModel.updateShowDialog(false) },
        okOnClick = { _, _ -> loginViewModel.updateShowDialog(false) })
}

@Composable
fun ConfirmEmailDialog(emailState: String? = null,
                       codeState: String? = null,
                       loginViewModel: LoginViewModel, mode: ColorMode,
                       emailError: ((String) -> Unit)? = null,
                       errorString: String? = null
                        ) {
    CustomDialog(
        title = "Verification Code",
        message = "Please enter the verification code in the email.",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
        fieldOne = "Email",
        fieldTwo = "Confirmation Code",
        onDismiss = { loginViewModel.updateShowConfirmEmailDialog(false) },
        cancelOnClick = { _, _ ->
            loginViewModel.updateShowConfirmEmailDialog(false) },
        okOnClick = { email: String?, code: String? ->
            //if (!inputList?.get(0).isNullOrEmpty() && !inputList?.get(1).isNullOrEmpty()) {
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
                emailError!!.invoke("Both fields are required")
            }
        }
    )
}

@Composable
fun ConfirmEmailResultDialog(confirmEmailStatus: Int, error: String? = null, loginViewModel: LoginViewModel, mode: ColorMode) {
    if (confirmEmailStatus == 1) {
        CustomDialog(
            title = "Email Verification Success",
            message = "Your email and your account is verified.  Please login with the email.",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            //cancelOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },
        )
    } else if (confirmEmailStatus == 2) {
        CustomDialog(
            title = "Email Verification Failure",
            message = "We couldn't verify your email.  Please make sure you have wifi, and the confirmation code is correct.",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            //cancelOnClick = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },
        )
    } else if (confirmEmailStatus == 3 && !error.isNullOrEmpty()) {
        CustomDialog(
            title = "Confirm Email Error",
            message = error,
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightBlueBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.blueBackground,
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.blueText,
            onDismiss = { loginViewModel.updateConfirmEmailStatus(0) },
            okOnClick = { _, _ -> loginViewModel.updateConfirmEmailStatus(0) },)
    }
}


