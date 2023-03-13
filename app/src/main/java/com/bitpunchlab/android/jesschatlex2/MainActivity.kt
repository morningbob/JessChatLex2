package com.bitpunchlab.android.jesschatlex2

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLexTheme
import androidx.navigation.compose.rememberNavController
import com.bitpunchlab.android.jesschatlex2.awsClient.MobileClient
import com.bitpunchlab.android.jesschatlex2.main.MainScreen
import com.bitpunchlab.android.jesschatlex2.main.MessagesRecordScreen
import com.bitpunchlab.android.jesschatlex2.userAccount.*

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileClient.initializeMobileClient(applicationContext)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            JessChatLexTheme {
                // A surface container using the 'background' color from the theme
                JessNavigation(application)
            }
        }
    }
}

@Composable
fun JessNavigation(application: Application) {
    val navController = rememberNavController()
    val mainViewModel : MainViewModel = viewModel(factory = MainViewModelFactory(application))

        NavHost(navController = navController, startDestination = Login.route) {
            composable(Login.route) {
                LoginScreen(navController, mainViewModel)
            }
            composable(CreateAccount.route) {
                CreateAccountScreen(navController, mainViewModel)
            }
            composable(ForgotPassword.route) {
                ForgotPasswordScreen(navController)
            }
            composable(Main.route) {
                MainScreen(navController, mainViewModel)
            }
            composable(Records.route) {
                MessagesRecordScreen(navController, mainViewModel)
            }
            composable(Profile.route) {
                ProfileScreen(navController, mainViewModel)
            }
            composable(Logout.route) {
                LogoutScreen(navController, mainViewModel)
            }

        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JessChatLexTheme {

    }
}

