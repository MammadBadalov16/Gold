package az.mb.gold.presentation.products

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.mb.gold.domain.model.Product
import az.mb.gold.presentation.products.components.FilterDialog
import az.mb.gold.presentation.products.components.Header
import az.mb.gold.presentation.products.components.SearchBox
import az.mb.gold.presentation.products.components.Table
import az.mb.gold.presentation.products.event.ProductEvent
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ProductScreen(viewModel: ProductViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val filteredProducts = remember { mutableStateOf(Product()) }
    val filterOption = remember { mutableIntStateOf(0) }
    val products = viewModel.stateProducts.collectAsState().value.products
    val audit = viewModel.stateProducts.collectAsState().value.audit
    val showFilterDialog = remember { mutableStateOf(false) }
    val pdfUri by viewModel.pdfUri.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    LaunchedEffect(pdfUri) {
        pdfUri?.let { uri ->
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "PDF yaradıldı!",
                    actionLabel = "Aç"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    openPdf(context, uri)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Header(audit,
                onSync = { viewModel.onEvent(ProductEvent.SyncProduct) },
                createPdf = {
                    viewModel.onEvent(ProductEvent.CreatePdf(products))
                })

            Spacer(modifier = Modifier.height(10.dp))

            SearchBox(
                onSearch = {},
                onClear = {},
                onClickFilter = { showFilterDialog.value = true })

            Spacer(modifier = Modifier.height(10.dp))

            Table(products = products,
                onSave = {
                    viewModel.onEvent(ProductEvent.SaveProduct(product = it))
                    viewModel.getProducts(
                        filter = filteredProducts.value,
                        filterOption = filterOption.intValue
                    )
                },
                onRemove = {
                    viewModel.onEvent(ProductEvent.DeleteProduct(product = it))
                    viewModel.getProducts(
                        filter = filteredProducts.value,
                        filterOption = filterOption.intValue
                    )
                })
        }
    }
    FilterDialog(
        showRangeDatePicker = showFilterDialog,
        onSearch = { product, option ->
            filteredProducts.value = product
            filterOption.intValue = option

            viewModel.getProducts(
                filter = filteredProducts.value,
                filterOption = filterOption.intValue
            )
        })
}
