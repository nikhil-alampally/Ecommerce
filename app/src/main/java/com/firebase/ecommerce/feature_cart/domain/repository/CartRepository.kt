package com.firebase.ecommerce.feature_cart.domain.repository

import android.content.Context
import com.firebase.ecommerce.core.Resource
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getDataForCartItems(): Flow<Resource<Any>>
    fun deleteCartItem(documentPath:String,context: Context):Flow<Resource<Any>>

}