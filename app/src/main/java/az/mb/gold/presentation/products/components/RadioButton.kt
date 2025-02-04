package az.mb.gold.presentation.products.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtons(onOptionSelected: MutableIntState) {
    val options = listOf("Hamısı", "Satılan", "Satılmayan")
    Column {
        options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOptionSelected.intValue = index
                    }
            ) {
                RadioButton(
                    selected = (index == onOptionSelected.intValue),
                    onClick = {
                        onOptionSelected.intValue = index
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = option, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
