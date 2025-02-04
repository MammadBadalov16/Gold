package az.mb.gold.di

import androidx.paging.PagingConfig
import az.mb.gold.common.FireStoreCollection.PAGE_SIZE
import az.mb.gold.common.FireStoreCollection.PRODUCTS
import az.mb.gold.common.PreferencesManager
import az.mb.gold.data.local.GoldDatabase
import az.mb.gold.data.repository.NetworkRepositoryImpl
import az.mb.gold.domain.repository.NetworkRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module(includes = [RoomModule::class, AppModule::class])
object FirebaseModule {

    @Provides
    fun provideQueryProducts() = Firebase.firestore
        .collection(PRODUCTS)
        .limit(PAGE_SIZE)


    @Provides
    fun providePagingConfig() = PagingConfig(
        pageSize = PAGE_SIZE.toInt()
    )

    @Provides
    @Singleton
    fun provideFirebaseDatabaseInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFireStoreInstance(): FirebaseFirestore {


        val firestore = FirebaseFirestore.getInstance()

        firestore.clearPersistence()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()

        firestore.firestoreSettings = settings

        return firestore
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        firebaseFireStore: FirebaseFirestore,
        db: GoldDatabase,
        sh: PreferencesManager
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            firebaseFireStore = firebaseFireStore,
            dao = db.productDao,
            sh = sh
        )
    }

}