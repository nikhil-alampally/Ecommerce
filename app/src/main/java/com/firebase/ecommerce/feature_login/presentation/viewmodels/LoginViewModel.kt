package com.firebase.ecommerce.feature_login.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.StoreData
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.firebase.ecommerce.feature_login.domain.use_case.StoringGoogleSignInDataIntoFireStoreUseCase
import com.firebase.ecommerce.feature_login.presentation.SignInState
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: RegistrationRepository,private val dataStore:StoreData,
    private val storingGoogleSignInDataIntoFireStoreUseCase: StoringGoogleSignInDataIntoFireStoreUseCase
) : ViewModel() {
    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()
    private val _googleSignInState = Channel<SignInState>()
    val googleSignInState = _googleSignInState.receiveAsFlow()
    private val _googleSignDataState = Channel<SignInState>()
    val googleSignDataState = _googleSignDataState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email, password).collect { result ->
            when (result) {

                is Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = result.data?.user?.uid))
                }

                is Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }

                is Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }
            }
        }
    }
    fun saveUserName(userId: String) {
        viewModelScope.launch {
            dataStore.saveData(userId)
        }
    }
    fun getUserID(): Flow<String?> {
        return dataStore.getData

    }
    fun storingGoogleSignInDataIntoFireStore(homeData: HomeDataDto)=
        viewModelScope.launch {
            storingGoogleSignInDataIntoFireStoreUseCase.storingGoogleSignInDataIntoFireStore(homeData)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _googleSignDataState.send(SignInState(isSuccess = "sucess"))
                        }
                        is Resource.Loading -> {
                            _googleSignDataState.send(SignInState(isLoading = true))
                        }
                        is Resource.Error -> {
                            _googleSignDataState.send(SignInState(isError = result.message))
                        }
                    }
                }
        }


    fun signWithGoogle(credential: AuthCredential) = viewModelScope.launch {
        repository.signInWithGoogle(credential).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _googleSignInState.send(SignInState(isSuccess =result.data?.user?.uid))
                }

                is Resource.Loading -> {
                    _googleSignInState.send(SignInState(isLoading = true))
                }

                is Resource.Error -> {
                    _googleSignInState.send(SignInState(isError = result.message))
                }
            }
        }
    }
}