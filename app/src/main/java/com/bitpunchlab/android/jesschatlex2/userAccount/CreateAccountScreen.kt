package com.bitpunchlab.android.jesschatlex2.userAccount

import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.kotlin.core.Amplify
import com.bitpunchlab.android.jesschatlex2.Login
import com.bitpunchlab.android.jesschatlex2.Main
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.awsClient.CognitoClient
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex
import kotlinx.coroutines.*

@Composable
fun CreateAccountScreen(navController: NavHostController,
    mainViewModel: MainViewModel,
    registerViewModel: RegisterViewModel = viewModel())
{
    val nameState by registerViewModel.nameState.collectAsState()
    val emailState by registerViewModel.emailState.collectAsState()
    val passwordState by registerViewModel.passwordState.collectAsState()
    val confirmPassState by registerViewModel.confirmPassState.collectAsState()
    val nameErrorState by registerViewModel.nameErrorState.collectAsState()
    val emailErrorState by registerViewModel.emailErrorState.collectAsState()
    val passwordErrorState by registerViewModel.passwordErrorState.collectAsState()
    val confirmPassErrorState by registerViewModel.confirmPassErrorState.collectAsState()
    val loadingAlpha by registerViewModel.loadingAlpha.collectAsState()
    val loginState by mainViewModel.isLoggedIn.collectAsState()
    val showRegistrationStatusDialog by registerViewModel.showRegistrationStatusDialog.collectAsState()
    val readyRegister by registerViewModel.readyRegister.collectAsState()
    val shouldRedirectLogin by registerViewModel.shouldRedirectLogin.collectAsState()

    // navigate to main page if the user successfully created the account
    // or when user navigate to this page by back button
    // but he is already logged in

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_GREEN
        }
        return ColorMode.DARK_GREEN
    }

    LaunchedEffect(key1 = loginState) {
        if (loginState) {
            navController.navigate(Main.route)
        }
    }

    LaunchedEffect(key1 = shouldRedirectLogin) {
        if (shouldRedirectLogin) {
            navController.navigate(Login.route)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        //color = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.lightGreenBackground
    ) {
        val mode = chooseMode()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(JessChatLex.getColor(mode, Element.BACKGROUND)),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                var onSendClicked = {
                    registerViewModel.registerUser()

                }
                var onLoginClicked = {
                    //registerViewModel.navigateLogin()
                    navController.navigate(Login.route)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        //.padding(top = 100.dp, bottom = 30.dp)
                        .background(JessChatLex.getColor(mode, Element.BANNER)),//JessChatLex.greenBackground),

                    ) {
                    TitleText(title = "Register", paddingTop = 100, paddingBottom = 100)
                }

                //HeaderImage(
                //    resource = R.mipmap.adduser, description = "Create Account logo",
                //    paddingTop = 30, paddingBottom = 0
                //)
                //TitleText(title = "Create Account", paddingTop = 30, paddingBottom = 30)
                Column(horizontalAlignment = Alignment.Start) {
                    UserInputTextField(title = "Name", content = nameState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.greenBackground,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = false,
                        modifier = Modifier.padding(top = 30.dp, start = 50.dp, end = 50.dp)
                    ) { registerViewModel.updateName(it) }
                    ErrorText(error = nameErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    )
                    UserInputTextField(title = "Email", content = emailState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.greenBackground,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = false,
                        modifier = Modifier.padding(top = 10.dp, start = 50.dp, end = 50.dp)
                    ) { registerViewModel.updateEmail(it) }
                    ErrorText(error = emailErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    )
                    UserInputTextField(title = "Password", content = passwordState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.greenBackground,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = true,
                        modifier = Modifier.padding(top = 10.dp, start = 50.dp, end = 50.dp)
                    ) { registerViewModel.updatePassword(it) }
                    ErrorText(error = passwordErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    )
                    UserInputTextField(title = "Confirm Password", content = confirmPassState,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
                        textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.greenBackground,
                        fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                        fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                        hide = true,
                        modifier = Modifier.padding(top = 10.dp, start = 50.dp, end = 50.dp)) {
                        registerViewModel.updateConfirmPassword(it)
                    }
                    ErrorText(error = confirmPassErrorState, color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(bottom = 50.dp)
                ) {
                    AppButton(
                        title = "Send",
                        onClick = onSendClicked,
                        shouldEnable = readyRegister,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.greenBackground,
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightGreenBackground,
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                    )
                    AppButton(
                        title = "Login",
                        onClick = onLoginClicked,
                        shouldEnable = true,
                        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.greenBackground,
                        buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightGreenBackground,
                        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                        modifier = Modifier
                    )

                }
            }
        }
        if (showRegistrationStatusDialog != 0) {
            RegistrationStatusDialog(status = showRegistrationStatusDialog,
                registerViewModel = registerViewModel, mode = mode)
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
fun RegistrationStatusDialog(status: Int, registerViewModel: RegisterViewModel, mode: ColorMode) {
    if (status == 1) {
        CustomDialog(
            title = "Registration Success",
            message = "You are successfully registered.  We sent a verification code to your email.  You need to confirm your email before logging in.",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightGreenBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.greenBackground,
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { registerViewModel.updateRegistrationStatusDialog(0) },
            okOnClick = { _, _ -> registerViewModel.updateRegistrationStatusDialog(0) }
        )
    } else if (status == 2) {
        CustomDialog(
            title = "Registration Failure",
            message = "There is error registering your account.  Please make sure you have wifi, and the email is not registered before.  Other than that, the server may be in maintenance.  If the problem persists, please contact admin@jessbitcom.pro",
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightGreenBackground,
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.greenBackground,
            textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.greenText,
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { registerViewModel.updateRegistrationStatusDialog(0) },
            okOnClick = { _, _ -> registerViewModel.updateRegistrationStatusDialog(0) }
        )
    }
}




