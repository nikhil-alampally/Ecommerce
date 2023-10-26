package com.firebase.ecommerce.feature_products.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.R
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.presentation.signInState.ProductsState
import com.firebase.ecommerce.feature_login.presentation.SignInState
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.use_case.GetProductDataUseCase
import com.firebase.ecommerce.feature_products.domain.use_case.StoringCartItemsIntoFireStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor( private val getProductDataUseCase: GetProductDataUseCase,private val storingCartItemsIntoFireStoreUseCase: StoringCartItemsIntoFireStoreUseCase):ViewModel() {


    private val _getData = Channel<ProductsState>()
    val getData = _getData.receiveAsFlow()
    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    private val _addToCartDataState = Channel<SignInState>()
    val addToCartDataState = _addToCartDataState.receiveAsFlow()
    fun setCategory(value: String) {
        _selectedCategory.value = value
    }

fun getProducts(id:String){
    viewModelScope.launch {
        getProductDataUseCase.invoke(id).collectLatest{result->
            when(result){
                is Resource.Success->{
                    if(_selectedCategory.value=="All") {
                        result.data?.let { it1 -> ProductsState(products = it1, isLoading = false) }
                            ?.let { _getData.send(it) }
                    }else{
                       val data= result.data?.filter { it.category==_selectedCategory.value }
                        data?.let { ProductsState(products = it, isLoading = false) }?.let { _getData.send(it) }
                        println("data=${data}")
                    }
                }
                is Resource.Error->{
                    _getData.send(ProductsState(error = result.message, isLoading = false))

                }
                is Resource.Loading->{
                    _getData.send(ProductsState(isLoading = true))

                }
            }
        }
    }
}

fun storeAddToCartData(product: Product,context:Context) {
    viewModelScope.launch {
       storingCartItemsIntoFireStoreUseCase.storingCartItemsIntoFireStore(product)
            .collect { result ->
                when (result) {
                    is Resource.Success -> {
                        Log.e("success in getting data","ssucess")
                        _addToCartDataState.send(SignInState(isSuccess = context.getString(R.string.successfullyLoggedIn)))
                    }

                    is Resource.Loading -> {
                        Log.e("loading in getting data","ssucess")
                        _addToCartDataState.send(SignInState(isLoading = true))
                    }

                    is Resource.Error -> {
                        Log.e("error in getting data",result.message.toString())
                        _addToCartDataState.send(SignInState(isError = result.message))
                    }
                }
            }
    }
}
}