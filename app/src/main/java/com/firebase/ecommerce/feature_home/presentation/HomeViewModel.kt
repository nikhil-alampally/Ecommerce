package com.firebase.ecommerce.feature_home.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.HomeSignInState
import com.firebase.ecommerce.feature_home.data.HomeDataDto
import com.firebase.ecommerce.feature_home.domain.GetDataHomeScreenUseCase
import com.firebase.ecommerce.feature_home.domain.HomeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getDataHomeScreenUseCase: GetDataHomeScreenUseCase):ViewModel(){
    private val _getDataInState = Channel<HomeSignInState>()
    val getDataInState = _getDataInState.receiveAsFlow()
     @SuppressLint("SuspiciousIndentation")

     suspend fun getData() {
         viewModelScope.launch {
             getDataHomeScreenUseCase.getData()
                 .collect { result ->
                     when (result) {
                         is Resource.Success -> {
                             _getDataInState.send(HomeSignInState(isSuccess = result.data as HomeData?))
                         }
                         is Resource.Loading -> {
                             _getDataInState.send(HomeSignInState(isLoading = true))
                         }
                         is Resource.Error -> {
                             _getDataInState.send(HomeSignInState(isError = result.message))
                         }
                     }
                 }
         }




    }




}