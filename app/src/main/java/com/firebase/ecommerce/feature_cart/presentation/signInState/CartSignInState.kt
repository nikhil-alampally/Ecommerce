package com.firebase.ecommerce.feature_cart.presentation.signInState

import com.firebase.ecommerce.feature_cart.domain.model.CartItem

data class CartSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<CartItem>?= null,
    val isError: String? = ""
)