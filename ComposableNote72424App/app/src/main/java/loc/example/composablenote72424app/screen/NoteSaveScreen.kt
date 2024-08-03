package loc.example.composablenote72424app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import loc.example.composablenote72424app.R

@Composable
fun NoteSaveScreen(
  title: String,
  body: String,
  canBeCheckedOff: Boolean,
  color: Color,
  noteColors: List<Color>,
  onTitleChange: (String) -> Unit,
  onBodyChange: (String) -> Unit,
  onCanBeCheckedOff: (Boolean) -> Unit,
  onColorSelect: (Color) -> Unit,
  modifier: Modifier = Modifier
) {
  var showDialog by remember { mutableStateOf(false) }
  var titleState by remember { mutableStateOf(title) }
  var bodyState by remember { mutableStateOf(body) }
  var canBeCheckedOffState by remember { mutableStateOf(canBeCheckedOff) }
  var colorState by remember { mutableStateOf(color) }
  Column(modifier = modifier.fillMaxSize()) {
    Column(
        modifier = modifier
          .fillMaxSize()
          .padding(horizontal = 8.dp)
    ) {
      TextField(
          value = titleState,
          onValueChange = {
            titleState = it
            onTitleChange(it)
          },
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
          label = { Text(text = stringResource(R.string.title)) },
          placeholder = { Text(text = stringResource(R.string.enter_note_title_here)) })
      TextField(
          value = bodyState,
          onValueChange = {
            bodyState = it
            onBodyChange(it)
          },
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
          label = { Text(text = stringResource(R.string.body)) },
          placeholder = { Text(text = stringResource(R.string.enter_note_body_here)) })
      Row(
          modifier = Modifier.padding(vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
            text = stringResource(R.string.can_note_be_checked_off),
            modifier = Modifier.weight(weight = 6f)
        )
        Switch(
            checked = canBeCheckedOffState,
            onCheckedChange = {
              canBeCheckedOffState = it
              onCanBeCheckedOff(it)
            }
        )
      }
      Row(
          modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
            text = stringResource(R.string.picked_color),
            modifier = Modifier.fillMaxWidth(fraction = .9f)
        )
        Box(
            modifier = Modifier
              .size(size = 32.dp)
              .clip(shape = CircleShape)
              .background(color = colorState, shape = CircleShape)
              .border(width = 1.dp, color = Color.Black, shape = CircleShape)
              .clickable { showDialog = true }
        )
      }
      if (showDialog) {
        ColorPicker(noteColors = noteColors, onDismiss = {
          showDialog = false
        }, onColorSelect = {
          colorState = it
          onColorSelect(it)
        })
      }
    }
  }
}

@Composable
fun ColorPicker(
  noteColors: List<Color>,
  onDismiss: () -> Unit,
  onColorSelect: (Color) -> Unit,
  modifier: Modifier = Modifier
) {
  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        Button(onClick = onDismiss) {
          Text(text = "Close")
        }
      },
      modifier = modifier,
      title = { Text(text = "Pick a color") },
      text = {
        val cells = GridCells.Fixed(count = 2)
        LazyHorizontalGrid(
            rows = cells,
            modifier = Modifier.height(height = 140.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
          items(noteColors) {
            Box(
                modifier = Modifier
                  .size(size = 64.dp)
                  .clip(shape = CircleShape)
                  .background(color = it)
                  .clickable {
                    onColorSelect(it)
                  }
            )
          }
        }
      }
  )
}

@Preview
@Composable
private fun NoteSaveScreenPrev() {
  NoteSaveScreen(
      title = "Note title",
      body = "Note body..",
      canBeCheckedOff = false,
      color = Color.Blue,
      noteColors = emptyList(),
      onTitleChange = {},
      onBodyChange = {},
      onCanBeCheckedOff = {},
      onColorSelect = {})
}

@Preview
@Composable
private fun ColorPickerPrev() {
  val colors = listOf(
      Color.Red,
      Color.Green,
      Color.Blue,
      Color.Yellow,
      Color.Gray,
      Color.Magenta,
      Color.Cyan
  )
  ColorPicker(noteColors = colors, onDismiss = {}, onColorSelect = {})
}
