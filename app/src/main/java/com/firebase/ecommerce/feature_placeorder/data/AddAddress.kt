package com.firebase.ecommerce.feature_placeorder.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AddAddress(
    var houseNumber: String = "",
    val area: String = "",
    val state: String = "",
    val pincode: String = "",
    var isSelected: Boolean = false,
    var id: String = "",
    val addressType: String = "",
) : Parcelable {
}