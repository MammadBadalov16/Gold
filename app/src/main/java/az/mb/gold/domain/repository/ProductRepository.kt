package az.mb.gold.domain.repository

import az.mb.gold.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getDeletedProducts() : Flow<List<ProductEntity>>

    suspend fun getProductsFilter(product: ProductEntity, option: Int): Flow<List<ProductEntity>>

    suspend fun upsertProduct(product: ProductEntity): Long

}