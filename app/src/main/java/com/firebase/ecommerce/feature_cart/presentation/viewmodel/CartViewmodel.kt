package com.firebase.ecommerce.feature_cart.presentation.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_cart.domain.use_case.GetDataForCartItemsUseCase
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.domain.use_case.CartItemDeleteUseCase
import com.firebase.ecommerce.feature_cart.presentation.signInState.CartSignInState
import com.firebase.ecommerce.feature_cart.presentation.signInState.DeleteSignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val getDataForCartItemsUseCase: GetDataForCartItemsUseCase, private val cartItemDeleteUseCase: CartItemDeleteUseCase): ViewModel(){
    private val _getDataInState = Channel<CartSignInState>()
    val getDataInState = _getDataInState.receiveAsFlow()
    private val _deleteItemInState = Channel<DeleteSignInState>()
    val deleteItemInState = _deleteItemInState.receiveAsFlow()
    @SuppressLint("SuspiciousIndentation")

    suspend fun getData() {
        viewModelScope.launch {
       getDataForCartItemsUseCase.getData()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _getDataInState.send(CartSignInState(isSuccess = result.data as ArrayList<CartItem>))
                        }
                        is Resource.Loading -> {
                            _getDataInState.send(CartSignInState(isLoading = true))
                        }
                        is Resource.Error -> {
                            _getDataInState.send(CartSignInState(isError = result.message))
                        }
                    }
                }
        }
    }

    suspend fun deleteCartItem(documentPath:String,context:Context) {
        viewModelScope.launch {
            cartItemDeleteUseCase.deleteCartItem(documentPath, context = context)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _deleteItemInState.send(DeleteSignInState(isSuccess ="successfully deleted"))
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