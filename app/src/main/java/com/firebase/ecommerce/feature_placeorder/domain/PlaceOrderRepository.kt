package com.firebase.ecommerce.feature_placeorder.domain

import android.content.Context
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_placeorder.data.AddAddress
import kotlinx.coroutines.flow.Flow

interface PlaceOrderRepository {
    fun addAddressToFirebase(addAddress:AddAddress): Flow<Resource<Any>>
    fun getAddressFromFirebase():Flow<Resource<Any>>
    fun deleteAddressFromFirebase(documentPath:String,context: Context):    Flow<Resource<Any>>

}