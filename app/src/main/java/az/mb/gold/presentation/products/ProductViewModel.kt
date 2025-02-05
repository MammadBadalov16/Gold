package az.mb.gold.presentation.products

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.mb.gold.GoldApplication.Companion.context
import az.mb.gold.R
import az.mb.gold.common.Helper
import az.mb.gold.domain.model.Product
import az.mb.gold.domain.repository.NetworkRepository
import az.mb.gold.domain.use_case.product.ProductUseCase
import az.mb.gold.presentation.products.event.ProductEvent
import az.mb.gold.presentation.products.state.ProductsState
import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _stateProducts = MutableStateFlow(ProductsState())
    val stateProducts: StateFlow<ProductsState> = _stateProducts

    private val _pdfUri = MutableStateFlow<Uri?>(null)
    val pdfUri: StateFlow<Uri?> = _pdfUri


    init {
        syncProducts()
        getProducts(Product(), 0)
    }

    fun getProducts(filter: Product, filterOption: Int) {
        viewModelScope.launch {
            productUseCase.getProducts(filter, option = filterOption).collect { products ->
                val audit = Helper.calculateAudit(products)
                _stateProducts.value = ProductsState(products = products, audit = audit)
            }
        }
    }

    private fun addProduct(product: Product) {
        viewModelScope.launch {
            productUseCase.addProduct(product = product)
        }
    }

    private fun syncProducts() {
        viewModelScope.launch {
            networkRepository.syncProducts()
        }
    }


    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productUseCase.deleteProduct(product = product)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.DeleteProduct -> {
                deleteProduct(event.product)
            }

            is ProductEvent.SaveProduct -> {
                addProduct(product = event.product)
            }

            ProductEvent.SyncProduct -> {
                syncProducts()
            }

            is ProductEvent.CreatePdf -> {
                viewModelScope.launch {
                    createPdf(products = event.product)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private suspend fun createPdf(products: List<Product>) {
        withContext(Dispatchers.IO) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, "${Helper.getCurrentTimeDate()}.pdf")
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(
                    MediaStore.Downloads.RELATIVE_PATH,
                    Environment.DIRECTORY_DOWNLOADS
                )
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = PdfWriter(outputStream)
                    val pdfDocument = PdfDocument(writer)
                    val document = Document(pdfDocument)

                    val fontBytes = context.resources.openRawResource(R.raw.noto).readBytes()
                    val tempFontFile = File(context.cacheDir, "noto_temp.ttf").apply {
                        writeBytes(fontBytes)
                    }

                    val font = PdfFontFactory.createFont(
                        tempFontFile.absolutePath,
                        PdfEncodings.IDENTITY_H,
                        pdfDocument
                    )

                    val columnNames = listOf(
                        "Nömrə",
                        "Alış tarixi",
                        "Satıcı",
                        "Adı",
                        "Çəkisi",
                        "Alış qiyməti",
                        "Satış qiyməti",
                        "Xeyir",
                        "Satış tarixi"
                    )
                    val columnWidths = floatArrayOf(3f, 3f, 3f, 3f, 2f, 2f, 2f, 2f, 3f)
                    val table = Table(columnWidths, true)

                    for (columnName in columnNames) {
                        table.addCell(
                            Cell().add(Paragraph(columnName).setFont(font))
                                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        )
                    }

                    for (product in products) {
                        table.addCell(Cell().add(Paragraph(product.productNumber).setFont(font)))
                        table.addCell(Cell().add(Paragraph(product.datePurchase).setFont(font)))
                        table.addCell(Cell().add(Paragraph(product.seller).setFont(font)))
                        table.addCell(Cell().add(Paragraph(product.productName).setFont(font)))
                        table.addCell(Cell().add(Paragraph(product.weight.toString()).setFont(font)))
                        table.addCell(
                            Cell().add(
                                Paragraph(product.purchasePrice.toString()).setFont(
                                    font
                                )
                            )
                        )
                        table.addCell(
                            Cell().add(
                                Paragraph(product.salePrice.toString()).setFont(
                                    font
                                )
                            )
                        )
                        table.addCell(Cell().add(Paragraph(product.profit.toString()).setFont(font)))
                        table.addCell(Cell().add(Paragraph(product.dateSale).setFont(font)))
                    }

                    document.add(table)
                    document.close()
                }

                _pdfUri.value = uri
            }
        }
    }
}

fun openPdf(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "PDF açmaq üçün tətbiq tapılmadı!", Toast.LENGTH_LONG).show()
    }
}

