package com.firebase.ecommerce.feature_profile.data.model

import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel

data class ProfileResponseDto(
    val email: String? = null,
    val mobileNumber: String? = null,
    val gender: String? = null,
    val userName: String? = null,
    val image: String = "https://firebasestorage.googleapis.com/v0/b/ecommerce-b13bb.appspot.com/o/Profile%2Fprofile_image.jpg?alt=media&token=0b671803-1084-4bdc-8c36-206ecda5cab5"
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

