package az.mb.gold.presentation.products.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import az.mb.gold.R
import az.mb.gold.domain.model.Product


@Composable
fun FilterDialog(
    showRangeDatePicker: MutableState<Boolean>,
    onSearch: (Product, Int) -> Unit,
) {
    val product: Product = Product()
    val editableProduct = remember { mutableStateOf(product) }
    val filterOption = rememberSaveable { mutableIntStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateFieldIndex by remember { mutableIntStateOf(-1) }

    val context = LocalContext.current
    val columnNames = context.resources.getStringArray(R.array.filter_column_names)

    if (showRangeDatePicker.value) {
        AlertDialog(
            onDismissRequest = { showRangeDatePicker.value = false },
            title = { Text("Ətraflı axtarış") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

                    Button(modifier = Modifier.align(Alignment.End), onClick = {
                        editableProduct.value = Product()
                        filterOption.intValue = 0
                        onSearch(editableProduct.value, filterOption.intValue)
                        showRangeDatePicker.value = false
                    }) {
                        Text("Filteri sil")
                    }

                    RadioButtons(filterOption)

                    columnNames.forEachIndexed { index, columnName ->
                        val fieldValue = when (index) {
                            0 -> editableProduct.value.productNumber
                            1 -> editableProduct.value.seller
                            2 -> editableProduct.value.weight.toString()
                            3 -> editableProduct.value.datePurchase
                            4 -> editableProduct.value.dateSale
                            else -> ""
                        }

                        TextField(
                            value = fieldValue,
                            onValueChange = {
                                when (index) {
                                    0 -> editableProduct.value =
                                        editableProduct.value.copy(productNumber = it)

                                    1 -> editableProduct.value =
                                        editableProduct.value.copy(seller = it)

                                    2 -> editableProduct.value =
                                        editableProduct.value.copy(
                                            weight = it.toDoubleOrNull() ?: 0.0
                                        )

                                    3 -> editableProduct.value =
                                        editableProduct.value.copy(datePurchase = it)

                                    4 -> editableProduct.value =
                                        editableProduct.value.copy(dateSale = it)
                                }
                            },
                            label = { Text(columnName) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .let {
                                    if (index == 3 || index == 4) {
                                        it.clickable {
                                            dateFieldIndex = index
                                            showDatePicker = true
                                        }
                                    } else {
                                        it
                                    }
                                },
                            enabled = index !in listOf(3, 4)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    onSearch(editableProduct.value, filterOption.intValue)
                    showRangeDatePicker.value = false
                }) {
                    Text("Axtar")
                }
            },
            dismissButton = {
                Button(onClick = { showRangeDatePicker.value = false }) {
                    Text("Ləğv Et")
                }
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                if (dateFieldIndex == 3) {
                    editableProduct.value = editableProduct.value.copy(datePurchase = date)
                } else if (dateFieldIndex == 4) {
                    editableProduct.value = editableProduct.value.copy(dateSale = date)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}