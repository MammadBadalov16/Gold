package az.mb.gold.domain.use_case.product

data class ProductUseCase(
    val syncProduct: SyncProductUseCase,
    val getProducts: GetProductsUseCase,
    val deleteProduct: DeleteProductUseCase,
    val addProduct: AddProductUseCase,
)