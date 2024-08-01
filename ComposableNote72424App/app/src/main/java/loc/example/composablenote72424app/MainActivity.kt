package loc.example.composablenote72424app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MainApp()
    }
  }
}

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ComposableNote72424AppTheme {
    Greeting("Android")
  }
}
*/