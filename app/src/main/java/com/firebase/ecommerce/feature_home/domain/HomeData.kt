package com.firebase.ecommerce.feature_home.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeData(
    val email:String?=null,
    val password:String?=null,
    var mobileNumber:String?=null,
    val gender:String?=null,
    var userName:String?=null,
    val image:String="https://firebasestorage.googleapis.com/v0/b/ecommerce-b13bb.appspot.com/o/Profile%2Fprofile_image.jpg?alt=media&token=0b671803-1084-4bdc-8c36-206ecda5cab5"
): Parcelable {
}