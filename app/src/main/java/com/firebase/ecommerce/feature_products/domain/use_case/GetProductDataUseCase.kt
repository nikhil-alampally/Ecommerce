package com.firebase.ecommerce.feature_products.domain.use_case

import com.firebase.ecommerce.core.Resource
import com.firebase.ecommerce.feature_products.domain.model.Product
import com.firebase.ecommerce.feature_products.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

class GetProductDataUseCase (private val productsRepository: ProductsRepository){
    suspend  fun invoke(id:String): Flow<Resource<List<Product>>> {
        return productsRepository.getProducts(id)
    }
}