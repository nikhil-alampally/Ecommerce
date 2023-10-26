package com.firebase.ecommerce.feature_cart.presentation.signInState

data class DeleteSignInState(
    val isLoading: Boolean = false,
    val isSuccess: String?= null,
    val isError: String? = ""
)