package com.firebase.ecommerce.feature_login.presentation.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_login.domain.use_case.StoreRegistrationDetailsWithAuthenticationUseCase
import com.firebase.ecommerce.feature_login.domain.model.RegistrationDetails
import com.firebase.ecommerce.feature_login.presentation.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val registrationUseCase: StoreRegistrationDetailsWithAuthenticationUseCase) : ViewModel() {

    private val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun storeRegistrationDetailsWithAuthentication(registrationDetails: RegistrationDetails) {
        viewModelScope.launch {
            registrationUseCase.storeRegistrationDetailsWithAuthentication(registrationDetails)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _signInState.send(SignInState(isSuccess = "Successfully Logged In"))
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

