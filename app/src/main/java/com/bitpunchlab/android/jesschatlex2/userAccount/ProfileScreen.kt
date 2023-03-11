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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.Login
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
            //navController.popBackStack()
            navController.navigate(Login.route) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
            }
        }
    }

    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val mode = chooseMode()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController//, paddingValues = paddingValues
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
                    )//JessChatLex.lightPurpleBackground)
                    .verticalScroll(rememberScrollState()),
                //verticalArrangement = Arrangement.Center,
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
                        )//JessChatLex.purpleBackground)
                        .padding(top = 20.dp, bottom = 20.dp),

                    ) {
                    TitleText(title = "Profile", paddingTop = 100, paddingBottom = 100)
                }
                Column(
                    modifier = Modifier
                        .background(JessChatLex.getColor(mode, Element.BACKGROUND)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GeneralText(
                        textString = "Name",
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
                        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
                        textAlign = TextAlign.Center,
                        size = 18.sp
                    )
                    GeneralText(
                        textString = "Email",
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
                        textString = "Password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, bottom = 5.dp),
                        textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT)
                    )
                    if (!shouldChangePassword) {
                        Text(
                            text = "Change Password",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable(
                                    enabled = true,
                                    onClick = {
                                        profileViewModel.updateShouldChangePassword(true)
                                    }),
                            color = JessChatLex.getColor(mode, Element.CLICKABLE),//JessChatLex.greenBackground
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp)
                        ) {
                            UserInputTextField(
                                title = "Current Password",
                                content = currentPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.purpleText,
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
                                title = "New Password",
                                content = newPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.purpleText,
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
                                title = "Confirm Password",
                                content = confirmPassword,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
                                textBorder = JessChatLex.getColor(mode, Element.BANNER),//JessChatLex.purpleText,
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
                                title = "Send",
                                onClick = {
                                    //if (userEmail.isNotEmpty()) {
                                    profileViewModel.updatePassword(currentPassword, newPassword)
                                    //}
                                },
                                shouldEnable = readyChange,
                                buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.purpleBackground,
                                buttonBackground = JessChatLex.getColor(mode, Element.BUTTON_BACKGROUND),//JessChatLex.lightPurpleBackground,
                                buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
                                modifier = Modifier
                                //.padding(bottom = 100.dp)
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

                    // show name, email and change password button
                }
                }

            if (changePassResult == 1) {
                ChangePasswordSuccessDialog(profileViewModel = profileViewModel, mode = mode)
            } else if (changePassResult == 2) {
                ChangePasswordFailureDialog(profileViewModel = profileViewModel, mode = mode)
            } // 0 - no dialog
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
        title = "Updated Password",
        message = "Your password is updated.",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightPurpleBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.purpleBackground,
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { _, _ -> profileViewModel.updateChangePassResult(0) })
}

@Composable
fun ChangePasswordFailureDialog(profileViewModel: ProfileViewModel, mode: ColorMode) {
    CustomDialog(
        title = "Update Password Failure",
        message = "We couldn't update your password.  Please make sure you have wifi and try again.  If the problem persists, please contact admin@jessbitcom.pro",
        backgroundColor = JessChatLex.getColor(mode, Element.BACKGROUND),//JessChatLex.lightPurpleBackground,
        buttonColor = JessChatLex.getColor(mode, Element.BUTTON_COLOR),//JessChatLex.purpleBackground,
        buttonBorder = JessChatLex.getColor(mode, Element.BUTTON_BORDER),
        textColor = JessChatLex.getColor(mode, Element.TEXT),//JessChatLex.purpleText,
        onDismiss = { profileViewModel.updateChangePassResult(0) },
        okOnClick = { _, _ -> profileViewModel.updateChangePassResult(0) })
}