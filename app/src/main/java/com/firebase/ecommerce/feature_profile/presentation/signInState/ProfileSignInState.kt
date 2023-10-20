package com.firebase.ecommerce.feature_profile.presentation.signInState

data class ProfileSignInState(
    val isLoading: Boolean = false,
    val isSuccess: String? = null,
    val isError: String? = ""
)