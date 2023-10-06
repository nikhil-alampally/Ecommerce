package com.firebase.ecommerce.feature_login.domain.use_case

import android.util.Log
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoringGoogleSignInDataIntoFireStoreUseCase@Inject constructor(private val registrationRepository: RegistrationRepository) {
    fun storingGoogleSignInDataIntoFireStore(homeData: HomeDataDto): Flow<Resource<Any>> {
        return registrationRepository.addDetailsIntoFireStore(homeData)
    }
}