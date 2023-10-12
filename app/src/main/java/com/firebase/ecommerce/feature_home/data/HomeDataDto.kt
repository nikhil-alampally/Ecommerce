package com.firebase.ecommerce.feature_home.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeDataDto (
    val email:String?=null,
    val password:String?=null,
    val mobileNumber:String?=null,
    val gender:String?=null,
    val userName:String?=null,
    val image:String=""

):Parcelable

