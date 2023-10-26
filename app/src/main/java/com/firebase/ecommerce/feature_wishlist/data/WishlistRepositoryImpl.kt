package com.firebase.ecommerce.feature_wishlist.data

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_wishlist.domain.WishlistItem
import com.firebase.ecommerce.feature_wishlist.domain.WishlistRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class WishlistRepositoryImpl @Inject constructor(
    private val dataStore: StoreData,
    private val fireBase: Firebase,
) : WishlistRepository {
    override fun getDataForWishlistItems(): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 ->
                                fireBase.firestore.collection("WishList").document(it1)
                            }
                                .collection("wish_list_items").get()
                        docRef.addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))

                        }.addOnSuccessListener { querySnapshot ->
                            val wishlistItemList = mutableListOf<WishlistItem>()

                            for (document in querySnapshot.documents) {
                                val product =
                                    document.toObject<WishlistItem>(WishlistItem::class.java)
                                if (product != null) {
                                    wishlistItemList.add(product)
                                }
                            }
                            trySend(Resource.Success(data = wishlistItemList))
                        }
                    }

                }
            } catch (e: Exception) {
                trySend(Resource.Error(message = e.localizedMessage.orEmpty()))
            }
            awaitClose {
                close()
            }
        }
    }

    override fun deleteWishlistItem(documentPath:String): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 -> fireBase.firestore.collection("WishList").document(it1) }
                                .collection("wish_list_items").document(documentPath).delete()
                        docRef.addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))

                        }.addOnSuccessListener {
                            trySend(Resource.Success(data = "deletedItemSuccessfully"))
                        }
                    }

                }
            } catch (e: Exception) {
                trySend(Resource.Error(message = e.localizedMessage.orEmpty()))
            }
            awaitClose {
                close()
            }
        }
    }
}