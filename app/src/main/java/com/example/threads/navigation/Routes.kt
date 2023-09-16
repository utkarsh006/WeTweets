package com.example.threads.navigation

sealed class Routes(val routes: String){
    object Home: Routes("home")
    object Notification: Routes("notification")
    object Profile: Routes("profile")
    object Search: Routes("search")
    object Splash: Routes("splash")
    object AddThread: Routes("addThread")
    object BottomNav: Routes("bottomNav")


}
