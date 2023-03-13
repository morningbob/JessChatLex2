package com.bitpunchlab.android.jesschatlex2.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.Login
import com.bitpunchlab.android.jesschatlex2.R
import com.bitpunchlab.android.jesschatlex2.Records
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.base.CustomCircularProgressBar
import com.bitpunchlab.android.jesschatlex2.base.SendIcon
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex2.models.Message
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex2.userAccount.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController,
    mainViewModel: MainViewModel,) {
    //userInfoViewModel: UserInfoViewModel = viewModel(LocalContext.current as ComponentActivity)) {

    val loadingAlpha by mainViewModel.loadingAlpha.collectAsState()

    val loginState by MobileClient.isLoggedIn.collectAsState()
    var input by remember { mutableStateOf("") }

    var shouldNavigateRecords by remember { mutableStateOf(false) }

    val lexError by MobileClient.lexError.collectAsState()

    LaunchedEffect(key1 = loginState) {
        if (loginState == false) {
            navController.navigate(Login.route) {
                popUpTo(navController.graph.id) {
                    inclusive = false
                }
            }
        }
    }

    LaunchedEffect(key1 = shouldNavigateRecords) {
        if (shouldNavigateRecords) {
            navController.navigate(Records.route)
        }
    }
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT
        }
        return ColorMode.DARK
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) {
        val themeMode = chooseMode()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) },

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(JessChatLex.getColor(themeMode, Element.BACKGROUND)),
                horizontalAlignment = Alignment.CenterHorizontally,


            ) {
                // display error message here, if there is interaction error
                lexError?.let {
                    Text(
                        text = stringResource(R.string.lex_error),
                        modifier = Modifier
                            .background(Color.Yellow)
                            .padding(5.dp),
                        color = Color.Red
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .fillMaxWidth()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        mainViewModel.currentMessageList.forEach { message : Message ->
                            val textColor = if (message.whoSaid == WhoSaid.Lex) {
                                JessChatLex.getColor(themeMode, Element.BOT_MESSAGE)
                            } else {
                                JessChatLex.getColor(themeMode, Element.USER_MESSAGE)
                            }
                            Text(
                                text = message.message,
                                modifier = Modifier.padding(8.dp),
                                color = textColor,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
                // this is the color of the cursor handle
                val customTextSelectionColors = TextSelectionColors(
                    handleColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                    backgroundColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                )

                CompositionLocalProvider(
                    LocalTextSelectionColors provides customTextSelectionColors,
                ) {

                    OutlinedTextField(
                        value = input,
                        onValueChange = { newInput: String ->
                            input = newInput
                        },
                        trailingIcon = {
                            if (input != "") {
                                SendIcon(color = JessChatLex.getColor(themeMode, Element.SEND_ICON)) {
                                    mainViewModel.sendMessage(input)
                                    input = ""
                                }
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(color = JessChatLex.getColor(themeMode, Element.FIELD_BORDER)),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                           
                            focusedBorderColor = JessChatLex.getColor(
                                themeMode,
                                Element.FIELD_BORDER
                            ),
                            unfocusedBorderColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            placeholderColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                            cursorColor = JessChatLex.getColor(themeMode, Element.FIELD_BORDER)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(
                            text = stringResource(R.string.type_message),
                            color = JessChatLex.getColor(themeMode, Element.FIELD_BORDER),
                        ) }
                    )
                }

            }
            // progress bar
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
}



