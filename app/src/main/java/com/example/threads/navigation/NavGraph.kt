package com.example.threads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threads.screens.AddThreads
import com.example.threads.screens.Home
import com.example.threads.screens.Notifications
import com.example.threads.screens.Profile
import com.example.threads.screens.Search
import com.example.threads.screens.Splash

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.Splash.routes){

        composable(Routes.Splash.routes){
            Splash()
        }

        composable(Routes.Home.routes){
           Home()
        }

        composable(Routes.Notification.routes){
            Notifications()
        }

        composable(Routes.Profile.routes){
            Profile()
        }

        composable(Routes.Search.routes){
            Search()
        }

        composable(Routes.AddThread.routes){
            AddThreads()
        }
    }
}