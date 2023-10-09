package com.firebase.ecommerce.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun connectivityState(): State<ConnectionState> {
    val context= LocalContext.current
    return produceState(context.currentConnectivityState){
        context.observeConnectivityAsFlow().collect{
            value=it
        }
    }
}
sealed class ConnectionState{
    object Available:ConnectionState()
    object Unavailable:ConnectionState()
}