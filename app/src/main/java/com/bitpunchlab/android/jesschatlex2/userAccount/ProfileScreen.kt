package com.bitpunchlab.android.jesschatlex2.userAccount

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
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

    val config = LocalConfiguration.current

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
                    .padding(bottom = it.calculateBottomPadding())
                    //.verticalScroll(rememberScrollState()),
                //horizontalAlignment = Alignment.CenterHorizontally,
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
                        .padding(top = 0.dp, bottom = 0.dp),
                    ) {
                    TitleText(
                        title = stringResource(R.string.profile),
                        modifier = Modifier
                        .padding(
                            top = dimensionResource(id = R.dimen.header_title_padding),
                            bottom = dimensionResource(id = R.dimen.header_title_padding)))
                }

                // portrait
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                            //.background(JessChatLex.getColor(mode, Element.BACKGROUND)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GeneralText(
                            textString = stringResource(R.string.name),
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding),
                                    top = dimensionResource(id = R.dimen.bit_more_space),
                                    bottom = dimensionResource(id = R.dimen.general_space)
                                ),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                            textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT),
                        )
                        GeneralText(
                            textString = userName,
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(bottom = dimensionResource(id = R.dimen.general_space)),
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            textAlign = TextAlign.Center,
                            size = dimensionResource(id = R.dimen.normal_text_size).value.sp
                        )
                        GeneralText(
                            textString = stringResource(R.string.email),
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding),
                                    bottom = dimensionResource(id = R.dimen.general_space)
                                ),
                            textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp
                        )
                        GeneralText(
                            textString = userEmail,
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    bottom = dimensionResource(id = R.dimen.general_space)
                                ),
                            textAlign = TextAlign.Center,
                            textColor = JessChatLex.getColor(mode, Element.TEXT),
                            size = dimensionResource(id = R.dimen.normal_text_size).value.sp
                        )
                        GeneralText(
                            textString = stringResource(R.string.password),
                            modifier = Modifier
                                //.fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.general_space),
                                    start = dimensionResource(id = R.dimen.textfield_left_padding),
                                    end = dimensionResource(id = R.dimen.textfield_right_padding),
                                    bottom = dimensionResource(id = R.dimen.general_space)
                                ),
                            textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT),
                            size = dimensionResource(id = R.dimen.general_text_size).value.sp
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
                                    modifier = Modifier.padding(
                                        bottom = 2.dp,
                                        start = dimensionResource(id = R.dimen.textfield_left_padding),
                                        end = dimensionResource(id = R.dimen.textfield_right_padding),),
                                    call = { profileViewModel.updateCurrentPassword(it)} )
                                ErrorText(
                                    error = currentPassError,
                                    color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                    modifier = Modifier
                                        .padding(
                                            bottom = 4.dp,
                                            start = dimensionResource(id = R.dimen.error_left_right_padding),
                                            end = dimensionResource(id = R.dimen.error_left_right_padding)),
                                )
                                UserInputTextField(
                                    title = stringResource(R.string.new_pass),
                                    content = newPassword,
                                    textColor = JessChatLex.getColor(mode, Element.TEXT),
                                    textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                    fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                                    fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                                    hide = false,
                                    modifier = Modifier.padding(
                                        bottom = 2.dp,
                                        start = dimensionResource(id = R.dimen.textfield_left_padding),
                                        end = dimensionResource(id = R.dimen.textfield_right_padding),),
                                    call = { profileViewModel.updateNewPassword(it)} )
                                ErrorText(
                                    error = newPassError,
                                    color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                    modifier = Modifier
                                        .padding(
                                            bottom = 4.dp,
                                            start = dimensionResource(id = R.dimen.error_left_right_padding),
                                            end = dimensionResource(id = R.dimen.error_left_right_padding)),
                                )

                                UserInputTextField(
                                    title = stringResource(R.string.confirm_password),
                                    content = confirmPassword,
                                    textColor = JessChatLex.getColor(mode, Element.TEXT),
                                    textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                    fieldBackground = JessChatLex.getColor(mode, Element.FIELD_BACKGROUND),
                                    fieldBorder = JessChatLex.getColor(mode, Element.FIELD_BORDER),
                                    hide = false,
                                    modifier = Modifier.padding(
                                        bottom = 2.dp,
                                        start = dimensionResource(id = R.dimen.textfield_left_padding),
                                        end = dimensionResource(id = R.dimen.textfield_right_padding),),
                                    call = { profileViewModel.updateConfirmPassword(it) },
                                )
                                ErrorText(
                                    error = confirmPassError,
                                    color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                    modifier = Modifier
                                        .padding(
                                            bottom = 4.dp,
                                            start = dimensionResource(id = R.dimen.error_left_right_padding),
                                            end = dimensionResource(id = R.dimen.error_left_right_padding)),
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
                                        .fillMaxWidth()
                                        .padding(
                                            start = dimensionResource(id = R.dimen.app_button_left_padding),
                                            end = dimensionResource(id = R.dimen.app_button_right_padding),
                                            bottom = dimensionResource(id = R.dimen.more_space))
                                )
                            }
                        }
                        } // end of portrait column

                } else {
                    // landscape
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .verticalScroll(rememberScrollState()),
                            //.background(JessChatLex.getColor(mode, Element.BACKGROUND)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GeneralText(
                                textString = stringResource(R.string.name),
                                modifier = Modifier
                                    //.fillMaxWidth()
                                    .padding(
                                        start = dimensionResource(id = R.dimen.textfield_left_padding),
                                        end = dimensionResource(id = R.dimen.textfield_right_padding),
                                        top = dimensionResource(id = R.dimen.more_space),
                                        bottom = dimensionResource(id = R.dimen.general_space)
                                    ),
                                size = dimensionResource(id = R.dimen.general_text_size).value.sp,
                                textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT),
                            )
                            GeneralText(
                                textString = userName,
                                modifier = Modifier
                                    //.fillMaxWidth()
                                    .padding(bottom = dimensionResource(id = R.dimen.general_space)),
                                textColor = JessChatLex.getColor(mode, Element.TEXT),
                                textAlign = TextAlign.Center,
                                size = dimensionResource(id = R.dimen.general_text_size).value.sp
                            )
                            GeneralText(
                                textString = stringResource(R.string.email),
                                modifier = Modifier
                                    //.fillMaxWidth()
                                    .padding(
                                        start = dimensionResource(id = R.dimen.textfield_left_padding),
                                        end = dimensionResource(id = R.dimen.textfield_right_padding),
                                        bottom = dimensionResource(id = R.dimen.general_space)
                                    ),
                                textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT)
                            )
                            GeneralText(
                                textString = userEmail,
                                modifier = Modifier
                                    //.fillMaxWidth()
                                    .padding(
                                        top = dimensionResource(id = R.dimen.general_space),
                                        bottom = dimensionResource(id = R.dimen.general_space)
                                    ),
                                textAlign = TextAlign.Center,
                                textColor = JessChatLex.getColor(mode, Element.TEXT),
                            )

                        } // end of landscape first column
                        Column(
                            modifier = Modifier
                                //.fillMaxSize(),
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (!shouldChangePassword) {
                                GeneralText(
                                    textString = stringResource(R.string.password),
                                    modifier = Modifier
                                        //.fillMaxWidth()
                                        .padding(
                                            top = dimensionResource(id = R.dimen.more_space),
                                            //start = dimensionResource(id = R.dimen.textfield_right_padding),
                                            end = dimensionResource(id = R.dimen.textfield_left_padding),
                                            bottom = dimensionResource(id = R.dimen.general_space)
                                        ),
                                    textColor = JessChatLex.getColor(mode, Element.OTHER_TEXT)
                                )
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
                                        .padding(
                                            start = dimensionResource(id = R.dimen.error_left_right_padding),
                                            end = dimensionResource(id = R.dimen.error_left_right_padding)
                                        )
                                ) {
                                    UserInputTextField(
                                        title = stringResource(R.string.current_pass),
                                        content = currentPassword,
                                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                        fieldBackground = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BACKGROUND
                                        ),
                                        fieldBorder = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BORDER
                                        ),
                                        hide = false,
                                        modifier = Modifier
                                            .padding(
                                                top = dimensionResource(id = R.dimen.more_space),
                                                bottom = 2.dp,
                                                //start = dimensionResource(id = R.dimen.textfield_right_padding),
                                                end = dimensionResource(id = R.dimen.textfield_left_padding),
                                            )
                                            .fillMaxWidth(),
                                        call = { profileViewModel.updateCurrentPassword(it) })
                                    ErrorText(
                                        error = currentPassError,
                                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                        modifier = Modifier
                                            .padding(
                                                bottom = 4.dp,
                                                //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                                end = dimensionResource(id = R.dimen.error_left_right_padding)
                                            )
                                            .fillMaxWidth(),
                                    )
                                    UserInputTextField(
                                        title = stringResource(R.string.new_pass),
                                        content = newPassword,
                                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                        fieldBackground = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BACKGROUND
                                        ),
                                        fieldBorder = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BORDER
                                        ),
                                        hide = false,
                                        modifier = Modifier
                                            .padding(
                                            bottom = 2.dp,
                                            start = dimensionResource(id = R.dimen.textfield_right_padding),
                                            end = dimensionResource(id = R.dimen.textfield_left_padding),)
                                            .fillMaxWidth(),
                                        call = { profileViewModel.updateNewPassword(it) })
                                    ErrorText(
                                        error = newPassError,
                                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                        modifier = Modifier
                                            .padding(
                                                bottom = 4.dp,
                                                //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                                end = dimensionResource(id = R.dimen.error_left_right_padding)
                                            ).fillMaxWidth(),
                                    )

                                    UserInputTextField(
                                        title = stringResource(R.string.confirm_password),
                                        content = confirmPassword,
                                        textColor = JessChatLex.getColor(mode, Element.TEXT),
                                        textBorder = JessChatLex.getColor(mode, Element.BANNER),
                                        fieldBackground = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BACKGROUND
                                        ),
                                        fieldBorder = JessChatLex.getColor(
                                            mode,
                                            Element.FIELD_BORDER
                                        ),
                                        hide = false,
                                        modifier = Modifier
                                            .padding(
                                            bottom = 2.dp,
                                            start = dimensionResource(id = R.dimen.textfield_right_padding),
                                            end = dimensionResource(id = R.dimen.textfield_left_padding)
                                        )
                                            .fillMaxWidth(),
                                        call = { profileViewModel.updateConfirmPassword(it) },

                                    )
                                    ErrorText(
                                        error = confirmPassError,
                                        color = JessChatLex.getColor(mode, Element.ERROR_TEXT),
                                        modifier = Modifier
                                            .padding(
                                                bottom = 4.dp,
                                                //start = dimensionResource(id = R.dimen.error_left_right_padding),
                                                end = dimensionResource(id = R.dimen.error_left_right_padding)
                                            )
                                            .fillMaxWidth(),
                                    )

                                    AppButton(
                                        title = stringResource(R.string.send),
                                        onClick = {
                                            profileViewModel.checkAndUpdatePassword(
                                                currentPassword,
                                                newPassword
                                            )
                                        },
                                        shouldEnable = readyChange,
                                        buttonColor = JessChatLex.getColor(
                                            mode,
                                            Element.BUTTON_COLOR
                                        ),
                                        buttonBackground = JessChatLex.getColor(
                                            mode,
                                            Element.BUTTON_BACKGROUND
                                        ),
                                        buttonBorder = JessChatLex.getColor(
                                            mode,
                                            Element.BUTTON_BORDER
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                //start = dimensionResource(id = R.dimen.app_button_left_padding),
                                                end = dimensionResource(id = R.dimen.textfield_left_padding),
                                                bottom = dimensionResource(id = R.dimen.more_space)
                                            ),
                                    )
                                }
                            }
                        } // end of landscape second column
                    } // end of Row
                } // end of landscape if
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