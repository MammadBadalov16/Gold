package az.mb.gold.domain.use_case.product

import az.mb.gold.GoldApplication.Companion.context
import az.mb.gold.common.Helper.isInternetAvailable
import az.mb.gold.common.Resource
import az.mb.gold.data.mapper.toProductDTO
import az.mb.gold.data.mapper.toProductEntity
import az.mb.gold.domain.model.Product
import az.mb.gold.domain.repository.NetworkRepository
import az.mb.gold.domain.repository.ProductRepository

class AddProductUseCase(
    private val repository: ProductRepository,
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(product: Product) {


        repository.upsertProduct(product.toProductEntity())

        if (isInternetAvailable(context = context)) {
            when (val response = networkRepository.upsertProduct(product.toProductDTO())) {
                is Resource.Success -> {
                    response.data?.let {
                        repository.upsertProduct(
                            it.toProductEntity(
                                firebaseStatus = true,
                            )
                        )
                    }
                }

                is Resource.Error -> {
                }

                else -> {

                }
            }

        }
    }
}