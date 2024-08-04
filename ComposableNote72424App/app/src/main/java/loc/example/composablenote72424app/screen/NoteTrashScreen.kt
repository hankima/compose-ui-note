package loc.example.composablenote72424app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import loc.example.composablenote72424app.R
import loc.example.composablenote72424app.comp.NoteList
import loc.example.composablenote72424app.model.Note
import loc.example.composablenote72424app.vm.NoteListViewModel

@Composable
fun NoteTrashScreen(
  trashedNoteIds: List<Int>,
  clickableNoteIds: List<Int>,
  onNoteClick: (Note) -> Unit,
  modifier: Modifier = Modifier,
  model: NoteListViewModel = viewModel()
) {
  val trashedNotes = model.getNoteByIds(ids = trashedNoteIds)
  val clickableNotes = model.getNoteByIds(ids = clickableNoteIds)
  var selectedTabIdx by remember { mutableIntStateOf(0) }
  Column {
    TabRow(
        selectedTabIndex = selectedTabIdx,
        modifier = modifier,
        tabs = {
          Tab(
              selected = selectedTabIdx == 0,
              onClick = { selectedTabIdx = 0 },
              text = { Text(text = stringResource(R.string.regular)) })
          Tab(
              selected = selectedTabIdx == 1,
              onClick = { selectedTabIdx = 1 },
              text = { Text(text = stringResource(R.string.checkable)) })
        }
    )
    when (selectedTabIdx) {
      0 -> showNoteList(items = trashedNotes, onNoteClick = onNoteClick)
      1 -> showNoteList(items = clickableNotes, onNoteClick = onNoteClick)
    }
  }
}

@Composable
fun showNoteList(items: List<Note>, onNoteClick: (Note) -> Unit) {
  NoteList(items = items, onNoteClick = onNoteClick)
}