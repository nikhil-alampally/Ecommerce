package com.firebase.ecommerce.feature_wishlist.di

import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_wishlist.domain.usecases.GetDataForWishlistItemUsecase
import com.firebase.ecommerce.feature_wishlist.domain.WishlistRepository
import com.firebase.ecommerce.feature_wishlist.data.WishlistRepositoryImpl
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishlistModule {
    @Provides
    @Singleton
    fun providesWishlistRepository(
        dataStore: StoreData, fireBase: Firebase,
    ): WishlistRepository {
        return WishlistRepositoryImpl(
            dataStore, fireBase
        )
    }

    @Provides
    @Singleton
    fun providesGetDataUseCase(
        wishlistRepository: WishlistRepository,
    ): GetDataForWishlistItemUsecase {
        return GetDataForWishlistItemUsecase(wishlistRepository)
    }

}