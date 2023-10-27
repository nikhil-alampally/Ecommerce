package com.firebase.ecommerce.feature_cart.di

import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import com.firebase.ecommerce.feature_cart.data.repository.CartRepositoryImp
import com.firebase.ecommerce.feature_cart.domain.use_case.GetDataForCartItemsUseCase
import com.firebase.ecommerce.feature_cart.domain.use_case.CartItemDeleteUseCase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartRepository(
        dataStore: StoreData, fireBase: Firebase,
    ): CartRepository {
        return CartRepositoryImp(
           dataStore,fireBase
        )
    }
    @Provides
    @Singleton
    fun getCartDataUseCase(
      cartRepository: CartRepository
    ): GetDataForCartItemsUseCase {
        return GetDataForCartItemsUseCase(cartRepository)
    }
    @Provides
    @Singleton
    fun deleteCartItemUseCase(
        cartRepository: CartRepository
    ): CartItemDeleteUseCase {
        return CartItemDeleteUseCase(cartRepository)
    }

}