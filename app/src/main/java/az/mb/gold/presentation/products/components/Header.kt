package az.mb.gold.presentation.products.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import az.mb.gold.domain.model.Audit

@Composable
fun Header(
    audit: Audit,
    onSync: () -> Unit,
    createPdf: () -> Unit
) {
    val dialogCreatePdf = remember { mutableStateOf(false) }
    var dialogInfo by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = "Gold",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)

        )

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sync Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSync() }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.PictureAsPdf,
                contentDescription = "Pdf Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { dialogCreatePdf.value = true }
            )

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { dialogInfo = true }
            )
        }
    }

    if (dialogInfo) {
        AlertDialog(
            onDismissRequest = { dialogInfo = false },
            title = {
                Text(text = "Məlumatlar")
            },
            text = {
                Column {
                    Text(text = "Cəm çəki: ${audit.totalWeight} qram")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cəm alış qiyməti: ${audit.totalPurchasePrice} AZN")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cəm satış qiyməti: ${audit.totalSalePrice} AZN")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Cəm xeyir: ${audit.totalProfit} AZN")
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogInfo = false }) {
                    Text(text = "Bağla")
                }
            }
        )
    }


    DialogCreatePdf(confirm = { createPdf() }, dialogCreatePdf)

}


@Composable
fun DialogCreatePdf(confirm: () -> Unit, showCreatePdfDialog: MutableState<Boolean>) {

    if (showCreatePdfDialog.value)
        AlertDialog(
            onDismissRequest = { showCreatePdfDialog.value = false },
            title = {
                Text(text = "Pdf yarat")
            },
            text = {
                Text(text = "Siyahının pdf versiyasını yaratmaq üçün təsdiqləyin")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirm()
                        showCreatePdfDialog.value = false
                    }
                ) {
                    Text(text = "Təsdiq")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCreatePdfDialog.value = false }
                ) {
                    Text(text = "Ləğv et")
                }
            }
        )
}

