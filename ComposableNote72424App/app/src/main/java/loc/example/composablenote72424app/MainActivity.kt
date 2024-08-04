package loc.example.composablenote72424app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import loc.example.composablenote72424app.ui.theme.ComposableNote72424AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()
    setContent {
      MainApp()
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ComposableNote72424AppTheme {
    MainApp()
  }
}