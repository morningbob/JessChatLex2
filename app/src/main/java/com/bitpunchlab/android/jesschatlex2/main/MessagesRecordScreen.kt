package com.bitpunchlab.android.jesschatlex2.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.helpers.WhoSaid
import com.bitpunchlab.android.jesschatlex2.models.Message
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex
import com.bitpunchlab.android.jesschatlex2.userAccount.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MessagesRecordScreen(navController: NavHostController,
                         mainViewModel: MainViewModel
) {
    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT
        }
        return ColorMode.DARK
    }

    LaunchedEffect(Unit) {
        mainViewModel.getAllMessages()
    }

    val allMessages by mainViewModel.allMessages.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val mode = chooseMode()

        Scaffold(
            bottomBar = { BottomNavigationBar(navController
            ) },
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(JessChatLex.getColor(mode, Element.BACKGROUND))) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 30.dp, end = 30.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                        allMessages.forEach { message : Message  ->
                            val textColor = if (message.whoSaid == WhoSaid.Lex) {
                                JessChatLex.getColor(mode, Element.BOT_MESSAGE)
                            } else {
                                JessChatLex.getColor(mode, Element.USER_MESSAGE)
                            }
                            item {
                                Text(
                                    text = message.message,
                                    color = textColor,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier
                                        .padding(
                                            top = 0.dp,
                                            bottom = 8.dp,
                                            start = 8.dp,
                                            end = 8.dp
                                        ),
                                    fontSize = 18.sp
                                )
                            }
                        }
                }
            }
        }
    }
}