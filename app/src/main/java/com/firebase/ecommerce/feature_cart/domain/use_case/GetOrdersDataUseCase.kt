package com.firebase.ecommerce.feature_cart.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetOrdersDataUseCase @Inject constructor(private val cartRepository: CartRepository) {
    fun getDataUseCase(): Flow<Resource<Any>> {
        return cartRepository.getOrdersData()
    }

}