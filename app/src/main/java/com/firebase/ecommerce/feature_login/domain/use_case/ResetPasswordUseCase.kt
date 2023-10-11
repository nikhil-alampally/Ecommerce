package com.firebase.ecommerce.feature_login.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ResetPasswordUseCase@Inject constructor(private val registrationRepository: RegistrationRepository) {
    fun resetPassword(email:String): Flow<Resource<AuthResult>> {
        return registrationRepository.initiatePasswordReset(email)
    }
}