package com.firebase.ecommerce.feature_profile.domain.use_cases

import android.content.Context
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveUserDetailsUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun saveUserDetails(context: Context, profileModel: ProfileModel): Flow<Resource<Any>> {
        return profileRepository.saveUserDetailsInFireStore(context, profileModel)
    }
}