package com.firebase.ecommerce.navigation

sealed class NavRoute(val route: String){
    object LoginScreen : NavRoute("login_screen")
    object RegisterScreen : NavRoute("register_screen")
}