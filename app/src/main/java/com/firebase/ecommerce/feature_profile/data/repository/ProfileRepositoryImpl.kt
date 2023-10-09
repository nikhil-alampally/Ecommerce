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

    suspend fun getUrl(): Uri {
        return storage.reference.child(PROFILE).child(PROFILE_IMAGE_NAME).downloadUrl.await()
    }

    override suspend fun addImageUrlToFirestore(uri: Uri): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            try {
                db.collection(Constants.PROFILE).document(UID).set(
                    mapOf(
                        URL to uri,
                        CREATED_AT to FieldValue.serverTimestamp()
                    )
                ).await()
                emit(Resource.Success(true))
            } catch (e: Exception) {
                emit(Resource.Error(data = null, message = e.message.orEmpty()))
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
        context: Context,
        profileModel: ProfileModel,
    ): Flow<Resource<Any>> {
        return callbackFlow {
            try {
                Firebase.firestore
                    .collection("Profile")
                    .document(profileModel.userName).set(toProfileModel(profileModel))
                    .addOnSuccessListener {
                        Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT)
                            .show()
                    }

            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            awaitClose {
                close()
            }
        }
    }
}