package com.firebase.ecommerce.feature_home.domain

import android.annotation.SuppressLint
import com.firebase.ecommerce.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataHomeScreenUseCase @Inject constructor(private val homeRepository: HomeRepository) {
     @SuppressLint("SuspiciousIndentation")
     suspend fun getData(): Flow<Resource<Any>> {
         return homeRepository.getDataFromFirebase()
    }

}