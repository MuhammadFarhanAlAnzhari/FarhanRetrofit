package com.zhari.farhanretrofit.data.network.datasource

import com.zhari.farhanretrofit.data.network.service.ProductService
import com.zhari.farhanretrofit.data.model.ProductsResponse

interface ProductDataSource {
    suspend fun getProducts(): ProductsResponse
}

class ProductDataSourceImpl(private val service: ProductService) : ProductDataSource {
    override suspend fun getProducts(): ProductsResponse {
        return service.getProducts()
    }
}