package az.mb.gold.domain.use_case.product

import az.mb.gold.GoldApplication.Companion.context
import az.mb.gold.common.Helper.isInternetAvailable
import az.mb.gold.common.Resource
import az.mb.gold.data.mapper.toProductDTO
import az.mb.gold.data.mapper.toProductEntity
import az.mb.gold.domain.model.Product
import az.mb.gold.domain.repository.NetworkRepository
import az.mb.gold.domain.repository.ProductRepository


class DeleteProductUseCase(
    private val repository: ProductRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(product: Product) {
        product.isDeleted = true
        if (isInternetAvailable(context = context)) {
            val response = networkRepository.upsertProduct(product = product.toProductDTO())
            if (response is Resource.Success) {
                product.firebaseStatus = true
                repository.upsertProduct(product = product.toProductEntity())
            }
        } else {
            product.firebaseStatus = false
            repository.upsertProduct(product = product.toProductEntity())
        }
    }
}