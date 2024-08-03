package loc.example.composablenote72424app

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val navCtrl = rememberNavController()
    ComposableNote72424AppTheme {
        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(
                    onHomeClick = {
                        navCtrl.navigate(Route.NOTE_LIST.path)
                    },
                    onTrashClick = {
                        navCtrl.navigate(Route.NOTE_TRASH.path)
                    }
                )
            },
            modifier = modifier,
            drawerState = drawerState
        ) {
            MainScreen(navCtrl = navCtrl, drawerState = drawerState)
        }
    }
}

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit,
    onTrashClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed) { true }
    val corScope = rememberCoroutineScope()
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
            onClick = {
                corScope.launch { drawerState.close() }
                onHomeClick()
            }
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
            onClick = onTrashClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navCtrl: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    model: NoteListViewModel = viewModel()
) {
    val corScope = rememberCoroutineScope()
    val navBackStack by navCtrl.currentBackStackEntryAsState()
    val navGraph = remember(navCtrl) {
        navCtrl.createGraph(startDestination = Route.NOTE_LIST.path) {
            composable(route = Route.NOTE_LIST.path) {
                NoteListScreen(model = model, onNoteClick = {
                    navCtrl.navigate("${Route.NOTE_SAVE.path}/${it.id}")
                })
            }
            composable(
                route = "${Route.NOTE_SAVE.path}/{noteId}",
                arguments = listOf(navArgument(name = "noteId") { type = NavType.IntType })
            ) {
                val noteId = it.arguments?.getInt("noteId") ?: 0
                Log.d(TAG, "MainScreen: note id: $noteId")
                val note = model.getNoteById(id = noteId)
                NoteSaveScreen(
                    title = note?.title.orEmpty(),
                    body = note?.body.orEmpty(),
                    color = note?.color ?: Color.White,
                    canBeCheckedOff = note?.canBeCheckedOff ?: false,
                    noteColors = model.noteColors,
                    onTitleChange = {
                        model.updateNoteTitle(id = noteId, title = it)
                    },
                    onBodyChange = {
                        model.updateNoteBody(id = noteId, body = it)
                    },
                    onCanBeCheckedOff = {
                        model.updateCanBeCheckedOff(id = noteId, canBeCheckedOff = it)
                    },
                    onColorSelect = {
                        model.updateNoteColor(id = noteId, color = it)
                    })
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    val strRes =
                        navBackStack?.destination?.route?.takeIf { it.contains(Route.NOTE_LIST.path) }
                            ?.let {
                                R.string.app_name
                            } ?: R.string.save_note
                    Text(
                        text = stringResource(id = strRes), // appBarTitleRes),
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
                actions = {
                    navBackStack?.destination?.route?.takeIf {
                        it.contains(
                            other = Route.NOTE_SAVE.path,
                            ignoreCase = true
                        )
                    }?.let {
                        Row(
                            modifier = Modifier.width(width = 152.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_palette_24),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
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
            navBackStack?.destination?.route?.takeIf {
                it.contains(
                    other = Route.NOTE_LIST.path,
                    ignoreCase = true
                )
            }?.let {
                FloatingActionButton(onClick = {
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