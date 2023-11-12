package com.firebase.ecommerce.feature_products.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
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
    var quantity: Int = 1
):Parcelable