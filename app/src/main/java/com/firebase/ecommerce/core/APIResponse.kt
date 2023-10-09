package com.firebase.ecommerce.core

data class APIResponse<T>(
    val isLoading: Boolean = false,
    val response: T? = null,
    val errorMessage: String = "",
) {
}