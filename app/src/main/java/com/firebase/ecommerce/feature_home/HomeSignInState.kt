package com.firebase.ecommerce.feature_home

import com.firebase.ecommerce.feature_home.domain.HomeData

data class HomeSignInState(
    val isLoading: Boolean = false,
    val isSuccess: HomeData?= null,
    val isError: String? = ""
)

data class DeleteSignInState(
    val isLoading: Boolean = false,
    val isSuccess: String?= null,
    val isError: String? = ""
)

