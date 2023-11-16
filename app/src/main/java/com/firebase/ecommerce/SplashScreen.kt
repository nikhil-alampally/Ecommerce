package com.firebase.ecommerce

import android.window.SplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.firebase.ecommerce.feature_login.presentation.viewmodels.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(viewModel: LoginViewModel = hiltViewModel(),navigateToHomeScreen:()->Unit={},navigateToRegistration:()->Unit={}){
val scope= rememberCoroutineScope()
    var url by rememberSaveable {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

        AsyncImage(model = R.drawable.shopping_logo, contentDescription =null ,)
    }

    LaunchedEffect(key1 = url, block = {

        scope.launch {
            viewModel.getUserID().collect {
                if (it != null) {
                    url = it
                }
            }
        }
        if (url.isNotEmpty()) {
            navigateToHomeScreen.invoke()
        }else{
            delay(3000)
            navigateToRegistration.invoke()
        }
    })
}