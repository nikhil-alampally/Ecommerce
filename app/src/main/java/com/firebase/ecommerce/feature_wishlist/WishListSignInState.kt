package com.firebase.ecommerce.feature_wishlist

import com.firebase.ecommerce.feature_wishlist.domain.WishlistItem

data class WishListSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<WishlistItem>?= null,
    val isError: String? = ""
)

