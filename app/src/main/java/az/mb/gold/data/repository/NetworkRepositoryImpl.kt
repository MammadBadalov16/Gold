package az.mb.gold.data.repository

import az.mb.gold.common.Constants.LAST_SYNC_TIME
import az.mb.gold.common.FireStoreCollection
import az.mb.gold.common.PreferencesManager
import az.mb.gold.common.Resource
import az.mb.gold.data.local.dao.ProductDao
import az.mb.gold.data.mapper.toProductDTO
import az.mb.gold.data.mapper.toProductEntity
import az.mb.gold.data.remote.ProductDTO
import az.mb.gold.domain.repository.NetworkRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class NetworkRepositoryImpl(
    private val firebaseFireStore: FirebaseFirestore,
    private val dao: ProductDao,
    private val sh: PreferencesManager
) : NetworkRepository {

    override suspend fun upsertProduct(product: ProductDTO): Resource<ProductDTO> {
        return try {
            withTimeout(3000) {
                firebaseFireStore.collection(FireStoreCollection.PRODUCTS)
                    .document(product.id)
                    .set(product, SetOptions.merge())
                    .await()
            }
            Timber.d("Product upsert successfully: $product")
            Resource.Success(product)
        } catch (e: Exception) {
            Timber.e("Error upsert product to FireStore: $e")
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun syncProducts() {
        val lastSyncTime = sh.getData(LAST_SYNC_TIME, 0L)
        val collectionRef = firebaseFireStore.collection(FireStoreCollection.PRODUCTS)

        collectionRef
            .whereGreaterThan(
                "updateAt",
                Timestamp(lastSyncTime / 1000, ((lastSyncTime % 1000) * 1000000).toInt())
            )
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.e("Error fetching updates: $e")
                    return@addSnapshotListener
                }

                for (documentChange in snapshots!!.documentChanges) {
                    //  val product = documentChange.document.toObject(ProductDTO::class.java)
                    val productDTO = documentChange.document.data.toMap().toProductDTO()
                    when (documentChange.type) {
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                            GlobalScope.launch(Dispatchers.IO) {
                                try {
                                    Timber.e("New or updated product: $productDTO")
                                    dao.upsertProduct(
                                        productDTO.toProductEntity(
                                            firebaseStatus = true,

                                        )
                                    )
                                    val updateAtServerTime =
                                        documentChange.document.getTimestamp("updateAt")
                                            ?.toDate()?.time
                                    updateAtServerTime?.let { sh.saveData(LAST_SYNC_TIME, it) }
                                } catch (ex: Exception) {
                                    Timber.e("Error while syncing product: $ex")
                                }
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            /*  GlobalScope.launch(Dispatchers.IO) {
                                try {
                                    Timber.e("Removed product: $product") // Silinmiş məhsul logunu yaz
                                    dao.deleteProduct(product.toProductEntity()) // Məhsulu sil
                                    val updateAtServerTime =
                                        documentChange.document.getTimestamp("updateAt")
                                            ?.toDate()?.time
                                    updateAtServerTime?.let {
                                        sh.saveData(
                                            LAST_SYNC_TIME,
                                            it
                                        )
                                    } // Son server vaxtını yenilə
                                } catch (ex: Exception) {
                                    Timber.e("Error while removing product: $ex")
                                }
                            }*/
                        }
                    }
                }
            }

        upsertProductFirebaseSync()
    }

    private suspend fun upsertProductFirebaseSync() {
        val productList = dao.getProductsNotInFirebase()
        productList.forEach { product ->
            try {
                product.firebaseStatus = true
                withTimeout(3000) {
                    val document = firebaseFireStore.collection(FireStoreCollection.PRODUCTS)
                        .document(product.id)
                    document.set(product, SetOptions.merge()).await()
                }
                dao.upsertProduct(product = product)
            } catch (e: Exception) {
                Timber.e("Firebase Add Product : ${e.message.toString()}")
                product.firebaseStatus = false
                dao.upsertProduct(product = product)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun fetchProductsPaginated(lastDocumentSnapshot: DocumentSnapshot? = null) {
        val lastSyncTime = sh.getData(LAST_SYNC_TIME, 0L)  // Get the last sync time
        Timber.e("Last Sync Time: $lastSyncTime")

        val collectionRef = firebaseFireStore.collection(FireStoreCollection.PRODUCTS)
            .whereGreaterThan(
                "updateAt",
                Timestamp(
                    lastSyncTime / 1000,
                    ((lastSyncTime % 1000) * 1000000).toInt()
                ) // Filter by updateAt > lastSyncTime
            )

        val query = if (lastDocumentSnapshot == null) {
            collectionRef.limit(FireStoreCollection.PAGE_SIZE)
        } else {
            collectionRef.startAfter(lastDocumentSnapshot).limit(FireStoreCollection.PAGE_SIZE)
        }

        query.get().addOnSuccessListener { snapshot ->
            val products = snapshot.documents.map { it.data?.toProductDTO() }
            val updateAtServerTime = snapshot.documents
                .mapNotNull { it.getTimestamp("updateAt")?.toDate()?.time }

            GlobalScope.launch(Dispatchers.IO) {
                products.forEach { product ->
                    Timber.e("PRODUCT : $product")
                }
            }
            Timber.e("------------------------------------")

            // Find the maximum updateAtServerTime value
            val maxUpdateAtServerTime = updateAtServerTime.maxOrNull() ?: 0L
            Timber.e("Max Update At Server Time: $maxUpdateAtServerTime")

            val lastVisible = snapshot.documents.lastOrNull()
            if (lastVisible != null && snapshot.isEmpty) {
                fetchProductsPaginated(lastVisible) // Fetch next page
            } else {
                // Check if maxUpdateAtServerTime is greater than the lastSyncTime
                if (maxUpdateAtServerTime > lastSyncTime) {
                    Timber.e("Updating LAST_SYNC_TIME to: $maxUpdateAtServerTime")
                    sh.saveData(LAST_SYNC_TIME, maxUpdateAtServerTime)
                }
            }
        }
    }


}