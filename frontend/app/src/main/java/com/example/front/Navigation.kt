package com.example.front

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.front.screens.pre_auth_screens.SignInScreen
import com.example.front.screens.pre_auth_screens.SignUpScreen
import com.example.front.screens.pre_auth_screens.WelcomeScreen

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
                SignInScreen(navController)
            }
            composable(route = PreAuthScreens.Signup.route) {
                SignUpScreen(navController)
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