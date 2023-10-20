package com.firebase.ecommerce.feature_products.data.repository

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_products.data.model.toDomain
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class ProductRepositoryImp @Inject constructor(private val productApiService: ProductApiService, private val dataStore: StoreData, val firebase: Firebase):
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

    override suspend fun addToCart(product:Product): Flow<Resource<Any>> {
        return callbackFlow {
                trySend(Resource.Loading(true))
                try {
                    dataStore.getData.collect {value->
                        if (value != null) {
                            val docRef =
                                value.let { it1 -> firebase.firestore.collection("Cartitems").document(it1).collection(
                                    "cart_items"
                                ) }
                            docRef.document("${product.title}-${product.id}").set(product).addOnFailureListener {
                                trySend(Resource.Error(message = it.localizedMessage))
                            }.addOnSuccessListener {
                                trySend(Resource.Success(data = value))
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
