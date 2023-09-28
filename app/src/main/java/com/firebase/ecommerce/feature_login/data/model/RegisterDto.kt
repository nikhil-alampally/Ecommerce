package com.firebase.ecommerce.feature_login.data.model

import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails

data class RegisterDto(
    val email: String,
    val password: String,
    val mobileNumber: String,
    val gender: String,
    val userName:String
)
fun toDomain(registrationDetails: RegistrationDetails): RegisterDto {
    return RegisterDto(
        email = registrationDetails.email,
        password = registrationDetails.password,
        mobileNumber = registrationDetails.mobileNumber,
        gender = registrationDetails.gender,
        userName = registrationDetails.userName
    )
}
