package com.firebase.ecommerce.feature_login.presentation

data class SignInState(
        val isLoading: Boolean = false,
        val isSuccess: String? = "",
        val isError: String? = ""
    )
