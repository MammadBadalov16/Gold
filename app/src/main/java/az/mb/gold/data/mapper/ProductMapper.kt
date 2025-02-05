package az.mb.gold.data.mapper

import az.mb.gold.data.local.entity.ProductEntity
import az.mb.gold.data.remote.ProductDTO
import az.mb.gold.domain.model.Product


fun Product.toProductEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        seller = seller,
        productNumber = productNumber,
        productName = productName,
        categoryName = categoryName,
        weight = weight,
        purchasePrice = purchasePrice,
        salePrice = salePrice,
        profit = profit,
        datePurchase = datePurchase,
        dateSale = dateSale,
        isSold = isSold,
        firebaseStatus = firebaseStatus,
        isDeleted = isDeleted
    )
}

fun Product.toProductDTO(): ProductDTO {
    return ProductDTO(
        id = id,
        seller = seller,
        productNumber = productNumber,
        productName = productName,
        categoryName = categoryName,
        weight = weight,
        purchasePrice = purchasePrice,
        salePrice = salePrice,
        profit = profit,
        datePurchase = datePurchase,
        dateSale = dateSale,
        isSold = isSold,
        isDeleted = isDeleted
    )
}

fun ProductDTO.toProductEntity(firebaseStatus: Boolean): ProductEntity {
    return ProductEntity(
        id = id,
        seller = seller,
        productNumber = productNumber,
        productName = productName,
        categoryName = categoryName,
        weight = weight,
        purchasePrice = purchasePrice,
        salePrice = salePrice,
        profit = profit,
        datePurchase = datePurchase,
        dateSale = dateSale,
        isSold = isSold,
        firebaseStatus = firebaseStatus,
        isDeleted = isDeleted
    )
}

fun ProductEntity.toProductDTO(): ProductDTO {
    return ProductDTO(
        id = id,
        seller = seller,
        productNumber = productNumber,
        productName = productName,
        categoryName = categoryName,
        weight = weight,
        purchasePrice = purchasePrice,
        salePrice = salePrice,
        profit = profit,
        datePurchase = datePurchase,
        dateSale = dateSale,
        isSold = isSold,
        isDeleted = isDeleted
    )
}

fun List<ProductEntity>.toProducts(): List<Product> {
    val list = mutableListOf<Product>()
    this.forEach {
        list.add(
            Product(
                id = it.id,
                seller = it.seller,
                productNumber = it.productNumber,
                productName = it.productName,
                categoryName = it.categoryName,
                weight = it.weight,
                purchasePrice = it.purchasePrice,
                salePrice = it.salePrice,
                profit = it.profit,
                datePurchase = it.datePurchase,
                dateSale = it.dateSale,
                isSold = it.isSold,
                firebaseStatus = it.firebaseStatus,
                isDeleted = it.isDeleted
            )
        )
    }
    return list
}

fun Map<String, Any>.toProductDTO(): ProductDTO {
    return ProductDTO(
        id = this["id"] as String,
        seller = this["seller"] as String,
        productNumber = this["productNumber"] as String,
        productName = this["productName"] as String,
        categoryName = this["categoryName"] as String,
        weight = (this["weight"] as Number).toDouble(),
        purchasePrice = (this["purchasePrice"] as Number).toInt(),
        salePrice = (this["salePrice"] as Number).toInt(),
        profit = (this["profit"] as Number).toInt(),
        datePurchase = this["datePurchase"] as String,
        dateSale = this["dateSale"] as String,
        isSold = this["sold"] as Boolean,
        isDeleted = this["deleted"] as Boolean,
        updateAt = this["updateAt"] as Long,
        )
}


