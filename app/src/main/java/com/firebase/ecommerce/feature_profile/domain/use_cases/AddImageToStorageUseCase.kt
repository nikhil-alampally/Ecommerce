package com.firebase.ecommerce.feature_profile.domain.use_cases

import android.net.Uri
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddImageToStorageUseCase @Inject constructor(private val profileRepository: ProfileRepository) {

    suspend operator fun invoke(uri: Uri) : Flow<Resource<Any>>{
       return profileRepository.addImageToFirebaseStorage(uri)
    }
}