package com.firebase.ecommerce.navigation

sealed class  NavRoute(val route:String) {
    object RegisterScreen:NavRoute("register")
    object LoginScreen:NavRoute("login")
    object HomeScreen:NavRoute("home")
    object ItemScreen:NavRoute("items")
    object DetailsScreen:NavRoute("Details")
}