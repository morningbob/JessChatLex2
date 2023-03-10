package com.bitpunchlab.android.jesschatlex2.userAccount

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.bitpunchlab.android.jesschatlex2.Login
import com.bitpunchlab.android.jesschatlex2.base.CustomCircularProgressBar
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

@Composable
fun LogoutScreen(navController: NavHostController,
                 mainViewModel: MainViewModel) {

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT
        }
        return ColorMode.DARK
    }

    val loginState by mainViewModel.isLoggedIn.collectAsState()
    var loadingAlpha by remember {
        mutableStateOf(1f)
    }

    mainViewModel.logoutUser()

    LaunchedEffect(key1 = loginState) {
        if (!loginState) {
            navController.navigate(Login.route)
            loadingAlpha = 0f
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        val mode = chooseMode()

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .alpha(loadingAlpha),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(JessChatLex.getColor(mode, Element.BACKGROUND)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                Text(
                    text = "Logging Out",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(bottom = 40.dp),
                    color = JessChatLex.getColor(mode, Element.FIELD_BORDER)
                )
                CustomCircularProgressBar()
            }

        }

    }
}