package com.firebase.ecommerce.core

sealed class UiEvents {
    data class SnackbarEvent(val message: String) : UiEvents()
}