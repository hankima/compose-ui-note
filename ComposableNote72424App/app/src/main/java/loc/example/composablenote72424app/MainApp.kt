package loc.example.composablenote72424app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import loc.example.composablenote72424app.model.Route
import loc.example.composablenote72424app.screen.NoteListScreen
import loc.example.composablenote72424app.screen.NoteSaveScreen
import loc.example.composablenote72424app.ui.theme.ComposableNote72424AppTheme
import loc.example.composablenote72424app.vm.NoteListViewModel

@Composable
fun MainApp(modifier: Modifier = Modifier) {
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  ComposableNote72424AppTheme {
    ModalNavigationDrawer(
        drawerContent = { DrawerContent() },
        modifier = modifier,
        drawerState = drawerState
    ) {
      MainScreen(drawerState)
    }
  }
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
  ModalDrawerSheet(modifier = modifier) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(imageVector = Icons.Default.Menu, contentDescription = null)
      Text(text = stringResource(id = R.string.app_name), modifier = Modifier.padding(16.dp))
    }
    HorizontalDivider()
    NavigationDrawerItem(
        label = {
          Row {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(id = R.string.home)
            )
            Text(
                text = stringResource(id = R.string.notes),
                modifier = Modifier.padding(start = 16.dp)
            )
          }
        },
        selected = false,
        onClick = { /*TODO*/ }
    )
    NavigationDrawerItem(
        label = {
          Row {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.trash)
            )
            Text(
                text = stringResource(id = R.string.trash),
                modifier = Modifier.padding(start = 16.dp)
            )
          }
        },
        selected = false,
        onClick = { /*TODO*/ }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  drawerState: DrawerState,
  modifier: Modifier = Modifier,
  model: NoteListViewModel = viewModel()
) {
  val corScope = rememberCoroutineScope()
  @StringRes var appBarTitleRes by remember { mutableIntStateOf(R.string.app_name) }
  val navCtrl = rememberNavController()
  val navGraph = remember(navCtrl) {
    navCtrl.createGraph(startDestination = Route.NOTE_LIST.path) {
      composable(route = Route.NOTE_LIST.path) {
        appBarTitleRes = R.string.app_name
        NoteListScreen(model = model, onNoteClick = {
          navCtrl.navigate("${Route.NOTE_SAVE.path}/${it.id}")
        })
      }
      composable(
          route = "${Route.NOTE_SAVE.path}/{noteId}",
          arguments = listOf(navArgument(name = "noteId") { type = NavType.IntType })
      ) {
        appBarTitleRes = R.string.save_note
        val noteId = it.arguments?.getInt("noteId") ?: 0
        Log.d(TAG, "MainScreen: note id: $noteId")
        val note = model.getNoteById(id = noteId)
        NoteSaveScreen(note = note, noteColors = model.noteColors)
      }
    }
  }
  Scaffold(
      modifier = modifier,
      topBar = {
        TopAppBar(
            title = {
              Text(
                  text = stringResource(id = appBarTitleRes),
                  modifier = Modifier.padding(start = 16.dp)
              )
            },
            navigationIcon = {
              IconButton(onClick = {
                corScope.launch {
                  if (drawerState.isClosed) drawerState.open() else drawerState.close()
                }
              }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null
                )
              }
            },
            colors = TopAppBarDefaults.topAppBarColors()
              .copy(
                  containerColor = MaterialTheme.colorScheme.primary,
                  navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                  titleContentColor = MaterialTheme.colorScheme.onPrimary
              )
        )
      },
      floatingActionButton = {
        val backStackEntry = navCtrl.currentBackStackEntry
        if (backStackEntry == null || backStackEntry.destination.route == Route.NOTE_LIST.path) {
          FloatingActionButton(onClick = {
            appBarTitleRes = Route.getAppBarTitleRes(route = Route.NOTE_SAVE)
            navCtrl.navigate(route = "${Route.NOTE_SAVE.path}/0")
          }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add_note)
            )
          }
        }
      }
  ) { innerPadding ->
    NavHost(
        navController = navCtrl,
        graph = navGraph,
        modifier = Modifier.padding(paddingValues = innerPadding)
    )
  }
}