package com.firebase.ecommerce.feature_products.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.ProductsState
import com.firebase.ecommerce.feature_products.domain.use_case.GetProductDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor( private val getProductDataUseCase: GetProductDataUseCase):ViewModel() {


    private val _getData = Channel<ProductsState>()
    val getData = _getData.receiveAsFlow()
    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory

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
}