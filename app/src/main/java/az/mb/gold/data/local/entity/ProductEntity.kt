package az.mb.gold.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey
    val id: String,
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
    var firebaseStatus: Boolean,
    val isDeleted: Boolean,

)



