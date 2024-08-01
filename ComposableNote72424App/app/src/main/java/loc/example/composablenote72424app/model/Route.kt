package loc.example.composablenote72424app.model

import androidx.annotation.StringRes
import loc.example.composablenote72424app.R

enum class Route(val path: String) {
  NOTE_LIST(path = "note_list"),
  NOTE_SAVE(path = "note_save");

  companion object {
    @StringRes
    fun getAppBarTitleRes(route: Route) = when (route) {
      NOTE_LIST -> R.string.app_name
      NOTE_SAVE -> R.string.save_note
    }
  }
}