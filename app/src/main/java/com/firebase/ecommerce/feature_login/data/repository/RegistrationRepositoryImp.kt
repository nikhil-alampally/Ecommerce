package com.firebase.ecommerce.feature_login.data.repository



import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.data.model.toDomain
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class RegistrationRepositoryImp @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: StoreData,
    val firebase: Firebase
) : RegistrationRepository {


    override suspend fun authenticateEmailAndPassword(registrationDetails: RegistrationDetails): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading())

            try {
                val firebaseAuth = firebaseAuth
                val userDetails = firebaseAuth.createUserWithEmailAndPassword(
                    registrationDetails.email,
                    registrationDetails.password
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(Resource.Success(task.result))
                    }
                }.await()

                val user = userDetails.user?.uid

                val fireStoreInstance = Firebase.firestore
                user?.let {
                    fireStoreInstance.collection("Profile").document(it)
                        .set(toDomain(registrationDetails))
                }
            } catch (e: Exception) {
                trySend(Resource.Error(message = e.localizedMessage.orEmpty()))
            }
            awaitClose {
                close()
            }

        }
    }

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }
    }

    override fun signInWithGoogle(credential: AuthCredential): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithCredential(credential).await()

            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(message = it.message.toString()))
        }
    }

    override fun addDetailsIntoFireStore(homeData: HomeDataDto): Flow<Resource<Any>> {
        return callbackFlow {
            trySend(Resource.Loading(true))
            try {
                dataStore.getData.collect {
                    if (it != null) {
                        val docRef =
                            it.let { it1 -> firebase.firestore.collection("Profile").document(it1) }
                        docRef.set(homeData).addOnFailureListener {
                            trySend(Resource.Error(message = it.localizedMessage))
                        }.addOnSuccessListener {
                            trySend(Resource.Success(data = "successfully added"))
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


