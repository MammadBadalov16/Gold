package az.mb.gold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import az.mb.gold.presentation.products.ProductScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductScreen()
        }
    }
}




