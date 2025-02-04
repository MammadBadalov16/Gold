package az.mb.gold.domain.use_case.product

import az.mb.gold.GoldApplication.Companion.context
import az.mb.gold.common.Helper.isInternetAvailable
import az.mb.gold.domain.repository.NetworkRepository

class SyncProductUseCase(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke() {
        if (isInternetAvailable(context = context)) {
            networkRepository.syncProducts()
        }
    }
}