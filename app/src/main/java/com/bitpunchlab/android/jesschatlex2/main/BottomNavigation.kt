package com.bitpunchlab.android.jesschatlex2.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bitpunchlab.android.jesschatlex2.*
import com.bitpunchlab.android.jesschatlex2.helpers.ColorMode
import com.bitpunchlab.android.jesschatlex2.helpers.Element
import com.bitpunchlab.android.jesschatlex2.ui.theme.JessChatLex

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val lightMode = !isSystemInDarkTheme()
    fun chooseMode() : ColorMode {
        if (lightMode) {
            return ColorMode.LIGHT
        }
        return ColorMode.DARK
    }

    val bottomItems = listOf<Destinations>(
        Main,
        Records,
        Profile,
        Logout
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinations = navBackStackEntry?.destination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            //.padding(70.dp)
    ) {
        val mode = chooseMode()

        BottomNavigation(
            backgroundColor = JessChatLex.getColor(mode, Element.BOTTOM_BACKGROUND),//JessChatLex.blueBackground,
            modifier = Modifier.height(70.dp),

        ) {
            bottomItems.forEach { item ->

                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.route,
                            modifier = Modifier.size(40.dp)
                        )
                    },
                    selectedContentColor = JessChatLex.getColor(mode, Element.BOTTOM_ICON),
                    unselectedContentColor = JessChatLex.getColor(mode, Element.BOTTOM_UNSELECTED),
                    selected = currentDestinations?.route == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { dest ->
                                popUpTo(dest) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

}