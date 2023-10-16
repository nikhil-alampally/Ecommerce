package com.firebase.ecommerce.feature_profile.domain.model

data class ProfileModel(
    var email: String? = null,
    var mobileNumber: String? = null,
    var gender: String? = null,
    var userName: String = "",
    val image: String = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250",
)
