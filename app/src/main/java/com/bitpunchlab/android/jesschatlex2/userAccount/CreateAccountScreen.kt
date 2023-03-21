package com.bitpunchlab.android.jesschatlex2.userAccount

import android.content.res.Configuration
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.Login
import com.bitpunchlab.android.jesschatlex2.Main
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

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
    val loginState by MobileClient.isLoggedIn.collectAsState()
    val showRegistrationStatusDialog by registerViewModel.showRegistrationStatusDialog.collectAsState()
    val readyRegister by registerViewModel.readyRegister.collectAsState()
    val shouldRedirectLogin by registerViewModel.shouldRedirectLogin.collectAsState()

    // navigate to main page if the user successfully created the account
    // or when user navigate to this page by back button
    // but he is already logged in

    val config = LocalConfiguration.current

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_GREEN
        }
        return ColorMode.DARK_GREEN
    }

    LaunchedEffect(key1 = loginState) {
        if (loginState == true) {
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
    ) {
        val mode = chooseMode()

        Column(
            modifier = Modifier
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
                .background(JessChatLex.getColor(mode, Element.BACKGROUND)),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            var onSendClicked = {
                registerViewModel.registerUser()

            }
            var onLoginClicked = {
                navController.navigate(Login.route)
            }
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                JessChatLex.getColor(
                                    mode,
                                    Element.BANNER
                                )
                            ),

                    ) {
                        TitleText(
                            title = stringResource(R.string.register),
                            modifier = Modifier
                                .padding(
                                    top = dimensionResource(id = R.dimen.header_title_padding),
                                    bottom = dimensionResource(id = R.dimen.header_title_padding))
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier) {
                            //.verticalScroll(rememberScrollState())) {
                        UserInputTextField(
                            title = stringResource(R.string.name), content = nameState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            hide = false,
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updateName(it) }
                        ErrorText(
                            error = nameErrorState,
                            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimensionResource(id = R.dimen.error_left_right_padding),
                                    end = dimensionResource(id = R.dimen.error_left_right_padding)
                                )
                        )
                        UserInputTextField(
                            title = stringResource(R.string.email), content = emailState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updateEmail(it) }
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
                        UserInputTextField(
                            title = stringResource(R.string.password), content = passwordState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updatePassword(it) }
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
                        UserInputTextField(
                            title = stringResource(R.string.confirm_password),
                            content = confirmPassState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) {
                            registerViewModel.updateConfirmPassword(it)
                        }
                        ErrorText(
                            error = confirmPassErrorState,
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
                    Column(
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.general_space),
                                bottom = dimensionResource(id = R.dimen.more_space)),
                            //.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        //verticalArrangement = Arrangement.Center

                    ) {
                        AppButton(
                            title = stringResource(R.string.send),
                            onClick = onSendClicked,
                            shouldEnable = readyRegister,
                            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                            buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = dimensionResource(id = R.dimen.app_button_left_padding),
                                    end = dimensionResource(id = R.dimen.app_button_right_padding)
                                )
                        )
                        AppButton(
                            title = stringResource(R.string.login),
                            onClick = onLoginClicked,
                            shouldEnable = true,
                            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                            buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    start = dimensionResource(id = R.dimen.app_button_left_padding),
                                    end = dimensionResource(id = R.dimen.app_button_right_padding)
                                )
                        )
                    }
                } // end of portrait body column
            } // end of portrait if
            else {
                // landscape
                Column(
                    modifier = Modifier
                        .fillMaxSize(),

                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                JessChatLex.getColor(
                                    mode,
                                    Element.BANNER
                                )
                            ),

                    ) {
                        TitleText(
                            title = stringResource(R.string.register),
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.header_title_padding),
                                    bottom = dimensionResource(id = R.dimen.header_title_padding))
                        )
                    }
                    Row(
                        modifier = Modifier,
                    ) {

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .fillMaxHeight()
                            .padding(
                                start = dimensionResource(id = R.dimen.land_2_col_padding),
                                end = dimensionResource(id = R.dimen.land_2_col_padding)
                            )
                            .verticalScroll(rememberScrollState()),
                    ) {

                        UserInputTextField(
                            title = stringResource(R.string.name), content = nameState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    //start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    //end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updateName(it) }
                        ErrorText(
                            error = nameErrorState,
                            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                    //end = dimensionResource(id = R.dimen.error_left_right_padding)
                                )
                        )
                        UserInputTextField(
                            title = stringResource(R.string.email), content = emailState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    //start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    //end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updateEmail(it) }
                        ErrorText(
                            error = emailErrorState,
                            color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                    //end = dimensionResource(id = R.dimen.error_left_right_padding)
                                )
                        )
                        UserInputTextField(
                            title = stringResource(R.string.password), content = passwordState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    //start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    //end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) { registerViewModel.updatePassword(it) }
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
                        UserInputTextField(
                            title = stringResource(R.string.confirm_password),
                            content = confirmPassState,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textBorder = JessChatLex.getColor(mode, Element.BANNER),
                            fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                            fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                            textSize = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            hide = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    //start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    //end = dimensionResource(id = R.dimen.textfield_right_padding)
                                )
                        ) {
                            registerViewModel.updateConfirmPassword(it)
                        }
                        ErrorText(
                            error = confirmPassErrorState,
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
                        // 2nd column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.bit_more_space),
                                end = dimensionResource(id = R.dimen.app_button_left_padding),
                                //end =
                                bottom = dimensionResource(id = R.dimen.more_space)
                            )
                            .fillMaxHeight(),
                            //verticalArrangement = Arrangement.SpaceEvenly

                    ) {
                        AppButton(
                            title = stringResource(R.string.send),
                            onClick = onSendClicked,
                            shouldEnable = readyRegister,
                            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                            buttonBackground = JessChatLex.getColor(
                                mode,
                                Element.BUTTON_BACKGROUND
                            ),
                            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.more_space),
                                    //start = dimensionResource(id = R.dimen.app_button_left_padding),
                                    //end = dimensionResource(id = R.dimen.app_button_right_padding),
                                    bottom = dimensionResource(id = R.dimen.general_space)
                                )
                        )
                        AppButton(
                            title = stringResource(R.string.login),
                            onClick = onLoginClicked,
                            shouldEnable = true,
                            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                            buttonBackground = JessChatLex.getColor(
                                mode,
                                Element.BUTTON_BACKGROUND
                            ),
                            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    //start = dimensionResource(id = R.dimen.app_button_left_padding),
                                    //end = dimensionResource(id = R.dimen.app_button_right_padding)
                                )
                        )
                    }
                    }
                } // end of landscape body column
            }

        } // end of body column
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
            title = stringResource(R.string.registration_success_title),
            message = stringResource(R.string.registration_success_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { registerViewModel.updateRegistrationStatusDialog(0) },
            okOnClick = { _, _ -> registerViewModel.updateRegistrationStatusDialog(0) }
        )
    } else if (status == 2) {
        CustomDialog(
            title = stringResource(R.string.registration_failure_title),
            message = stringResource(R.string.registration_failure_content),
            backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
            buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
            textColor = JessChatLex.getColor(mode, Element.TEXT),
            buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
            onDismiss = { registerViewModel.updateRegistrationStatusDialog(0) },
            okOnClick = { _, _ -> registerViewModel.updateRegistrationStatusDialog(0) }
        )
    }
}
//HeaderImage(
//    resource = R.mipmap.adduser, description = "Create Account logo",
//    paddingTop = 30, paddingBottom = 0
//)
//TitleText(title = "Create Account", paddingTop = 30, paddingBottom = 30)




