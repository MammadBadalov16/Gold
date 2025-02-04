package az.mb.gold.presentation.products.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun SearchBox(
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onClickFilter: () -> Unit
) {
    var value by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    var filterValue by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLeadingIconColor = Color.Black,
                unfocusedLeadingIconColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            value = value,
            onValueChange = { value = it },
            label = { Text("Axtarış") },
            placeholder = { Text("Axtarış edin...") },
            shape = CutCornerShape(percent = 25),
            singleLine = true,
            trailingIcon = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (value.isNotEmpty()) {
                        IconButton(onClick = {
                            value = ""
                            onClear()
                        }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                    IconButton(onClick = {
                        onSearch(value)  // Axtarış funksiyasını çağır
                        keyboardController?.hide()  // Klaviaturanı bağla
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
            },
            modifier = Modifier.weight(1f), // TextField daha geniş yer tutacaq
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, // 'Done' düyməsi
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearch(value)  // Axtarış funksiyasını çağır
                    keyboardController?.hide()  // Klaviaturanı bağla
                }
            )
        )

        // Filter iconu yan tərəfdə
        IconButton(onClick = { onClickFilter() }) {
            Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filter")
        }
    }
}

