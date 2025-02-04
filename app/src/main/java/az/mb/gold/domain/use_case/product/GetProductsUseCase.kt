package az.mb.gold.domain.use_case.product

import az.mb.gold.data.mapper.toProductEntity
import az.mb.gold.data.mapper.toProducts
import az.mb.gold.domain.model.Product
import az.mb.gold.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product, option: Int): Flow<List<Product>> {
        return repository.getProductsFilter(product = product.toProductEntity(), option = option)
            .map { it.toProducts() }
    }
}
