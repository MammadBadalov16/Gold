package az.mb.gold.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import az.mb.gold.common.Constants
import az.mb.gold.data.local.GoldDatabase
import az.mb.gold.data.repository.ProductRepositoryImpl
import az.mb.gold.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ProductEntity RENAME TO temp_table")

            db.execSQL("""
            CREATE TABLE ProductEntity (
                id TEXT PRIMARY KEY NOT NULL,
                seller TEXT NOT NULL,
                productNumber TEXT NOT NULL,
                productName TEXT NOT NULL,
                weight REAL NOT NULL,
                purchasePrice INTEGER NOT NULL,
                salePrice INTEGER NOT NULL,
                profit INTEGER NOT NULL,
                datePurchase TEXT NOT NULL,
                dateSale TEXT NOT NULL,
                isSold INTEGER NOT NULL,
                firebaseStatus INTEGER NOT NULL,
                isDeleted INTEGER NOT NULL
            )
        """)

            db.execSQL("""
            INSERT INTO ProductEntity (id, seller, productNumber, productName, weight, purchasePrice, salePrice, profit, datePurchase, dateSale, isSold, firebaseStatus, isDeleted)
            SELECT id, seller, productNumber, productName, weight, purchasePrice, salePrice, profit, datePurchase, dateSale, isSold, firebaseStatus, isDeleted
            FROM temp_table
        """)

            // Köhnə cədvəli sil
            db.execSQL("DROP TABLE temp_table")
        }
    }


    @Provides
    @Singleton
    fun provideShopDatabase(app: Application): GoldDatabase {
        return Room.databaseBuilder(
            app, GoldDatabase::class.java, Constants.DATABASE_NAME
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }


    @Provides
    @Singleton
    fun provideProductRepository(db: GoldDatabase): ProductRepository {
        return ProductRepositoryImpl(db.productDao)
    }
}