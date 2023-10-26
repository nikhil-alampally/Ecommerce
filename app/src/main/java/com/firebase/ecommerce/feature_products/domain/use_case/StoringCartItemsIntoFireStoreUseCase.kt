package com.firebase.ecommerce.feature_products.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_home.data.model.HomeDataDto
import com.firebase.ecommerce.feature_login.domain.repository.RegistrationRepository
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StoringCartItemsIntoFireStoreUseCase@Inject constructor(private val productsRepository: ProductsRepository) {
    suspend fun storingCartItemsIntoFireStore(product: Product): Flow<Resource<Any>> {
        return productsRepository.addToCart(product)
    }
}