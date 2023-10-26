package com.firebase.ecommerce.feature_products.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToWishListUseCase @Inject constructor(private val productsRepository: ProductsRepository) {
    suspend fun addToWIshList(product: Product): Flow<Resource<Any>>
    {
        return productsRepository.addToWishlist(product)
    }
}