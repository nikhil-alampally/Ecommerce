package com.firebase.ecommerce.feature_profile.data.model

import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel

data class ProfileResponseDto(
    val email: String? = null,
    val mobileNumber: String? = null,
    val gender: String? = null,
    val userName: String? = null,
    val image: String = ""
)

fun toProfileModel(profileModel: ProfileModel): ProfileModel {
    return ProfileModel(
        profileModel.email,
        profileModel.mobileNumber,
        profileModel.gender,
        profileModel.userName,
      //  profileModel.image,
    )
}

