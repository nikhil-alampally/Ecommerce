package com.firebase.ecommerce.feature_products.data.model

import com.firebase.ecommerce.feature_products.domain.model.Product

data class ProductDto(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val id: Int,
    val images: String,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val quantity: Int ,
)

internal fun ProductDto.toDomain(): Product {
    return Product(
        brand=brand,
        category = category,
        description = description,
        discountPercentage=discountPercentage,
        id = id,
        images=images,
        price=price,
        rating=rating,
        stock=stock,
        thumbnail=thumbnail,
        title=title,
        quantity = quantity
    )
}