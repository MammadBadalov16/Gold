package az.mb.gold.data.remote

import com.google.firebase.firestore.FieldValue

data class ProductDTO(
    val id: String,
    val seller: String,
    val productNumber: String,
    val productName: String,
    val weight: Double,
    val purchasePrice: Int,
    val salePrice: Int,
    val profit: Int,
    val datePurchase: String,
    val dateSale: String,
    val isSold: Boolean,
    val isDeleted: Boolean,
    var updateAt: Long = System.currentTimeMillis(),


)


