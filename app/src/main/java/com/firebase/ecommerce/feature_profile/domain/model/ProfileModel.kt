package com.firebase.ecommerce.feature_profile.domain.model

data class ProfileModel(
    var email: String? = null,
    var mobileNumber: String? = null,
    var gender: String? = null,
    var userName: String = "",
    val image: String = "",
)
