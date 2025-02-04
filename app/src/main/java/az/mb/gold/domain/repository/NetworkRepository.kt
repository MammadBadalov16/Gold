package az.mb.gold.domain.repository

import az.mb.gold.common.Resource
import az.mb.gold.data.remote.ProductDTO
import az.mb.gold.domain.model.Product

interface NetworkRepository {

    suspend fun upsertProduct(product: ProductDTO): Resource<ProductDTO>

    suspend fun syncProducts()

}