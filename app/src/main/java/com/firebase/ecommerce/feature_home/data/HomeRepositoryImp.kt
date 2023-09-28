package com.firebase.ecommerce.feature_home.data

import android.util.Log

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.domain.HomeData
import com.firebase.ecommerce.feature_home.domain.HomeRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(val firebase: Firebase, val dataStore: StoreData) :
    HomeRepository {
    override suspend fun getDataFromFirebase(): Flow<Resource<Any>> {
        return callbackFlow {
trySend(Resource.Loading(true))

          try {
              dataStore.getData.collect {
                  if (it != null) {
                      Log.e("print", it)
                      val docRef =
                          it.let { it1 -> firebase.firestore.collection("Profile").document(it1) }
                      docRef.get().addOnFailureListener {
                          Log.e("fail", "fail")

                       }.addOnSuccessListener { documentSnapshot ->
                          val data = documentSnapshot.toObject<HomeData>(HomeData::class.java)
                          trySend(Resource.Success(data = data))
                      }
                  }

              }
          }catch (e:Exception){
              trySend(Resource.Error(message = e.localizedMessage.orEmpty()))
          }
            awaitClose {
                close()
            }
        }

    }
}









