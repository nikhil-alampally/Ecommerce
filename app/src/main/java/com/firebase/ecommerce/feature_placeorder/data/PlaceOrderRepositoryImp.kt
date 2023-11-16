package com.firebase.ecommerce.feature_placeorder.data

import android.content.Context
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_placeorder.domain.repository.PlaceOrderRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class PlaceOrderRepositoryImp @Inject constructor(val firebase: Firebase, private val dataStore: StoreData,):
    PlaceOrderRepository {


    override fun addAddressToFirebase(addAddress:AddAddress) : Flow<Resource<Any>>{
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {value->
                    if (value != null) {
                        val docRef =
                            value.let { it1 -> firebase.firestore.collection("Address").document(it1).collection(
                                "address_details"
                            ) }
                        docRef.document(addAddress.id).set(addAddress).addOnFailureListener {
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
    override fun getAddressFromFirebase(): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 -> firebase.firestore.collection("Address").document(it1) }
                                .collection("address_details").get()
                        docRef.addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))

                        }.addOnSuccessListener { querySnapshot ->
                            val cartItemList = mutableListOf<AddAddress>()

                            for (document in querySnapshot.documents) {
                                val address = document.toObject<AddAddress>(AddAddress::class.java)
                                if (address != null) {
                                    cartItemList.add(address)
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

    override fun deleteAddressFromFirebase(documentPath:String,context: Context): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect { userId->
                    if (userId != null) {
                        val docRef =
                            userId.let { firebase.firestore.collection("Address").document(it) }
                                .collection("address_details").document(documentPath).delete()
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