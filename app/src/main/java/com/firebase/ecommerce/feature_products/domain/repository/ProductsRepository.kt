package com.firebase.ecommerce.feature_products.domain.repository

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getProducts(id:String): Flow<Resource<List<Product>>>
    suspend fun addToCart(product: Product):Flow<Resource<Any>>

    suspend fun addToWishlist(product: Product):Flow<Resource<Any>>

    suspend fun deleteFromWishlist(documentPath:String):Flow<Resource<Any>>
}