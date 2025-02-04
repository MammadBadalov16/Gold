package az.mb.gold.di

import android.content.Context
import az.mb.gold.common.PreferencesManager
import az.mb.gold.domain.repository.NetworkRepository
import az.mb.gold.domain.repository.ProductRepository
import az.mb.gold.domain.use_case.product.AddProductUseCase
import az.mb.gold.domain.use_case.product.DeleteProductUseCase
import az.mb.gold.domain.use_case.product.GetProductsUseCase
import az.mb.gold.domain.use_case.product.ProductUseCase
import az.mb.gold.domain.use_case.product.SyncProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [RoomModule::class])
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context)
    }


    @Provides
    @Singleton
    fun provideUseCases(
        repository: ProductRepository,
        networkRepository: NetworkRepository
    ): ProductUseCase {
        return ProductUseCase(
            syncProduct = SyncProductUseCase(networkRepository = networkRepository),
            getProducts = GetProductsUseCase(repository = repository),
            addProduct = AddProductUseCase(
                repository = repository,
                networkRepository = networkRepository
            ),
            deleteProduct = DeleteProductUseCase(
                repository = repository,
                networkRepository = networkRepository
            )
        )
    }

}
