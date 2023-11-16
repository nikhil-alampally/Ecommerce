package com.firebase.ecommerce.feature_cart.presentation.signInState

import com.firebase.ecommerce.feature_cart.data.repository.OrderDetails
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_placeorder.data.AddAddress

data class CartSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<CartItem>?= null,
    val isError: String? = ""
)
data class AddressSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<AddAddress>?= null,
    val isError: String? = ""
)
data class OrderSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<OrderDetails>?= null,
    val isError: String? = ""
)