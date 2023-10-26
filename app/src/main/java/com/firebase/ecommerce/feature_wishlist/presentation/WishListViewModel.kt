package com.firebase.ecommerce.feature_wishlist.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.DeleteSignInState
import com.firebase.ecommerce.feature_wishlist.WishListSignInState
import com.firebase.ecommerce.feature_wishlist.domain.WishlistItemModel
import com.firebase.ecommerce.feature_wishlist.domain.usecases.DeleteWishlistItemUsecase
import com.firebase.ecommerce.feature_wishlist.domain.usecases.GetDataForWishlistItemUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishlistItemUsecase: GetDataForWishlistItemUsecase,
    private val deleteWishlistItemUsecase: DeleteWishlistItemUsecase,
) :
    ViewModel() {

    private val _getDataInState = Channel<WishListSignInState>()
    val getDataInState = _getDataInState.receiveAsFlow()

    private val _deleteItemInState = Channel<DeleteSignInState>()
    val deleteItemInState = _deleteItemInState.receiveAsFlow()

    @SuppressLint("SuspiciousIndentation")
    suspend fun getData() {
        viewModelScope.launch {
            wishlistItemUsecase.getWishlistData()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _getDataInState.send(WishListSignInState(isSuccess = result.data as ArrayList<WishlistItemModel>))
                            Log.e("wishlist", "${result.data}")
                        }

                        is Resource.Loading -> {
                            _getDataInState.send(WishListSignInState(isLoading = true))
                        }

                        is Resource.Error -> {
                            _getDataInState.send(WishListSignInState(isError = result.message))
                        }
                    }
                }
        }

    }

    suspend fun deleteWishlistItem(documentPath: String) {
        viewModelScope.launch {
            deleteWishlistItemUsecase.deleteWishlistItem(documentPath)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _deleteItemInState.send(DeleteSignInState(isSuccess = "successfully deleted"))
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