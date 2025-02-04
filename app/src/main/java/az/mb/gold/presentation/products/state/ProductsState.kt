package az.mb.gold.presentation.products.state

import az.mb.gold.domain.model.Audit
import az.mb.gold.domain.model.Product


data class ProductsState(
   // val isLoading: Boolean = false,
    var products: List<Product> = emptyList(),
    val audit: Audit = Audit(0.0, 0.0, 0.0, 0.0)
    //val error: String = ""
)
