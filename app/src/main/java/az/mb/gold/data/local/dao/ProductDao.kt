package az.mb.gold.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import az.mb.gold.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT  * FROM ProductEntity WHERE isDeleted = 1")
    fun getDeletedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE firebaseStatus = 0")
    suspend fun getProductsNotInFirebase(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: ProductEntity): Long

    suspend fun getProductsFilter(product: ProductEntity, option: Int): Flow<List<ProductEntity>> {
        val queryBuilder =
            StringBuilder("SELECT * FROM ProductEntity WHERE 1=1")

        if (product.productNumber != "") {
            queryBuilder.append(" AND productNumber = '${product.productNumber}'")
        }

        if (product.seller != "") {
            queryBuilder.append(" AND LOWER(seller) = LOWER('${product.seller}')")
        }

        if (product.productName != "") {
            queryBuilder.append(" AND LOWER(productName) = LOWER('${product.productName}')")
        }

        if (product.weight != 0.0) {
            queryBuilder.append(" AND weight = ${product.weight}")
        }

        if (product.purchasePrice != 0) {
            queryBuilder.append(" AND purchasePrice = ${product.purchasePrice}")
        }

        if (product.salePrice != 0) {
            queryBuilder.append(" AND salePrice = ${product.salePrice}")
        }

        if (product.profit != 0) {
            queryBuilder.append(" AND profit = ${product.profit}")
        }

        if (product.datePurchase != "" && product.dateSale != "") {
            queryBuilder.append(" AND datePurchase BETWEEN '${product.datePurchase}' AND '${product.dateSale}'")
        } else {
            if (product.datePurchase != "") {
                queryBuilder.append(" AND datePurchase = '${product.datePurchase}'")
            }
            if (product.dateSale != "") {
                queryBuilder.append(" AND dateSale = '${product.dateSale}'")
            }
        }

        if (option == 1) {
            queryBuilder.append(" AND isSold = 1")
        } else if (option == 2) {
            queryBuilder.append(" AND isSold = 0")
        }

        queryBuilder.append(" AND isDeleted = 0")

        queryBuilder.append(" ORDER BY rowid DESC")

        val query = SimpleSQLiteQuery(queryBuilder.toString())


        return executeCustomQuery(query)
    }

    @RawQuery(observedEntities = [ProductEntity::class])
    fun executeCustomQuery(query: SupportSQLiteQuery): Flow<List<ProductEntity>>

}


