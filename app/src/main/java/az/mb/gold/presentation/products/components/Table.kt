package az.mb.gold.presentation.products.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.mb.gold.GoldApplication.Companion.context
import az.mb.gold.R
import az.mb.gold.domain.model.Product
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun Table(
    products: List<Product>,
    onRemove: (Product) -> Unit,
    onSave: (Product) -> Unit
) {
    val context = LocalContext.current
    val columnCount = 13
    val cellWidth = 100.dp
    val cellHeight = 50.dp

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    val columnNames = context.resources.getStringArray(R.array.column_names).toList()

    val showDialog = remember { mutableStateOf(false) }
    val showAddProductDialog = remember { mutableStateOf(false) }
    val showDeleteProductDialog = remember { mutableStateOf(false) }


    val productToEdit = remember { mutableStateOf<Product?>(null) }
    val productToDelete = remember { mutableStateOf<Product?>(null) }


    val newRowValues = remember { mutableStateListOf(*Array(columnCount) { "" }) }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddProductDialog.value = true },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Product") }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // .padding(16.dp)
                        .verticalScroll(verticalScrollState)
                        .horizontalScroll(horizontalScrollState)
                ) {

                    Column {
                        // Header Row
                        Row {
                            columnNames.forEach { columnName ->
                                TableHeaderCell(
                                    text = columnName,
                                    width = cellWidth,
                                    height = cellHeight
                                )
                            }
                        }


                        products.forEachIndexed { rowIndex, product ->
                            Row {
                                TableCellWithBorders(
                                    text = "${rowIndex + 1}",
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                )
                                TableCellWithBorders(
                                    text = product.productNumber,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                )
                                TableCellWithBorders(
                                    text = product.seller,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                )
                                TableCellWithBorders(
                                    text = product.productName,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                )
                                TableCellWithBorders(
                                    text = product.categoryName,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Category
                                TableCellWithBorders(
                                    text = product.weight.toString(),
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Weight
                                TableCellWithBorders(
                                    text = product.purchasePrice.toString(),
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Purchase price
                                TableCellWithBorders(
                                    text = product.salePrice.toString(),
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Sale price
                                TableCellWithBorders(
                                    text = product.profit.toString(),
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Profit
                                TableCellWithBorders(
                                    text = product.datePurchase,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Purchase date
                                TableCellWithBorders(
                                    text = product.dateSale,
                                    width = cellWidth,
                                    height = cellHeight,
                                    isSold = product.isSold
                                ) // Sale date

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(cellWidth, cellHeight)
                                        .border(1.dp, Color.Gray)
                                        .padding(4.dp)
                                ) {
                                    Row {

                                        // Update Icon Button
                                        IconButton(
                                            onClick = {
                                                productToEdit.value = product
                                                showDialog.value = true
                                            },
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Product",

                                                )
                                        }

                                        IconButton(
                                            onClick = {
                                                productToDelete.value = product
                                                showDeleteProductDialog.value = true
                                            },
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Product",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                }

                                val iconColor =
                                    if (product.firebaseStatus) Color.Green else Color.Red

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(cellWidth, cellHeight)
                                        .border(1.dp, Color.Gray)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Firebase Status",
                                        tint = iconColor,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(iconColor.copy(alpha = 0.2f))
                                            .padding(2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    if (showDialog.value && productToEdit.value != null) {
        EditProductDialog(
            header = "Düzəliş et",
            product = productToEdit.value!!,
            onDismiss = { showDialog.value = false },
            onSave = { updatedProduct ->
                updatedProduct.firebaseStatus = false
                onSave(updatedProduct)
                showDialog.value = false
            },
        )

    }

    if (showAddProductDialog.value) {
        EditProductDialog(
            header = "Yeni məhsul əlavə et",
            product = Product(),
            onDismiss = { showAddProductDialog.value = false },
            onSave = { saveProduct ->
                onSave(saveProduct)
                showAddProductDialog.value = false
            },
        )
    }

    AlertDeleteProduct(confirm = { onRemove(productToDelete.value!!) }, showDeleteProductDialog)

}

@Composable
fun TableHeaderCell(text: String, width: Dp, height: Dp) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width, height)
            .border(1.dp, Color.Black) // Hüceyrə xətti
            .background(Color.LightGray) // Başlıq fon rəngi
            .padding(4.dp)
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, // Mətnin kənara çıxmasının qarşısını almaq üçün
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ), // Başlıq mətni üslubu
            textAlign = TextAlign.Center // Mərkəzləşdirmək
        )
    }
}

@Composable
fun TableCellWithBorders(text: String, width: Dp, height: Dp, isSold: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width, height)
            .border(1.dp, Color.Gray)
            .background(if (isSold) Color(0x33FF0000) else Color.Transparent) // Solğun qırmızı rəng
            .padding(4.dp)
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(fontSize = 12.sp), // Mətn ölçüsü
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun EditProductDialog(
    header: String,
    product: Product,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit,
) {

    val fieldNames = context.resources.getStringArray(R.array.upsertDialogFields).toList()

    val editableProduct = remember { mutableStateOf(product) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateFieldIndex by remember { mutableIntStateOf(-1) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(header) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {


                SwitchButton(sold = editableProduct.value.isSold, isChecked = {
                    editableProduct.value =
                        editableProduct.value.copy(isSold = it)
                })

                fieldNames.forEachIndexed { index, columnName ->
                    val fieldValue = when (index) {
                        0 -> editableProduct.value.productNumber
                        1 -> editableProduct.value.seller
                        2 -> editableProduct.value.productName
                        3 -> editableProduct.value.categoryName
                        4 -> editableProduct.value.weight.toString()
                        5 -> editableProduct.value.purchasePrice.toString()
                        6 -> editableProduct.value.salePrice.toString()
                        7 -> editableProduct.value.profit.toString()
                        8 -> editableProduct.value.datePurchase
                        9 -> editableProduct.value.dateSale
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
                                    editableProduct.value.copy(productName = it)

                                3 -> editableProduct.value =
                                    editableProduct.value.copy(categoryName = it)

                                4 -> editableProduct.value =
                                    editableProduct.value.copy(weight = it.toDoubleOrNull() ?: 0.0)

                                5 -> {
                                    val purchasePrice = it.toDoubleOrNull() ?: 0
                                    editableProduct.value = editableProduct.value.copy(
                                        purchasePrice = purchasePrice.toInt(),
                                        profit = editableProduct.value.salePrice - purchasePrice.toInt()
                                    )
                                }

                                6 -> {
                                    val salePrice = it.toDoubleOrNull() ?: 0
                                    editableProduct.value = editableProduct.value.copy(
                                        salePrice = salePrice.toInt(),
                                        profit = salePrice.toInt() - editableProduct.value.purchasePrice
                                    )
                                }

                                /*8 -> {
                                    val profit = editableProduct.value.salePrice - editableProduct.value.purchasePrice
                                    editableProduct.value = editableProduct.value.copy()
                                }*/

                                8 -> editableProduct.value =
                                    editableProduct.value.copy(datePurchase = it)

                                9 -> editableProduct.value =
                                    editableProduct.value.copy(dateSale = it)
                            }
                        },
                        label = { Text(columnName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .let {
                                if (index == 8 || index == 9) {
                                    it.clickable {
                                        dateFieldIndex = index
                                        showDatePicker = true
                                    }
                                } else {
                                    it
                                }
                            },
                        enabled = index != 7 && index !in listOf(8, 9)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(editableProduct.value)
                onDismiss()
            }) {
                Text("Yadda Saxla")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Ləğv Et")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                if (dateFieldIndex == 8) {
                    editableProduct.value = editableProduct.value.copy(datePurchase = date)
                } else if (dateFieldIndex == 9) {
                    editableProduct.value = editableProduct.value.copy(dateSale = date)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Tarix seçin") },
        text = {
            DatePicker(state = datePickerState)
        },
        confirmButton = {
            Button(onClick = {
                if (datePickerState.selectedDateMillis != null) {
                    val date = Date(datePickerState.selectedDateMillis!!)
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val selectedDate = format.format(date)
                    onDateSelected(selectedDate)
                    onDismiss()
                }


            }) {
                Text("Təsdiqlə")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Ləğv et")
            }
        }
    )
}


@Composable
fun AlertDeleteProduct(confirm: () -> Unit, showDeleteProductDialog: MutableState<Boolean>) {


    if (showDeleteProductDialog.value)
        AlertDialog(
            onDismissRequest = { showDeleteProductDialog.value = false },
            title = {
                Text(text = "Təsdiq")
            },
            text = {
                Text(text = "Silmək istədiyinizdən əminsinizmi?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirm()
                        showDeleteProductDialog.value = false
                    }
                ) {
                    Text(text = "Bəli", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteProductDialog.value = false }
                ) {
                    Text(text = "Xeyr")
                }
            }
        )
}


