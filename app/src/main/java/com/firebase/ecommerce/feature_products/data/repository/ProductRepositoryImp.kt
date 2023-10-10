package com.firebase.ecommerce.feature_products.data.repository

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.data.ProductApiService
import com.firebase.ecommerce.feature_products.data.model.toDomain
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(private val productApiService: ProductApiService):
    ProductsRepository {
    override suspend fun getProducts(id: String):Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = productApiService.getProducts(id)
            emit(Resource.Success(response.products.map{it.toDomain()}))
        } catch (e: IOException) {
            emit(Resource.Error(message = "Could not reach the server, please check your internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = "Oops, something went wrong!"))
        }
    }
}