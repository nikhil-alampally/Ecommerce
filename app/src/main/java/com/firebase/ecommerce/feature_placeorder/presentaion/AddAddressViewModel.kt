package com.firebase.ecommerce.feature_placeorder.presentaion

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.presentation.signInState.AddressSignInState
import com.firebase.ecommerce.feature_cart.presentation.signInState.DeleteSignInState
import com.firebase.ecommerce.feature_login.presentation.SignInState
import com.firebase.ecommerce.feature_placeorder.data.AddAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val getAddressUseCase: GetAddressUseCase,
    private val deleteAddressFromFirebase: DeleteAddressFromFirebase
) : ViewModel() {
    private val _addAddressDataState = Channel<SignInState>()
    val addAddressDataState = _addAddressDataState.receiveAsFlow()
    private val _getDataInState = Channel<AddressSignInState>()
    val getDataInState = _getDataInState.receiveAsFlow()
    private val _deleteItemInState = Channel<DeleteSignInState>()
    val deleteItemInState = _deleteItemInState.receiveAsFlow()
    private val _editItemInState = Channel<DeleteSignInState>()
    val editItemInState = _editItemInState.receiveAsFlow()
    private val _address: MutableState<AddAddress?> = mutableStateOf(null)
    val address: State<AddAddress?> = _address
    fun addAddressData(addAddress: AddAddress, context: Context) {
        viewModelScope.launch {
            addAddressUseCase.addAddress(addAddress)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _addAddressDataState.send(SignInState(isSuccess = context.getString(R.string.successfullyLoggedIn)))
                        }

                        is Resource.Loading -> {
                            _addAddressDataState.send(SignInState(isLoading = true))
                        }

                        is Resource.Error -> {
                            _addAddressDataState.send(SignInState(isError = result.message))
                        }
                    }
                }
        }
    }

    fun saveAddress(address: AddAddress) {
        _address.value = address
    }

    suspend fun getData() {
        getAddressUseCase.getAddress()
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _getDataInState.send(AddressSignInState(isSuccess = result.data as ArrayList<AddAddress>))
                    }

                    is Resource.Loading -> {
                        _getDataInState.send(AddressSignInState(isLoading = true))
                    }

                    is Resource.Error -> {
                        _getDataInState.send(AddressSignInState(isError = result.message))
                    }
                }
            }
    }

    fun deleteAddress(documentPath: String, context: Context) {
        viewModelScope.launch {
            deleteAddressFromFirebase.deleteAddressFromFirebase(documentPath, context = context)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _deleteItemInState.send(DeleteSignInState(isSuccess =context.getString(R.string.DeletedSuccessfully)))
                        }

                        is Resource.Loading -> {
                            _deleteItemInState.send(DeleteSignInState(isLoading = true))
                        }

                        is Resource.Error -> {
                            _deleteItemInState.send(DeleteSignInState(isError = result.message))
                        }
                    }
                }
        }
    }
}