package az.mb.gold.data.local

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import az.mb.gold.common.Constants
import az.mb.gold.data.local.dao.ProductDao
import az.mb.gold.data.local.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class GoldDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
}