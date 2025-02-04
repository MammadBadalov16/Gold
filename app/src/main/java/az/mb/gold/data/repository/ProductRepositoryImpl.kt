package az.mb.gold.data.repository

import az.mb.gold.data.local.dao.ProductDao
import az.mb.gold.data.local.entity.ProductEntity
import az.mb.gold.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val dao: ProductDao
) : ProductRepository {
    override suspend fun getDeletedProducts(): Flow<List<ProductEntity>> {
        return dao.getDeletedProducts()
    }

    override suspend fun getProductsFilter(
        product: ProductEntity,
        option: Int
    ): Flow<List<ProductEntity>> {
        return dao.getProductsFilter(product = product, option = option)
    }

    override suspend fun upsertProduct(product: ProductEntity): Long {
        return dao.upsertProduct(product = product)
    }
}