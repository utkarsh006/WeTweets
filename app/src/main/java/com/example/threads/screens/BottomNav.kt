package com.example.threads.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.threads.model.BottomNavItems
import com.example.threads.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(navController: NavHostController) {
    val _navController = rememberNavController()

    Scaffold(bottomBar = { BottomBarUI(_navController) }) { innerPadding ->

        NavHost(
            navController = _navController,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ){
            //defining the route, konse route pe konsa screen aayega
            composable(route = Routes.Home.routes){
                Home()
            }

            composable(route = Routes.Notification.routes){
                Notifications()
            }

            composable(route = Routes.Profile.routes){
                Profile(navController)
            }

            composable(route = Routes.Search.routes){
                Search()
            }

            composable(route = Routes.AddThread.routes){
                AddThreads()
            }
        }

    }

}

@Composable
fun BottomBarUI(_navController: NavHostController) {

    //using destinations, bcz we wanna know which item is in back of the stack
    val backStackEntry = _navController.currentBackStackEntryAsState()

    //Make List of items which you wanna show in bottom bar
    val list = listOf(
        BottomNavItems(
            "Home",
            Routes.Home.routes,
            Icons.Rounded.Home
        ),

        BottomNavItems(
            "Search",
            Routes.Search.routes,
            Icons.Rounded.Search
        ),

        BottomNavItems(
            "Add Threads",
            Routes.AddThread.routes,
            Icons.Rounded.Add
        ),

        BottomNavItems(
            "Notifications",
            Routes.Notification.routes,
            Icons.Rounded.Notifications
        ),

        BottomNavItems(
            "Profile",
            Routes.Profile.routes,
            Icons.Rounded.Person
        )
    )

    BottomAppBar {
        list.forEach{
            val selected = it.route == backStackEntry?.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    _navController.navigate(it.route) {
                        popUpTo(_navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) }
            )
        }
    }
}