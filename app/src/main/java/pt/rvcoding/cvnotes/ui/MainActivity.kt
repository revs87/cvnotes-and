package pt.rvcoding.cvnotes.ui

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.asLongState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import pt.rvcoding.cvnotes.theme.Black
import pt.rvcoding.cvnotes.theme.Blue500
import pt.rvcoding.cvnotes.theme.Blue500_Background1
import pt.rvcoding.cvnotes.theme.Green500
import pt.rvcoding.cvnotes.theme.Green500_Background3
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.White
import pt.rvcoding.cvnotes.ui.about.AboutScreen
import pt.rvcoding.cvnotes.ui.about.AboutViewModel
import pt.rvcoding.cvnotes.ui.auth.AuthViewModel
import pt.rvcoding.cvnotes.ui.auth.IntroScreen
import pt.rvcoding.cvnotes.ui.auth.LoginScreen
import pt.rvcoding.cvnotes.ui.auth.RegistrationScreen
import pt.rvcoding.cvnotes.ui.dashboard.DashboardScreen
import pt.rvcoding.cvnotes.ui.dashboard.DashboardViewModel
import pt.rvcoding.cvnotes.ui.editnote.EditNoteScreen
import pt.rvcoding.cvnotes.ui.editnote.EditNoteViewModel
import pt.rvcoding.cvnotes.ui.home.HomeViewModel
import pt.rvcoding.cvnotes.ui.section_details.SectionDetailsScreen
import pt.rvcoding.cvnotes.ui.section_details.SectionDetailsViewModel
import pt.rvcoding.cvnotes.ui.splash.SplashScreen
import pt.rvcoding.cvnotes.ui.util.Screen.About
import pt.rvcoding.cvnotes.ui.util.Screen.Auth
import pt.rvcoding.cvnotes.ui.util.Screen.Dashboard
import pt.rvcoding.cvnotes.ui.util.Screen.EditNote
import pt.rvcoding.cvnotes.ui.util.Screen.Home
import pt.rvcoding.cvnotes.ui.util.Screen.Intro
import pt.rvcoding.cvnotes.ui.util.Screen.Login
import pt.rvcoding.cvnotes.ui.util.Screen.NewNote
import pt.rvcoding.cvnotes.ui.util.Screen.Register
import pt.rvcoding.cvnotes.ui.util.Screen.SectionDetails
import pt.rvcoding.cvnotes.ui.util.Screen.Splash
import pt.rvcoding.cvnotes.ui.util.component.AddSectionBottomSheet
import pt.rvcoding.cvnotes.ui.util.component.BottomBarWithFab
import pt.rvcoding.cvnotes.ui.util.component.TextFieldDialog
import pt.rvcoding.cvnotes.ui.util.component.UnselectDeleteSectionsBottomSheet
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()
            MyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Splash.route
                    ) {
                        composable(route = Splash.route) {
                            systemUiController.setSystemBarsColor(Black, White)
                            val authViewModel: AuthViewModel = hiltViewModel()
                            val startingRoute =
                                if (authViewModel.state.value.isLoggedIn) { Home.route }
                                else { Auth.route }
                            SplashScreen { navigateTo(navController, startingRoute, Splash.route) }
                        }
                        navigation(
                            route = Auth.route,
                            startDestination = Intro.route
                        ) {
                            composable(
                                route = Intro.route
                            ) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                IntroScreen(
                                    { authViewModel.cleanFields(); navigateTo(navController, Register.route) },
                                    { authViewModel.cleanFields(); navigateTo(navController, Login.route)  },
                                    { authViewModel.createUser("", "", isOffline = true) }
                                )
                            }
                            composable(route = Register.route) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                val errorEvent = authViewModel.errors.collectAsStateWithLifecycle(initialValue = "")
                                RegistrationScreen(
                                    state = authViewModel.state.value,
                                    fieldsState = authViewModel.fieldsState.value,
                                    errorMessage = errorEvent.value,
                                    updateEmailListener = { authViewModel.updateEmail(it) },
                                    updatePwdListener = { authViewModel.updatePwd(it) },
                                    createUserListener = { email, pwd -> authViewModel.createUser(email, pwd) },
                                )
                            }
                            composable(route = Login.route) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                val errorEvent = authViewModel.errors.collectAsStateWithLifecycle(initialValue = "")
                                LoginScreen(
                                    state = authViewModel.state.value,
                                    fieldsState = authViewModel.fieldsState.value,
                                    errorMessage = errorEvent.value,
                                    updateEmailListener = { authViewModel.updateEmail(it) },
                                    updatePwdListener = { authViewModel.updatePwd(it) },
                                    logUserListener = { email, pwd -> authViewModel.logUser(email, pwd) },
                                )
                            }
                        }
                        navigation(
                            route = Home.route,
                            startDestination = Dashboard.route
                        ) {
                            composable(route = Dashboard.route) {
                                systemUiController.setSystemBarsColor(Blue500, Blue500_Background1)
                                val homeViewModel: HomeViewModel = hiltViewModel()
                                val isFabVisible by remember { derivedStateOf { homeViewModel.state.value.selectedBottomItem == 0 } }
                                val dashboardViewModel: DashboardViewModel = hiltViewModel()
                                val sectionsWithNotes by dashboardViewModel.state.value.sectionsWithNotes.collectAsStateWithLifecycle(initialValue = emptyList())
                                val hasSelectedSections by dashboardViewModel.state.value.sectionsHasSelected.collectAsStateWithLifecycle(initialValue = false)
                                LaunchedEffect(dashboardViewModel.state) { dashboardViewModel.getAllNotes() }
                                val aboutViewModel: AboutViewModel = hiltViewModel()
                                var newSectionBottomSheetVisible by remember { mutableStateOf(false) }
                                var withSelectedSectionsBottomSheetVisible by remember { mutableStateOf(false) }
                                var newSectionNameDialogVisible by remember { mutableStateOf(false) }
                                var pdfExportDialogVisible by remember { mutableStateOf(false) }
                                val prefs by lazy { applicationContext.getSharedPreferences("ui_prefs", MODE_PRIVATE) }
                                val initialScrollPosition = prefs.getInt("scroll_position", 0)
                                val context = LocalContext.current
                                val launcher = rememberLauncherForActivityResult(
                                    ActivityResultContracts.RequestPermission()
                                ) { isGranted ->
                                    pdfExportDialogVisible = isGranted
                                }
                                BottomBarWithFab(
                                    bottomNavItems = listOf(
                                        Dashboard.apply {
                                            content = {
                                                DashboardScreen(
                                                    state = dashboardViewModel.state.value,
                                                    sectionsWithNotes = sectionsWithNotes,
                                                    hasSelectedSections = hasSelectedSections,
                                                    onSectionClick = { id ->
                                                        if (hasSelectedSections) { dashboardViewModel.selectSection(id) }
                                                        else { navigateTo(navController, "${SectionDetails.route}/$id") }
                                                    },
                                                    onSectionLongClick = { id -> dashboardViewModel.selectSection(id) },
                                                    saveToPrefs = { index -> prefs.edit().putInt("scroll_position", index).apply() },
                                                    initialScrollPosition = initialScrollPosition
                                                )
                                            }
                                        },
                                        About.apply {
                                            content = {
                                                AboutScreen(
                                                    state = aboutViewModel.state,
                                                    profileState = aboutViewModel.profileState,
                                                    logoutListener = aboutViewModel::logout
                                                ) { navigateTo(navController, Auth.route, Home.route) }
                                            }
                                        },
                                    ),
                                    bottomNavSelected = homeViewModel.state.value.selectedBottomItem,
                                    pageListener = { index -> homeViewModel.selectBottomNavPage(index) },
                                    smallFabClickListener = {
                                        requestPermission(context, launcher) { pdfExportDialogVisible = true }
                                    },
                                    fabClickListener = {
                                        when {
                                            hasSelectedSections -> withSelectedSectionsBottomSheetVisible = true
                                            else -> newSectionBottomSheetVisible = true
                                        }
                                    },
                                    fabIcon = when {
                                        hasSelectedSections -> Icons.Filled.DeleteSweep
                                        else -> Icons.Filled.Add
                                    },
                                    fabVisible = isFabVisible
                                )
                                AddSectionBottomSheet(
                                    bottomSheetVisible = newSectionBottomSheetVisible,
                                    onOtherClicked = { newSectionNameDialogVisible = true },
                                    onRuiVieiraClicked = { dashboardViewModel.addPersonalDataFromRuiVieira() }
                                ) { sectionType ->
                                    newSectionBottomSheetVisible = false
                                    dashboardViewModel.addSection(sectionType)
                                }
                                UnselectDeleteSectionsBottomSheet(
                                    bottomSheetVisible = withSelectedSectionsBottomSheetVisible,
                                    unselectAllSelected = dashboardViewModel::unselectAllSelectedSections,
                                    deleteAllSelected = dashboardViewModel::deleteSelectedSections,
                                ) { withSelectedSectionsBottomSheetVisible = false }
                                if (newSectionNameDialogVisible) {
                                    TextFieldDialog(
                                        title = "New section name",
                                        placeholder = "Enter name",
                                        initialValue = "",
                                        setShowDialog = { enabled -> newSectionNameDialogVisible = enabled },
                                        setValue = { newValue -> dashboardViewModel.addSectionOtherType(newValue) }
                                    )
                                }
                                if (pdfExportDialogVisible) {
                                    TextFieldDialog(
                                        title = "Export as PDF",
                                        placeholder = "Enter file name",
                                        initialValue = "myCVNotes",
                                        setShowDialog = { enabled -> pdfExportDialogVisible = enabled },
                                        setValue = { newValue -> dashboardViewModel.exportPdfWithName(context, newValue) }
                                    )
                                }
                            }
                            composable(
                                route = "${SectionDetails.route}/{sectionId}",
                                arguments = listOf(navArgument("sectionId") { type = NavType.IntType })
                            ) {
                                systemUiController.setSystemBarsColor(Blue500, Blue500_Background1)
                                val viewModel = it.sharedViewModel<SectionDetailsViewModel>(navController = navController)
                                val sectionIdState = remember { mutableIntStateOf(it.arguments?.getInt("sectionId") ?: 0) }.asIntState()
                                LaunchedEffect(sectionIdState) { viewModel.getSection(sectionIdState.intValue) }
                                val notes by viewModel.state.value.notes.collectAsStateWithLifecycle(initialValue = emptyList())
                                val hasSelectedNotes by viewModel.state.value.hasSelectedNotes.collectAsStateWithLifecycle(initialValue = false)
                                var withSelectedNotesBottomSheetVisible by remember { mutableStateOf(false) }
                                SectionDetailsScreen(
                                    state = viewModel.state.value,
                                    sectionNameEditState = viewModel.sectionNameEditState.value,
                                    editSectionNameTextListener = { nameChange -> viewModel.updateSectionNewNameState(nameChange) },
                                    addNoteListener = { navigateTo(navController, "${NewNote.route}/${sectionIdState.intValue}") },
                                    editSectionListener = { sectionId, newName -> viewModel.updateSection(sectionId, newName) },
                                    editNoteListener = { noteId -> navigateTo(navController, "${EditNote.route}/${sectionIdState.intValue}/$noteId") },
                                    selectNoteListener = { note -> viewModel.toggleNoteSelection(note) },
                                    notes = notes,
                                    hasSelectedNotes = hasSelectedNotes,
                                    onSelectedNotesFABClick = { withSelectedNotesBottomSheetVisible = true},
                                    onBackPressed = { navController.navigateUp() }
                                )
                                UnselectDeleteSectionsBottomSheet(
                                    bottomSheetVisible = withSelectedNotesBottomSheetVisible,
                                    unselectAllSelected = { viewModel.unselectAllSelectedNotes(sectionIdState.intValue) },
                                    deleteAllSelected = { viewModel.deleteSelectedNotes(sectionIdState.intValue) },
                                ) { withSelectedNotesBottomSheetVisible = false }
                            }
                            composable(
                                route = "${NewNote.route}/{sectionId}",
                                arguments = listOf(navArgument("sectionId") { type = NavType.IntType })
                            ) {
                                systemUiController.setSystemBarsColor(Green500, Green500_Background3)
                                val sdViewModel = it.sharedViewModel<SectionDetailsViewModel>(navController = navController)
                                val viewModel: EditNoteViewModel = hiltViewModel()
                                val sectionIdState = remember { mutableIntStateOf(it.arguments?.getInt("sectionId") ?: 0) }.asIntState()
                                LaunchedEffect(sectionIdState) { viewModel.setSectionId(sectionIdState.intValue) }
                                EditNoteScreen(
                                    state = viewModel.state.value,
                                    navBarTitle1 = sdViewModel.sectionNameEditState.value,
                                    navBarTitle2 = NewNote.title,
                                    sectionNameEditState = sdViewModel.sectionNameEditState.value,
                                    editSectionNameTextListener = { nameChange -> sdViewModel.updateSectionNewNameState(nameChange) },
                                    editSectionListener = { sectionId, newName -> sdViewModel.updateSection(sectionId, newName) },
                                    isNoteValid = (viewModel::isValid)(viewModel.state.value.note),
                                    updateContent1 = { note, newText -> viewModel.updateStateNode(note, newText, true) },
                                    updateContent2 = { note, newText -> viewModel.updateStateNode(note, newText, false) },
                                    savePartialListener = { note -> viewModel.saveStatePartialNote(note) },
                                    saveNoteListener = { note -> viewModel.addNote(note) },
                                    onBackPressed = { navController.navigateUp() }
                                )
                            }
                            composable(
                                route = "${EditNote.route}/{sectionId}/{noteId}",
                                arguments = listOf(
                                    navArgument("sectionId") { type = NavType.IntType },
                                    navArgument("noteId") { type = NavType.LongType }
                                )
                            ) {
                                systemUiController.setSystemBarsColor(Green500, Green500_Background3)
                                val sdViewModel = it.sharedViewModel<SectionDetailsViewModel>(navController = navController)
                                val viewModel: EditNoteViewModel = hiltViewModel()
                                val sectionIdState = remember { mutableIntStateOf(it.arguments?.getInt("sectionId") ?: 0) }.asIntState()
                                val noteIdState = remember { mutableLongStateOf(it.arguments?.getLong("noteId") ?: 0L) }.asLongState()
                                LaunchedEffect(noteIdState) { viewModel.getNote(sectionIdState.intValue, noteIdState.longValue) }
                                EditNoteScreen(
                                    state = viewModel.state.value,
                                    navBarTitle1 = sdViewModel.sectionNameEditState.value,
                                    navBarTitle2 = EditNote.title,
                                    sectionNameEditState = sdViewModel.sectionNameEditState.value,
                                    editSectionNameTextListener = { nameChange -> sdViewModel.updateSectionNewNameState(nameChange) },
                                    editSectionListener = { sectionId, newName -> sdViewModel.updateSection(sectionId, newName) },
                                    isNoteValid = (viewModel::isValid)(viewModel.state.value.note),
                                    updateContent1 = { note, newText -> viewModel.updateStateNode(note, newText, true) },
                                    updateContent2 = { note, newText -> viewModel.updateStateNode(note, newText, false) },
                                    savePartialListener = { note -> viewModel.saveStatePartialNote(note) },
                                    saveNoteListener = { note -> viewModel.addNote(note) },
                                    onBackPressed = { navController.navigateUp() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateTo(
        navController: NavHostController,
        route: String,
        popUpTo: String = ""
    ) {
        navController.navigate(route = route) {
            if (popUpTo.isNotEmpty()) {
                popUpTo(popUpTo) {
                    inclusive = true
                }
            }
        }
    }

    private fun requestPermission(
        context: Context,
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        onGranted: () -> Unit
    ) {
        /**
         * From Android 13 onwards, READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE are deprecated.
         * If the app only adds files in the shared storage, it can stop requesting any permission on Android 10+
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { onGranted.invoke() }
        else {
            when (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                true -> onGranted.invoke()
                false -> launcher.launch(WRITE_EXTERNAL_STORAGE)
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

@Composable
fun Greeting(name: String) {
    CVNText(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        Greeting("Android")
    }
}

fun SystemUiController.setSystemBarsColor(
    statusBarColor: Color = Blue500,
    navigationBarColor: Color = Blue500_Background1
) {
    this.setStatusBarColor(color = statusBarColor)
    this.setNavigationBarColor(color = navigationBarColor)
}