package com.firebase.ecommerce.feature_profile.domain.model

data class ProfileModel(
    var email: String? = null,
    var mobileNumber: String? = null,
    var gender: String? = null,
    var userName: String = "",
    val image: String = "https://firebasestorage.googleapis.com/v0/b/ecommerce-b13bb.appspot.com/o/Profile%2Fprofile_image.jpg?alt=media&token=0b671803-1084-4bdc-8c36-206ecda5cab5",
)
