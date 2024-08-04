package loc.example.composablenote72424app.model

import androidx.compose.ui.graphics.Color

data class Note(
  val id: Int,
  val title: String,
  val body: String,
  val canBeCheckedOff: Boolean = false,
  val color: Color = Color.White,
  var isTrashed: Boolean = false
)