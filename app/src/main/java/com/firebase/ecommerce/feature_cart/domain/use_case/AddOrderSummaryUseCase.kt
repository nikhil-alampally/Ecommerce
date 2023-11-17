package com.firebase.ecommerce.feature_cart.domain.use_case


import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.data.repository.OrderDetails
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddOrderSummaryUseCase @Inject constructor(private val cartRepository: CartRepository) {
    fun addOrderDetails(orderDetails: OrderDetails): Flow<Resource<Any>> {
        return cartRepository.addOrderSummary(orderDetails)
    }
}