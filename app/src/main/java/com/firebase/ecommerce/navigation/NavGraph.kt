package com.firebase.ecommerce.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.feature_home.presentation.screen.BottomNavigation
import com.firebase.ecommerce.feature_products.presentation.screens.DetailScreen
import com.firebase.ecommerce.feature_home.domain.model.HomeData
import com.firebase.ecommerce.feature_login.presentation.screens.LoginScreen
import com.firebase.ecommerce.feature_login.presentation.screens.RegistrationScreen
import com.firebase.ecommerce.feature_placeorder.presentaion.AddAddress
import com.firebase.ecommerce.feature_placeorder.presentaion.MainScreen
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.presentation.screens.ProductScreen
import com.firebase.ecommerce.feature_profile.presentation.screens.ProfileScreen
import com.firebase.ecommerce.feature_profile.presentation.screens.getData
import com.firebase.ecommerce.feature_wishlist.presentation.WishlistScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

val tweenSpec =
    tween<IntOffset>(durationMillis = 500, easing = CubicBezierEasing(0.08f, 0.93f, 0.68f, 1.27f))

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
        composable(NavRoute.HomeScreen.route){
            BottomNavigation(navController = navController)

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
            navController.getData<String>(Constants.apiParameterKey).let{
                if(it!=null){
                   ProductScreen(category = it, navController = navController, onItemClick = {
                       navController.navigate(NavRoute.DetailsScreen.route)
                   })
                }
            }
        }
        composable(NavRoute.DetailsScreen.route){
            navController.getData<Product>(Constants.ITEMSLIST).let{
                if (it != null) {
                    DetailScreen(it,navController)
                }
            }
        }
        composable(NavRoute.ProfileScreen.route) {
            val context = LocalContext.current
            val profileData = navController.getData<HomeData>("profile_details")
            if (profileData != null) {
                ProfileScreen(navController = navController, profileData = profileData, context = context )
            }
        }
        composable(NavRoute.PlaceOrder.route){
          val currentStep=  navController.getData<Int>("currentStep")
            MainScreen(
                    addAddress = { AddAddress(navController = navController) },
                    placeOrder = {  },
                orderSummary = {},
                currentStepScreen = if(currentStep!=null) currentStep else 1



                )

        }
        composable(NavRoute.WishlistScreen.route){
            WishlistScreen(navHostController = navController)
        }
    }
}

fun <T> NavHostController.getData(key: String): T? {
    return previousBackStackEntry?.savedStateHandle?.get<T>(key)

}