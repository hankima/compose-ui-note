package loc.example.composablenote72424app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import loc.example.composablenote72424app.model.Note
import loc.example.composablenote72424app.vm.NoteListViewModel

@Composable
fun NoteListScreen(
    model: NoteListViewModel,
    onNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    NoteList(model = model, onNoteClick = onNoteClick, modifier = modifier)
}

@Composable
fun NoteList(model: NoteListViewModel, onNoteClick: (Note) -> Unit, modifier: Modifier = Modifier) {
    val uiState by model.uiState.collectAsState()
    val items = uiState.notes
    LazyColumn(modifier = modifier) {
        items(items = items) {
            NoteItem(item = it, onNoteClick = { note ->
                onNoteClick(note)
            })
        }
    }
}

@Composable
fun NoteItem(item: Note, onNoteClick: (Note) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
          .fillMaxSize()
          .clickable { onNoteClick(item) }
          .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
              .size(size = 48.dp)
              .clip(shape = CircleShape)
              .background(color = item.color, shape = CircleShape)
              .border(width = 1.dp, color = Color.Black, shape = CircleShape)
        )
        Column(modifier = Modifier.padding(start = 24.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W400)
            )
            Text(
                text = item.body,
                modifier = Modifier.padding(top = 2.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        }
    }
    HorizontalDivider()
    Column {
    }
}

//@Preview
//@Composable
//private fun NoteItemPrev() {
//  NoteItem()
//}