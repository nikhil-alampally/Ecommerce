package com.firebase.ecommerce.feature_products.domain.use_case


import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteFromWishlistUseCase @Inject constructor(private val productScreenRepository:ProductsRepository) {
    suspend fun deleteWishListItem(documentPath:String): Flow<Resource<Any>> {
       return productScreenRepository.deleteFromWishlist(documentPath)
    }
}