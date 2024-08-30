package com.example.front

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.em
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.front.screens.basic_screens.HomeScreen
import com.example.front.screens.basic_screens.JobsScreen
import com.example.front.screens.basic_screens.NetworkScreen
import com.example.front.screens.basic_screens.NotificationsScreen
import com.example.front.screens.basic_screens.UploadScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar() {
//initializing the default selected item
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }
    val navController = rememberNavController()

//scaffold to hold our bottom navigation Bar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                //getting the list of bottom navigation items for our data class
                BottomNavigationItem().bottomNavigationItems().forEachIndexed {index,navigationItem ->

                    //iterating all items with their respective indexes
                    NavigationBarItem(
                        selected = index == navigationSelectedItem,
                        alwaysShowLabel = false,
                        label = {
                            Text(navigationItem.label, fontSize = 2.5.em)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.label,
                            )
                        },


                        onClick = {
                            navigationSelectedItem = index
                            navController.navigate(navigationItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues))
        {
            composable(Screens.Home.route) { HomeScreen(navController) }
            composable(Screens.Upload.route) { UploadScreen(navController) }
            composable(Screens.Network.route) { NetworkScreen(navController) }
            composable(Screens.Jobs.route) { JobsScreen(navController) }
            composable(Screens.Notifications.route) { NotificationsScreen(navController) }
        }
    }
}