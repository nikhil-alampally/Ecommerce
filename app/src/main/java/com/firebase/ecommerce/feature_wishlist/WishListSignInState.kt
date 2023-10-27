package com.firebase.ecommerce.feature_wishlist

import com.firebase.ecommerce.feature_wishlist.domain.WishlistItemModel

data class WishListSignInState(
    val isLoading: Boolean = false,
    val isSuccess: ArrayList<WishlistItemModel>?= null,
    val isError: String? = ""
)

data class DeleteSignInState(
    val isLoading: Boolean = false,
    val isSuccess: String?= null,
    val isError: String? = ""
)