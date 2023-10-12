package com.firebase.ecommerce.feature_home

import com.firebase.ecommerce.feature_products.domain.model.Product

data class ProductsState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)