package com.firebase.ecommerce.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.firebase.ecommerce.feature_login.presentation.screens.LoginScreen
import com.firebase.ecommerce.feature_login.presentation.screens.RegistrationScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.LoginScreen.route
    ) {
        composable(
            route = NavRoute.LoginScreen.route
        ) {
            LoginScreen(navigate = { navController.navigate(NavRoute.RegisterScreen.route)})
        }
        composable(route = NavRoute.RegisterScreen.route){
            RegistrationScreen({navController.navigate(NavRoute.LoginScreen.route)})
        }
    }
}