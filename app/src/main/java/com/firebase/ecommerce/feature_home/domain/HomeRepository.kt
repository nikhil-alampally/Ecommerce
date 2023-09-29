package com.firebase.ecommerce.feature_home.domain

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.data.Categories
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getDataFromFirebase():Flow<Resource<Any>>

}


