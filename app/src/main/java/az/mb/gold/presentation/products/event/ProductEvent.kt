package az.mb.gold.presentation.products.event

import az.mb.gold.domain.model.Product

sealed class ProductEvent {
    data class SaveProduct(val product: Product) : ProductEvent()
    data class DeleteProduct(val product: Product) : ProductEvent()
    data class CreatePdf(val product: List<Product>) : ProductEvent()
    data object SyncProduct : ProductEvent()
}
