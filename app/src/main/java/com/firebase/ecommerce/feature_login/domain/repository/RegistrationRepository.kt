package com.firebase.ecommerce.feature_login.domain.repository

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import kotlinx.coroutines.flow.Flow

interface RegistrationRepository {

    suspend fun authenticateEmailAndPassword(registrationDetails: RegistrationDetails): Flow<Resource<Any>>
}