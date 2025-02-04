package az.mb.gold.di

import android.app.Application
import androidx.room.Room
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

    @Provides
    @Singleton
    fun provideShopDatabase(app: Application): GoldDatabase {
        return Room.databaseBuilder(
            app, GoldDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }


    @Provides
    @Singleton
    fun provideProductRepository(db: GoldDatabase): ProductRepository {
        return ProductRepositoryImpl(db.productDao)
    }

}