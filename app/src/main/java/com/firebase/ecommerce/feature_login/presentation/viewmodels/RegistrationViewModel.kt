package com.firebase.ecommerce.feature_login.presentation.viewmodels


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.use_case.StoreRegistrationDetailsWithAuthenticationUseCase
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_login.presentation.SignInState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registrationUseCase: StoreRegistrationDetailsWithAuthenticationUseCase) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun storeRegistrationDetailsWithAuthentication(registrationDetails: RegistrationDetails,context: Context) {
        viewModelScope.launch {
            registrationUseCase.storeRegistrationDetailsWithAuthentication(registrationDetails)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _signInState.send(SignInState(isSuccess = context.getString(R.string.successfullyLoggedIn)))
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
    }


}

