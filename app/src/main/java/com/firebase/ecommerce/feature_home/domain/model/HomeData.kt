package com.firebase.ecommerce.feature_home.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeData(
    val email:String?=null,
    val password:String?=null,
    var mobileNumber:String?=null,
    val gender:String?=null,
    var userName:String?=null,
    val image:String="https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250"): Parcelable {
}