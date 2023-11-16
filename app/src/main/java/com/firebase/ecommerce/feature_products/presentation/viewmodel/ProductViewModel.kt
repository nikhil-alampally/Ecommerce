package com.firebase.ecommerce.feature_products.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.core.UiEvents
import com.firebase.ecommerce.feature_cart.domain.model.CartItem
import com.firebase.ecommerce.feature_cart.presentation.signInState.CartSignInState
import com.firebase.ecommerce.feature_home.presentation.signInState.ProductsState
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.use_case.AddToWishListUseCase
import com.firebase.ecommerce.feature_products.domain.use_case.DeleteFromWishlistUseCase
import com.firebase.ecommerce.feature_products.domain.use_case.GetProductDataUseCase
import com.firebase.ecommerce.feature_products.domain.use_case.StoringCartItemsIntoFireStoreUseCase
import com.firebase.ecommerce.feature_wishlist.DeleteSignInState
import com.firebase.ecommerce.feature_wishlist.WishListSignInState
import com.firebase.ecommerce.feature_wishlist.domain.WishlistItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductDataUseCase: GetProductDataUseCase,
    private val storingCartItemsIntoFireStoreUseCase: StoringCartItemsIntoFireStoreUseCase,
    private val addToWishListUseCase: AddToWishListUseCase,
    private val deleteFromWishlistUseCase: DeleteFromWishlistUseCase,

) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow: SharedFlow<UiEvents> = _eventFlow.asSharedFlow()
    private val _getData = Channel<ProductsState>()
    val getData = _getData.receiveAsFlow()
    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    private val _addToCartDataState = Channel<CartSignInState>()
    val addToCartDataState = _addToCartDataState.receiveAsFlow()
    private val _addToWishListDataState = Channel<WishListSignInState>()
    val addToWishListDataState = _addToWishListDataState.receiveAsFlow()
    private val _deleteItemInState = Channel<DeleteSignInState>()
    val deleteItemInState = _deleteItemInState.receiveAsFlow()

    fun setCategory(value: String) {
        _selectedCategory.value = value
    }


    fun getProducts(id: String) {
        viewModelScope.launch {
            getProductDataUseCase.invoke(id).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        if (_selectedCategory.value == "All") {
                            result.data?.let { it1 ->
                                ProductsState(
                                    products = it1,
                                    isLoading = false
                                )
                            }
                                ?.let { _getData.send(it) }
                        } else {
                            val data =
                                result.data?.filter { it.category == _selectedCategory.value }
                            data?.let { ProductsState(products = it, isLoading = false) }
                                ?.let { _getData.send(it) }
                            println("data=${data}")
                        }
                    }

                    is Resource.Error -> {
                        _getData.send(ProductsState(error = result.message, isLoading = false))

                    }

                    is Resource.Loading -> {
                        _getData.send(ProductsState(isLoading = true))

                    }
                }
            }
        }
    }

    fun storeAddToCartData(product: Product) {
        viewModelScope.launch {
            storingCartItemsIntoFireStoreUseCase.storingCartItemsIntoFireStore(product)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.e("success in getting data", "ssucess")
                            _addToCartDataState.send(CartSignInState(isSuccess = result.data as ArrayList<CartItem>))
                        }
                        is Resource.Loading -> {
                            Log.e("loading in getting data", "ssucess")
                            _addToCartDataState.send(CartSignInState(isLoading = true))
                        }
                        is Resource.Error -> {
                            Log.e("error in getting data", result.message.toString())
                            _addToCartDataState.send(CartSignInState(isError = result.message))
                        }
                    }
                }
        }
    }

    fun addToWishlistData(product: Product) {
        viewModelScope.launch {
            addToWishListUseCase.addToWIshList(product)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            Log.e("success in getting data", "success")
                            _addToWishListDataState.send(WishListSignInState(isSuccess = result.data as ArrayList<WishlistItemModel>))
                        }

                        is Resource.Loading -> {
                            Log.e("loading in getting data", "success")
                            _addToWishListDataState.send(WishListSignInState(isLoading = true))
                        }

                        is Resource.Error -> {
                            Log.e("error in getting data", result.message.toString())
                            _addToWishListDataState.send(WishListSignInState(isError = result.message))
                        }
                    }
                }
        }
    }

    fun deleteWishlistItem(documentPath: String) {
        viewModelScope.launch {
            deleteFromWishlistUseCase.deleteWishListItem(documentPath)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvents.SnackbarEvent(message = "No Wishlist items found")
                            )

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