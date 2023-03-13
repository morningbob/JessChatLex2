package com.bitpunchlab.android.jesschatlex2.userAccount

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.Login
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.base.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.main.BottomNavigationBar
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavHostController, mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel = viewModel()) {

    val loginState by MobileClient.isLoggedIn.collectAsState()

    val userName by mainViewModel.userName.collectAsState()
    val userEmail by mainViewModel.userEmail.collectAsState()
    val shouldChangePassword by profileViewModel.shouldChangePassword.collectAsState()
    val currentPassword by profileViewModel.currentPassword.collectAsState()
    val newPassword by profileViewModel.newPassword.collectAsState()
    val confirmPassword by profileViewModel.confirmPassword.collectAsState()
    val currentPassError by profileViewModel.currentPassError.collectAsState()
    val newPassError by profileViewModel.newPassError.collectAsState()
    val confirmPassError by profileViewModel.confirmPassError.collectAsState()
    val changePassResult by profileViewModel.changePassResult.collectAsState()
    val readyChange by profileViewModel.readyChange.collectAsState()
    val loadingAlpha by profileViewModel.loadingAlpha.collectAsState()

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT_PURPLE
        }
        return ColorMode.DARK_PURPLE
    }

    LaunchedEffect(key1 = loginState) {
        if (loginState == false) {
            navController.navigate(Login.route) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
            }
        }
    }

    //val paddingValues = WindowInsets.navigationBars.asPaddingValues()
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val mode = chooseMode()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        JessChatLex.getColor(
                            mode,
                            Element.BACKGROUND
                        )
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .background(
                            JessChatLex.getColor(
                                mode,
                                Element.BANNER
                            )
                        )
                        .padding(top = 20.dp, bottom = 20.dp),

                    ) {
                    TitleText(
                        title = stringResource(R.string.profile),
                        modifier = Modifier
                        .padding(top = 100.dp, bottom = 100.dp))
                }
                Column(
                    modifier = Modifier
                        .background(JessChatLex.getColor(mode, Element.BACKGROUND)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GeneralText(
                        textString = stringResource(R.string.name),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 10.dp),
                        size = 18.sp,
                        textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT),
                    )
                    GeneralText(
                        textString = userName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                        textAlign = TextAlign.Center,
                        size = 18.sp
                    )
                    GeneralText(
                        textString = stringResource(R.string.email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                        textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT)
                    )
                    GeneralText(
                        textString = userEmail,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 12.dp),
                        textAlign = TextAlign.Center,
                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                    )
                    GeneralText(
                        textString = stringResource(R.string.password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 5.dp),
                        textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT)
                    )
                    if (!shouldChangePassword) {
                        Text(
                            text = stringResource(R.string.change_pass),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(
                                    enabled = true,
                                    onClick = {
                                        profileViewModel.updateShouldChangePassword(true)
                                    }),
                            color = JessChatLex.getColor(mode, Element.CLICKABLE),
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                        ) {
                            UserInputTextField(
                                title = stringResource(R.string.current_pass),
                                content = currentPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                                fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                                hide = false,
                                modifier = Modifier.padding(bottom = 2.dp, start = 50.dp, end = 50.dp),
                                call = { profileViewModel.updateCurrentPassword(it)} )
                            ErrorText(
                                error = currentPassError,
                                color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                modifier = Modifier
                                    .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                            )
                            UserInputTextField(
                                title = stringResource(R.string.new_pass),
                                content = newPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                                fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                                hide = false,
                                modifier = Modifier.padding(bottom = 2.dp, start = 50.dp, end = 50.dp),
                                call = { profileViewModel.updateNewPassword(it)} )
                            ErrorText(
                                error = newPassError,
                                color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                modifier = Modifier
                                    .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                            )

                            UserInputTextField(
                                title = stringResource(R.string.confirm_password),
                                content = confirmPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                                fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                                hide = false,
                                modifier = Modifier.padding(bottom = 2.dp, start = 50.dp, end = 50.dp),
                                call = { profileViewModel.updateConfirmPassword(it) },
                            )
                            ErrorText(
                                error = confirmPassError,
                                color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                modifier = Modifier
                                    .padding(bottom = 4.dp, start = 20.dp, end = 20.dp),
                            )

                            AppButton(
                                title = stringResource(R.string.send),
                                onClick = {
                                    profileViewModel.checkAndUpdatePassword(currentPassword, newPassword)
                                },
                                shouldEnable = readyChange,
                                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
                                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),
                                buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                                modifier = Modifier
                            )
                            //Spacer(modifier = Modifier.width(200.dp))
                            // this column is used to set the bottom padding , so that the content won't be
                            // overlapped by the bottom navigation bar
                            Column() {
                                Text(
                                    text="a",
                                    modifier = Modifier.padding(top = 100.dp)
                                )
                            }
                        }

                    }

                }
                }

            if (changePassResult == 1) {
                ChangePasswordSuccessDialog(profileViewModel = profileViewModel, mode = mode)
            } else if (changePassResult == 2) {
                ChangePasswordFailureDialog(profileViewModel = profileViewModel, mode = mode)
            } else if (changePassResult == 3) {
                PasswordsSameErrorDialog(profileViewModel = profileViewModel, mode = mode)
            }// 0 - no dialog
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(loadingAlpha),

                ) {
                CustomCircularProgressBar()
            }
        } // scaffold
    }
}

@Composable
fun ChangePasswordSuccessDialog(profileViewModel: ProfileViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.update_pass),
        message = stringResource(R.string.update_pass_success_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { _, _ -> profileViewModel.updateChangePassResult(0) })
}

@Composable
fun ChangePasswordFailureDialog(profileViewModel: ProfileViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.update_pass_failed_title),
        message = stringResource(R.string.update_pass_failed_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { _, _ -> profileViewModel.updateChangePassResult(0) })
}

@Composable
fun PasswordsSameErrorDialog(profileViewModel: ProfileViewModel, mode: ColorMode) {
    CustomDialog(
        title = stringResource(R.string.invalid_pass_title),
        message = stringResource(R.string.invalid_pass_content),
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { _, _ -> profileViewModel.updateChangePassResult(0) })
}