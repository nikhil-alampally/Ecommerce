package com.firebase.ecommerce.feature_profile.domain.use_cases

import android.net.Uri
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddUrlToFirestoreUsecase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(downloadUri: Uri):Flow<Resource<Boolean>>{
       return  profileRepository.addImageUrlToFirestore(downloadUri)
    }
}