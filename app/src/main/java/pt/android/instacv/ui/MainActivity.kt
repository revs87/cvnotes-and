package pt.android.instacv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui.auth.AuthViewModel
import pt.android.instacv.ui.auth.IntroScreen
import pt.android.instacv.ui.auth.LoginScreen
import pt.android.instacv.ui.auth.RegistrationScreen
import pt.android.instacv.ui.home.HomeScreen
import pt.android.instacv.ui.home.HomeViewModel
import pt.android.instacv.ui.util.Screen.AboutScreen
import pt.android.instacv.ui.util.Screen.Auth
import pt.android.instacv.ui.util.Screen.Home
import pt.android.instacv.ui.util.Screen.HomeScreen
import pt.android.instacv.ui.util.Screen.IntroScreen
import pt.android.instacv.ui.util.Screen.LoginScreen
import pt.android.instacv.ui.util.Screen.RegisterScreen
import pt.android.instacv.ui.util.Screen.SplashScreen

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
                        startDestination = SplashScreen.route
                    ) {
                        composable(route = SplashScreen.route) {
                            //TODO Splash lottie
                            val authViewModel: AuthViewModel = hiltViewModel()
                            val startingRoute =
                                if (authViewModel.state.value.isLoggedIn) { Home.route }
                                else { Auth.route }
                            LaunchedEffect(true) { navigateTo(navController, startingRoute, SplashScreen.route) }
                        }
                        composable(route = AboutScreen.route) { /* TODO About */ }
                        navigation(
                            route = Auth.route,
                            startDestination = IntroScreen.route
                        ) {
                            composable(
                                route = IntroScreen.route
                            ) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                IntroScreen(
                                    { navigateTo(navController, RegisterScreen.route) },
                                    { navigateTo(navController, LoginScreen.route)  }
                                )
                            }
                            composable(route = RegisterScreen.route) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                RegistrationScreen(
                                    state = authViewModel.state.value,
                                    fieldsState = authViewModel.fieldsState.value,
                                    updateEmailListener = { authViewModel.updateEmail(it) },
                                    updatePwdListener = { authViewModel.updatePwd(it) },
                                    createUserListener = { email, pwd -> authViewModel.createUser(email, pwd) },
                                )
                            }
                            composable(route = LoginScreen.route) {
                                val authViewModel = it.sharedViewModel<AuthViewModel>(navController = navController)
                                if (authViewModel.state.value.isLoggedIn) {
                                    LaunchedEffect(true) { navigateTo(navController, Home.route, Auth.route) }
                                }
                                LoginScreen(
                                    state = authViewModel.state.value,
                                    fieldsState = authViewModel.fieldsState.value,
                                    updateEmailListener = { authViewModel.updateEmail(it) },
                                    updatePwdListener = { authViewModel.updatePwd(it) },
                                    logUserListener = { email, pwd -> authViewModel.logUser(email, pwd) },
                                )
                            }
                        }
                        navigation(
                            route = Home.route,
                            startDestination = HomeScreen.route
                        ) {
                            composable(route = HomeScreen.route) {
                                val homeViewModel = it.sharedViewModel<HomeViewModel>(navController = navController)
                                HomeScreen(
                                    state = homeViewModel.state.value,
                                    logoutListener = { homeViewModel.logout() },
                                    navigateAuthListener = { navigateTo(navController, Auth.route, Home.route) }
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