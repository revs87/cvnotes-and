
package pt.android.cvnotes.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.inditex.itxmoviand.ui.component.BottomBarWithFab
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
import pt.android.cvnotes.ui.util.Screen.Splash
import pt.android.cvnotes.ui.util.component.AddSectionBottomSheet

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
                                val aboutViewModel: AboutViewModel = hiltViewModel()
                                var bottomSheetVisible by remember { mutableStateOf(false) }
                                BottomBarWithFab(
                                    bottomNavItems = listOf(
                                        Dashboard.apply {
                                            content = {
                                                DashboardScreen(
                                                    state = dashboardViewModel.state.value
                                                ) { id -> dashboardViewModel.selectSection(id) }
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
                                        bottomSheetVisible = true
//                                        navigateTo(navController, NewNote.route)
                                    },
                                    fabIcon = Icons.Filled.NoteAdd
                                )
                                AddSectionBottomSheet(bottomSheetVisible) { sectionType ->
                                    bottomSheetVisible = false
                                    dashboardViewModel.addSection(sectionType)
                                }
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