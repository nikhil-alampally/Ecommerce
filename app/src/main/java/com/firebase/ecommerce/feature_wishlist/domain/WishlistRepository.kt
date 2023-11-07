package com.firebase.ecommerce.feature_wishlist.domain

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getDataForWishlistItems() : Flow<Resource<Any>>
    fun deleteWishlistItem(documentPath:String) : Flow<Resource<Any>>
}