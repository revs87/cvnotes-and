
package pt.android.cvnotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import dagger.hilt.android.AndroidEntryPoint
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.about.AboutScreen
import pt.android.cvnotes.ui.about.AboutViewModel
import pt.android.cvnotes.ui.auth.AuthViewModel
import pt.android.cvnotes.ui.auth.IntroScreen
import pt.android.cvnotes.ui.auth.LoginScreen
import pt.android.cvnotes.ui.auth.RegistrationScreen
import pt.android.cvnotes.ui.dashboard.DashboardScreen
import pt.android.cvnotes.ui.dashboard.DashboardViewModel
import pt.android.cvnotes.ui.editnote.EditNoteScreen
import pt.android.cvnotes.ui.editnote.EditNoteViewModel
import pt.android.cvnotes.ui.home.HomeViewModel
import pt.android.cvnotes.ui.section_details.SectionDetailsScreen
import pt.android.cvnotes.ui.section_details.SectionDetailsViewModel
import pt.android.cvnotes.ui.splash.SplashScreen
import pt.android.cvnotes.ui.util.Screen.About
import pt.android.cvnotes.ui.util.Screen.Auth
import pt.android.cvnotes.ui.util.Screen.Dashboard
import pt.android.cvnotes.ui.util.Screen.EditNote
import pt.android.cvnotes.ui.util.Screen.Home
import pt.android.cvnotes.ui.util.Screen.Intro
import pt.android.cvnotes.ui.util.Screen.Login
import pt.android.cvnotes.ui.util.Screen.NewNote
import pt.android.cvnotes.ui.util.Screen.Register
import pt.android.cvnotes.ui.util.Screen.SectionDetails
import pt.android.cvnotes.ui.util.Screen.Splash
import pt.android.cvnotes.ui.util.component.AddSectionBottomSheet
import pt.android.cvnotes.ui.util.component.BottomBarWithFab
import pt.android.cvnotes.ui.util.component.UnselectDeleteSectionsBottomSheet

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

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
                                    { authViewModel.cleanFields(); navigateTo(navController, Login.route)  }
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
                                val homeViewModel = it.sharedViewModel<HomeViewModel>(navController = navController)
                                val dashboardViewModel: DashboardViewModel = hiltViewModel()
                                LaunchedEffect(dashboardViewModel.state) { dashboardViewModel.getAllNotes() }
                                val aboutViewModel: AboutViewModel = hiltViewModel()
                                var newSectionBottomSheetVisible by remember { mutableStateOf(false) }
                                var withSelectedSectionsBottomSheetVisible by remember { mutableStateOf(false) }
                                val hasSelectedSections by dashboardViewModel.state.value.sectionsHasSelected.collectAsStateWithLifecycle(initialValue = false)
                                println("hasSelectedSections: $hasSelectedSections")
                                val prefs by lazy { applicationContext.getSharedPreferences("ui_prefs", MODE_PRIVATE) }
                                val initialScrollPosition = prefs.getInt("scroll_position", 0)
                                BottomBarWithFab(
                                    bottomNavItems = listOf(
                                        Dashboard.apply {
                                            content = {
                                                DashboardScreen(
                                                    state = dashboardViewModel.state.value,
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
                                                    state = aboutViewModel.state.value,
                                                    profileState = aboutViewModel.profileState.value,
                                                    logoutListener = aboutViewModel::logout,
                                                    navigateAuthListener = { navigateTo(navController, Auth.route, Home.route) }
                                                )
                                            }
                                        },
                                    ),
                                    bottomNavSelected = homeViewModel.state.value.selectedBottomItem,
                                    pageListener = { index -> homeViewModel.selectBottomNavPage(index) },
                                    fabListener = {
                                        when {
                                            hasSelectedSections -> withSelectedSectionsBottomSheetVisible = true
                                            else -> newSectionBottomSheetVisible = true
                                        }
                                    },
                                    fabIcon = if (hasSelectedSections) { Icons.Filled.DeleteSweep } else { Icons.Filled.NoteAdd }
                                )
                                AddSectionBottomSheet(
                                    bottomSheetVisible = newSectionBottomSheetVisible
                                ) { sectionType ->
                                    newSectionBottomSheetVisible = false
                                    dashboardViewModel.addSection(sectionType)
                                }
                                UnselectDeleteSectionsBottomSheet(
                                    bottomSheetVisible = withSelectedSectionsBottomSheetVisible,
                                    unselectAllSelected = dashboardViewModel::unselectAllSelectedSections,
                                    deleteAllSelected = dashboardViewModel::deleteSelectedSections,
                                ) {
                                    withSelectedSectionsBottomSheetVisible = false
                                }
                            }
                            composable(
                                route = "${SectionDetails.route}/{sectionId}",
                                arguments = listOf(navArgument("sectionId") { type = NavType.IntType })
                            ) {
                                val viewModel = it.sharedViewModel<SectionDetailsViewModel>(navController = navController)
                                val sectionIdState = remember { mutableIntStateOf(it.arguments?.getInt("sectionId") ?: 0) }.asIntState()
                                LaunchedEffect(sectionIdState) {
                                    viewModel.getSection(sectionIdState.intValue)
                                }
                                SectionDetailsScreen(
                                    state = viewModel.state.value,
                                    addNoteListener = {
//                                        viewModel.addNote(Note(19, 0, "Hello!"))
                                        navigateTo(navController, NewNote.route)
                                    },
                                    editNoteListener = { noteId -> navigateTo(navController, "${EditNote.route}/$noteId") }
                                )
                            }
                            composable(route = NewNote.route) {
                                val viewModel: EditNoteViewModel = hiltViewModel()
                                EditNoteScreen(
                                    state = viewModel.state.value,
                                    title = NewNote.title,
                                    saveNoteListener = {}
                                )
                            }
                            composable(
                                route = "${EditNote.route}/{noteId}",
                                arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                            ) {
                                val viewModel: EditNoteViewModel = hiltViewModel()
                                viewModel.getNote(it.arguments?.getLong("noteId") ?: 0L)
                                EditNoteScreen(
                                    state = viewModel.state.value,
                                    title = EditNote.title,
                                    saveNoteListener = {}
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
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        Greeting("Android")
    }
}