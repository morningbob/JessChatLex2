package com.bitpunchlab.android.jesschatlex2

interface Destinations {
    val route : String
    val icon : Int
}

object Login : Destinations {
    override val route: String = "Login"
    override val icon = 0
}

object CreateAccount : Destinations {
    override val route: String = "CreateAccount"
    override val icon = 0
}

object ForgotPassword : Destinations {
    override val route: String = "ForgotPassword"
    override val icon = 0
}

object Main : Destinations {
    override val route: String = "Main"
    override val icon: Int = R.mipmap.home
}

object Records : Destinations {
    override val route: String = "Records"
    override val icon: Int = R.mipmap.records
}

object Profile : Destinations {
    override val route: String = "Profile"
    override val icon: Int = R.mipmap.user
}

object Logout : Destinations {
    override val route: String = "Logout"
    override val icon : Int = R.mipmap.logout
}