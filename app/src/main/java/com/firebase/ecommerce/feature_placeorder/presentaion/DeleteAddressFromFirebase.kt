package com.firebase.ecommerce.feature_placeorder.presentaion

import android.content.Context
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_placeorder.domain.PlaceOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAddressFromFirebase @Inject constructor(val placeOrderRepository: PlaceOrderRepository) {
    fun deleteAddressFromFirebase(documentPath:String,context:Context): Flow<Resource<Any>> {
        return placeOrderRepository.deleteAddressFromFirebase(documentPath, context)

    }
}