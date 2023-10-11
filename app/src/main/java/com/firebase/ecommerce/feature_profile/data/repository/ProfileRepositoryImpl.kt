package com.firebase.ecommerce.feature_profile.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.firebase.ecommerce.core.Constants
import com.firebase.ecommerce.core.Constants.CREATED_AT
import com.firebase.ecommerce.core.Constants.PROFILE
import com.firebase.ecommerce.core.Constants.PROFILE_IMAGE_NAME
import com.firebase.ecommerce.core.Constants.UID
import com.firebase.ecommerce.core.Constants.URL
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_profile.data.model.toProfileModel
import com.firebase.ecommerce.feature_profile.domain.model.ProfileModel
import com.firebase.ecommerce.feature_profile.domain.repository.ProfileRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val db: FirebaseFirestore,
    val dataStore: StoreData,
) : ProfileRepository {
    override suspend fun addImageToFirebaseStorage(imageUri: Uri): Flow<Resource<Any>> {
        return flow {
            emit(Resource.Loading())
            try {
                val downloadUrl = storage.reference.child(PROFILE).child(PROFILE_IMAGE_NAME)
                    .putFile(imageUri).await()
                    .storage.downloadUrl.await()

                if (downloadUrl.path.isNullOrBlank().not()) {
                    emit(Resource.Success(downloadUrl))
                } else {
                    emit(Resource.Error(data = null, message = "Image is no uploaded"))
                }
            } catch (e: Exception) {
                Resource.Error(data = null, message = e.message.orEmpty())
            }
        }
    }

    override suspend fun getImageUrlFromFirestore(): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading())
            try {
                val imageUrl =
                    db.collection(Constants.PROFILE).document(UID).get().await().getString(URL)
                Log.e("data", "$imageUrl")
                emit(Resource.Success(imageUrl))
            } catch (e: Exception) {
                emit(Resource.Error(data = null, message = e.message.orEmpty()))
            }
        }
    }

    override suspend fun saveUserDetailsInFireStore(
        profileModel: ProfileModel,
        ): Flow<Resource<Any>> {
        return callbackFlow {
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        Firebase.firestore
                            .collection("Profile")
                            .document(it).set(toProfileModel(profileModel))
                            .addOnSuccessListener {

                            }
                    }
                }
            } catch (_: Exception) {
            }
            awaitClose {
                close()
            }
        }
    }
}