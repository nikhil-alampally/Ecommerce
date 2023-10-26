package com.firebase.ecommerce.feature_home.presentation.signInState

import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_home.domain.model.HomeData

data class HomeSignInState(
    val isLoading: Boolean = false,
    val isSuccess: HomeData?= null,
    val isError: String? = ""
)

