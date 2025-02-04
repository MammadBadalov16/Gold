package az.mb.gold.common

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Constants {

    const val DATABASE_NAME = "gold_database"
    const val LAST_SYNC_TIME = "lastSyncTime"
    const val CHECK_INTERNET_CONNECTION = "İnternet bağlantınızı yoxlayın !"
}
object FireStoreCollection {
    const val PAGE_SIZE = 2L
    val PRODUCTS = "product"
}
