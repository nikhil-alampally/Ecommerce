package com.firebase.ecommerce.feature_placeorder.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_placeorder.data.AddAddress
import com.firebase.ecommerce.feature_placeorder.domain.repository.PlaceOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(private val placeOrderRepository: PlaceOrderRepository) {
    fun addAddress(addAddress: AddAddress): Flow<Resource<Any>> {
        return placeOrderRepository.addAddressToFirebase(addAddress = addAddress)
    }
}