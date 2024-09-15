package com.example.front

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.em
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.front.activity.SearchViewModel
import com.example.front.screens.Subcomponents.DrawerContent
import com.example.front.screens.Subcomponents.TopBar
import com.example.front.screens.basic_screens.HomeScreen
import com.example.front.screens.basic_screens.JobsScreen
import com.example.front.screens.basic_screens.NetworkScreen
import com.example.front.screens.basic_screens.NotificationsScreen
import com.example.front.screens.basic_screens.UploadScreen
import com.example.front.ui.theme.FrontEndTheme
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BottomNavigationBar(navController: NavController, viewModel: SearchViewModel = viewModel()) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    FrontEndTheme {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    navController.navigate(route)
                }
            },
            content = {

                Scaffold(
                    topBar = { TopBar(drawerState = drawerState, scope = scope, onSearch = {
                        when (navController.currentDestination?.route) {
                            Screens.Home.route -> {

                                performSearch(context, it, "posts", viewModel)
                            }
                            else -> {
                                performSearch(context, it, "users", viewModel)
                            }
                        }
                    }) },
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
                        composable(Screens.Home.route) { HomeScreen() }
                        composable(Screens.Upload.route) { UploadScreen() }
                        composable(Screens.Network.route) { NetworkScreen(navController) }
                        composable(Screens.Jobs.route) { JobsScreen(navController) }
                        composable(Screens.Notifications.route) { NotificationsScreen(navController) }
                    }
                }
            }
        )
    }
}

fun performSearch(context: Context, query: String, searchEntity: String, vm: ViewModel) {
    // Search logic goes here, e.g., search in posts, users, etc.
    println("Searching for: $query in $searchEntity")


}
