package az.mb.gold.presentation.products.state

import androidx.paging.PagingData
import az.mb.gold.domain.model.Product
import kotlinx.coroutines.flow.Flow

sealed class ProductState {
    object Loading : ProductState()
    data class Paging(val pagingData: Flow<PagingData<Product>>) : ProductState()
    data class Error(val message: String) : ProductState()
}