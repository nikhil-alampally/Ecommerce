package com.firebase.ecommerce.feature_cart.data.repository

import android.content.Context
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.domain.repository.CartRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CartRepositoryImp @Inject constructor(private val dataStore:StoreData, private val fireBase:Firebase):
    CartRepository {
    override fun getDataForCartItems(): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 -> fireBase.firestore.collection("Cartitems").document(it1) }
                        .collection("cart_items").get()
                        docRef.addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))

                        }.addOnSuccessListener { querySnapshot ->
                            val cartItemList = mutableListOf<CartItem>()

                            for (document in querySnapshot.documents) {
                                val product = document.toObject<CartItem>(CartItem::class.java)
                                if (product != null) {
                                    cartItemList.add(product)
                                }
                            }
                            trySend(Resource.Success(data = cartItemList))
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



    override fun deleteCartItem(documentPath:String,context: Context): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 -> fireBase.firestore.collection( Constants.cartItemCollectionPath).document(it1) }
                                .collection(Constants.cartItemInternalCollectionPath).document(documentPath).delete()
                        docRef.addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))

                        }.addOnSuccessListener {
                            trySend(Resource.Success(data =context.getString(R.string.DeletedSuccessfully)))
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