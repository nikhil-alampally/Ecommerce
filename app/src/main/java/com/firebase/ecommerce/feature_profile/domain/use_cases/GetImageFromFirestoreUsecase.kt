package com.firebase.ecommerce.feature_profile.domain.use_cases

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageFromFirestoreUsecase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Flow<Resource<String>> {
        return profileRepository.getImageUrlFromFirestore()
    }

}