package com.firebase.ecommerce.feature_products.data.repository

import com.firebase.ecommerce.feature_products.data.model.ProductsListDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApiService {
    @GET("{id}")
    suspend fun getProducts(@Path("id") id: String): ProductsListDto

}