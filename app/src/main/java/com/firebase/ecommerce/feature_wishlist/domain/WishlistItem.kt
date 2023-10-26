package com.firebase.ecommerce.feature_wishlist.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WishlistItem(
    val brand: String="",
    val category: String="",
    val description: String="",
    val discountPercentage: Double=0.00,
    val id: Int=0,
    val images: String="",
    val price: Int=0,
    val rating: Double=0.00,
    val stock: Int=0,
    val thumbnail: String="",
    val title: String="",
):Parcelable
