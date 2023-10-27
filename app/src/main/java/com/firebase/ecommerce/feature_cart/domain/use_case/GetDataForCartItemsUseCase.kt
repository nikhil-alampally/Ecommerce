package com.firebase.ecommerce.feature_cart.domain.use_case

import android.annotation.SuppressLint
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataForCartItemsUseCase @Inject constructor(private val cartRepository: CartRepository) {
    @SuppressLint("SuspiciousIndentation")
     fun getData(): Flow<Resource<Any>> {
        return cartRepository.getDataForCartItems()
    }

}