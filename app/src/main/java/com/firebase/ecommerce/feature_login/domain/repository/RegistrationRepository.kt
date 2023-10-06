package com.firebase.ecommerce.feature_login.domain.repository

import android.content.Context
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface RegistrationRepository {

    suspend fun authenticateEmailAndPassword(registrationDetails: RegistrationDetails): Flow<Resource<Any>>
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun signInWithGoogle(credential: AuthCredential): Flow<Resource<AuthResult>>
    fun addDetailsIntoFireStore(homeData: HomeDataDto):Flow<Resource<Any>>
}