package com.example.front

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.front.screens.pre_auth_screens.SignInScreen
import com.example.front.screens.pre_auth_screens.SignUpScreen
import com.example.front.screens.pre_auth_screens.WelcomeScreen
import com.example.front.screens.user.ChatScreen
import com.example.front.screens.user.PersonalChatScreen

class SampleViewModel : ViewModel() {}


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = PreAuthScreens.Welcome.route) {
        composable(route = PreAuthScreens.Welcome.route) {
            WelcomeScreen(navController)
        }
        navigation(startDestination = "welcome_route", route = "auth") {
            composable(route = PreAuthScreens.Signin.route) {
                SignInScreen(navController,
                    onLoginSuccess ={
                        navController.navigate("main") {
                            popUpTo("auth") {inclusive=true}
                        }
                    }
                )
            }
            composable(route = PreAuthScreens.Signup.route) {
                SignUpScreen(navController)
            }
        }

        navigation(startDestination = "home_route", route = "main") {
            composable(Screens.Home.route) {
                BottomNavigationBar(navController = navController, onChatClick = {
                        navController.navigate("chat") {
                            popUpTo("main") { inclusive = true }
                    }
                })
            }
        }

        navigation(startDestination = "chat_route", route = "chat") {
            composable(ChatScreens.Chat.route) {
                ChatScreen(navController = navController)
            }
            composable("personal_chat_route/{userId}",
                arguments = listOf(navArgument("userId") {
                    type = NavType.IntType
            }))
            { backStackEntry ->
                val userId = backStackEntry.arguments!!.getInt("userId")
                PersonalChatScreen(navController = navController, userId = userId)
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}