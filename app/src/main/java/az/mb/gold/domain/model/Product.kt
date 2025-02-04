package az.mb.gold.domain.model

import java.util.UUID

data class Product(
    var id: String = UUID.randomUUID().toString(),
    val seller: String,
    val productNumber: String,
    val productName: String,
    val categoryName: String,
    val weight: Double,
    val purchasePrice: Int,
    val salePrice: Int,
    val profit: Int,
    val datePurchase: String,
    val dateSale: String,
    val isSold: Boolean,
    var isDeleted: Boolean,
    var firebaseStatus: Boolean,
) {
    constructor(
    ) : this(
        productNumber = "",
        seller = "",
        productName = "",
        categoryName = "",
        weight = 0.0,
        purchasePrice = 0,
        salePrice = 0,
        profit = 0,
        datePurchase = "",
        dateSale = "",
        isDeleted = false,
        isSold = false,
        firebaseStatus = false,
    )
}


