package com.firebase.ecommerce.feature_wishlist.domain.usecases

import android.annotation.SuppressLint
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_wishlist.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDataForWishlistItemUsecase @Inject constructor(private val wishlistRepository: WishlistRepository) {
    @SuppressLint("SuspiciousIndentation")
    fun getWishlistData(): Flow<Resource<Any>> {
        return wishlistRepository.getDataForWishlistItems()
    }
}