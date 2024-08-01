package loc.example.composablenote72424app.model

import androidx.compose.ui.graphics.Color

data class Note(
  val id: Int,
  val title: String,
  val body: String,
  val checkedOff: Boolean,
  val pickedColor: Color
)