package loc.example.composablenote72424app.vm

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import loc.example.composablenote72424app.model.Note
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor() : ViewModel() {
  private val _uiState = MutableStateFlow(NoteUiState())
  val uiState = _uiState.asStateFlow()
  val noteColors by lazy {
    listOf(
        Color.Blue,
        Color.Cyan,
        Color.DarkGray,
        Color.Gray,
        Color.Green,
        Color.LightGray,
        Color.Magenta,
        Color.White,
        Color.Yellow
    )
  }

  init {
    val notes = (1..20).map {
      Note(
          id = it, title = "Title # $it", body = "Body $it"
      )
    }.toList()
    _uiState.update {
      NoteUiState(notes = notes)
    }
  }

  fun getNoteById(id: Int) = _uiState.value.notes.firstOrNull { it.id == id }

  fun updateNoteTitle(id: Int, title: String) {
    _uiState.update { state ->
      state.copy(notes = state.notes.map { if (it.id == id) it.copy(title = title) else it })
    }
  }

  fun updateNoteBody(id: Int, body: String) {
    _uiState.update { state ->
      state.copy(notes = state.notes.map { if (it.id == id) it.copy(body = body) else it })
    }
  }

  fun updateCanBeCheckedOff(id: Int, canBeCheckedOff: Boolean) {
    _uiState.update { state ->
      state.copy(notes = state.notes.map { if (it.id == id) it.copy(canBeCheckedOff = canBeCheckedOff) else it })
    }
  }

  fun updateNoteColor(id: Int, color: Color) {
    _uiState.update { state ->
      state.copy(notes = state.notes.map { if (it.id == id) it.copy(color = color) else it })
    }
  }
}

data class NoteUiState(
  val notes: List<Note> = emptyList(), val selectedNote: Note? = null
)