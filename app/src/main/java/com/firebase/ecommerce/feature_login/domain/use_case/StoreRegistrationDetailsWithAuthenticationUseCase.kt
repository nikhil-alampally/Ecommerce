package com.firebase.ecommerce.feature_login.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoreRegistrationDetailsWithAuthenticationUseCase @Inject constructor(private val registrationRepository: RegistrationRepository){
    suspend fun storeRegistrationDetailsWithAuthentication(registrationDetails: RegistrationDetails): Flow<Resource<Any>> {
     return  registrationRepository.authenticateEmailAndPassword(registrationDetails=registrationDetails)
    }
}