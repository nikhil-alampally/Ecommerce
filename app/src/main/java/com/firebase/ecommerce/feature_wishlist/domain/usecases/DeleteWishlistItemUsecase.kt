package com.firebase.ecommerce.feature_wishlist.domain.usecases


import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_wishlist.domain.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteWishlistItemUsecase @Inject constructor(private val wishlistRepository: WishlistRepository) {
    fun deleteWishlistItem(documentPath:String): Flow<Resource<Any>> {
       return wishlistRepository.deleteWishlistItem(documentPath)
    }
}