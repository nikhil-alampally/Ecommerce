package com.firebase.ecommerce.feature_cart.domain.use_case


import android.content.Context
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartItemDeleteUseCase @Inject constructor(private val cartRepository: CartRepository) {
    fun deleteCartItem(documentPath:String,context: Context): Flow<Resource<Any>> {
       return cartRepository.deleteCartItem(documentPath,context)
    }
}