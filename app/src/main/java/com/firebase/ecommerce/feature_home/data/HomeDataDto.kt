package com.firebase.ecommerce.feature_home.data

import android.os.Parcelable
import com.firebase.ecommerce.feature_home.domain.HomeData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeDataDto (
    val email:String?=null,
    val password:String?=null,
    val mobileNumber:String?=null,
    val gender:String?=null,
    val userName:String?=null,
    val image:String="https://firebasestorage.googleapis.com/v0/b/ecommerce-b13bb.appspot.com/o/Profile%2Fprofile_image.jpg?alt=media&token=18de6744-6e35-4af7-9319-4c19abf47918"

):Parcelable

