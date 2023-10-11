package com.firebase.ecommerce.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import com.firebase.ecommerce.ItemScreen
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_home.presentation.HomeScreenPreview
import com.firebase.ecommerce.feature_login.presentation.screens.LoginScreen
import com.firebase.ecommerce.feature_login.presentation.screens.RegistrationScreen
import com.firebase.ecommerce.feature_profile.presentation.ProfileScreen
import com.firebase.ecommerce.feature_profile.presentation.getData
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

val tweenSpec =
    tween<IntOffset>(durationMillis = 1000, easing = CubicBezierEasing(0.08f, 0.93f, 0.68f, 1.27f))

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController, startDestination = NavRoute.LoginScreen.route
    ) {

        composable(NavRoute.RegisterScreen.route) {
            RegistrationScreen(navigateToLogin = {
                navController.navigate(NavRoute.LoginScreen.route)
            })
        }
        composable(NavRoute.LoginScreen.route) {
            LoginScreen(navigate = {
                navController.navigate(NavRoute.RegisterScreen.route)
            }, navigateToHomeScreen = {
                navController.navigate(NavRoute.HomeScreen.route)
            })
        }
        composable(NavRoute.HomeScreen.route) {
            HomeScreenPreview(navController)
        }
        composable(NavRoute.ItemScreen.route, enterTransition = {
            initialState
            slideInVertically(initialOffsetY = { 1000 }, animationSpec = tweenSpec)
        },
            exitTransition = {
                targetState
                slideOutVertically(targetOffsetY = { -3000 }, animationSpec = tweenSpec)
            },
            popEnterTransition = {
                initialState
                slideInVertically(initialOffsetY = { -2000 }, animationSpec = tweenSpec)
            },
            popExitTransition = {
                targetState
                slideOutVertically(targetOffsetY = { 3000 }, animationSpec = tweenSpec)
            }) {
            ItemScreen()
        }
        composable(NavRoute.ProfileScreen.route) {
            val context = LocalContext.current
            val profileData = navController.getData<HomeData>("profile_details")
            if (profileData != null) {
                ProfileScreen(navController = navController, profileData = profileData )
            }
        }

    }
}

