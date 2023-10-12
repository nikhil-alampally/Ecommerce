package com.firebase.ecommerce.feature_products.di

import com.firebase.ecommerce.feature_products.data.ProductApiService
import com.firebase.ecommerce.feature_products.data.repository.ProductRepositoryImp
import com.firebase.ecommerce.feature_products.domain.use_case.GetProductDataUseCase
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object ProductDi {
    val BASE_URL="https://mocki.io/v1/"

    @Provides
    @Singleton
    fun provideProductsApiService(): ProductApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideProductsRepository(
        productsApiService: ProductApiService
    ): ProductsRepository {
        return ProductRepositoryImp(
            productsApiService
        )
    }


    @Provides
    @Singleton
    fun getDataUseCase(
        productsRepository: ProductsRepository
    ): GetProductDataUseCase {
        return GetProductDataUseCase(productsRepository)
    }

}