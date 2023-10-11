package com.firebase.ecommerce.feature_profile.domain.repository

import android.content.Context
import android.net.Uri
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun addImageToFirebaseStorage(imageUri: Uri): Flow<Resource<Any>>

    suspend fun getImageUrlFromFirestore(): Flow<Resource<String>>

    suspend fun saveUserDetailsInFireStore(profileModel: ProfileModel): Flow<Resource<Any>>

}