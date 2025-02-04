package az.mb.gold.presentation.products.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SwitchButton(sold: Boolean, isChecked: (Boolean) -> Unit) {
    val isCheckedBoolean = remember { mutableStateOf(sold) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(if (isCheckedBoolean.value) "Satıldı" else "Satılmayıb")

        Switch(
            checked = isCheckedBoolean.value,
            onCheckedChange = {
                isCheckedBoolean.value = it
                isChecked(it)
            }
        )
    }
}

