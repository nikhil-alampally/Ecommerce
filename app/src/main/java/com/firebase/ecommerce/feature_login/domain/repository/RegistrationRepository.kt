package com.firebase.ecommerce.feature_login.domain.repository

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface RegistrationRepository {

    suspend fun authenticateEmailAndPassword(registrationDetails: RegistrationDetails): Flow<Resource<Any>>

    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
}