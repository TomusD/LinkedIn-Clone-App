package com.example.front

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Dialog
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
import com.example.front.screens.Subcomponents.modals.SearchPostsModal
import com.example.front.screens.Subcomponents.modals.SearchUsersModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController, viewModel: SearchViewModel = viewModel(), onChatClick: () -> Unit) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    var queryText by remember { mutableStateOf("") }
    var showPosts by remember { mutableStateOf(false) }
    var showUsers by remember { mutableStateOf(false) }

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
                    topBar = { TopBar(navController, drawerState = drawerState, scope = scope, onSearch = { it, it1 ->
                        when (navController.currentDestination?.route) {
                            Screens.Home.route -> {
                                showPosts = it1
                                queryText = it
                            }
                            else -> {
                                showUsers = true
                                queryText = it
                            }
                        }
                    },
                        onChatClick = onChatClick
                        ) },
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
                        if (showUsers) {
                            BasicAlertDialog(onDismissRequest = {}) {
                                SearchUsersModal(
                                    query = queryText,
                                    onDismiss = {showUsers = false},
                                )
                            }
                        }

                        if (showPosts) {
                            BasicAlertDialog(onDismissRequest = {}) {
                                SearchPostsModal(
                                    query = queryText,
                                    onDismiss = {showPosts = false},
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

