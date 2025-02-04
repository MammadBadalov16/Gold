package az.mb.gold.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import az.mb.gold.common.Constants.LAST_SYNC_TIME
import az.mb.gold.data.mapper.toProductDTO
import az.mb.gold.domain.model.Audit
import az.mb.gold.domain.model.Product
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Helper {

    fun calculateAudit(products: List<Product>): Audit {
        val totalWeight =
            products.sumOf { BigDecimal(it.weight.toString()) }.setScale(2, RoundingMode.HALF_EVEN)
        val totalSalePrice = products.sumOf { BigDecimal(it.salePrice.toString()) }
            .setScale(2, RoundingMode.HALF_EVEN)
        val totalPurchasePrice = products.sumOf { BigDecimal(it.purchasePrice.toString()) }
            .setScale(2, RoundingMode.HALF_EVEN)
        val totalProfit =
            products.sumOf { BigDecimal(it.profit.toString()) }.setScale(2, RoundingMode.HALF_EVEN)

        return Audit(
            totalWeight = totalWeight.toDouble(), // BigDecimal dəyəri Double-a çevrilir
            totalSalePrice = totalSalePrice.toDouble(),
            totalPurchasePrice = totalPurchasePrice.toDouble(),
            totalProfit = totalProfit.toDouble()
        )
    }


     fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                ))
    }

    fun getCurrentTimeDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy_HH-mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

}