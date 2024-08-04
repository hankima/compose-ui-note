package loc.example.composablenote72424app.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import loc.example.composablenote72424app.comp.NoteList
import loc.example.composablenote72424app.model.Note
import loc.example.composablenote72424app.vm.NoteListViewModel

@Composable
fun NoteListScreen(
  model: NoteListViewModel,
  onNoteClick: (Note) -> Unit,
  modifier: Modifier = Modifier
) {
  val uiState by model.uiState.collectAsState()
  val items = uiState.notes
  NoteList(items = items, onNoteClick = onNoteClick, modifier = modifier)
}

//@Preview
//@Composable
//private fun NoteItemPrev() {
//  NoteItem()
//}