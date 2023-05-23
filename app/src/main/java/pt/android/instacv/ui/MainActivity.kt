package pt.android.instacv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui.auth.AuthScreen
import pt.android.instacv.ui.auth.AuthViewModel
import pt.android.instacv.ui.home.HomeScreen
import pt.android.instacv.ui.home.HomeViewModel
import pt.android.instacv.ui.util.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val startingRoute =
                if (authViewModel.state.value.isLoggedIn) { Screen.HomeScreen.route }
                else { Screen.AuthScreen.route }
            val navController = rememberNavController()

            MyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startingRoute
                    ) {
                        composable(route = Screen.AuthScreen.route) {
                            AuthScreen(
                                navigateHomeListener = {
                                    navController.navigate(route = Screen.HomeScreen.route) {
                                        popUpTo(Screen.AuthScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                state = authViewModel.state.value,
                                fieldsState = authViewModel.fieldsState.value,
                                registerClickListener = { authViewModel.onRegisterClick() },
                                loginClickListener = { authViewModel.onLoginClick() },
                                updateEmailListener = { authViewModel.updateEmail(it) },
                                updatePwdListener = { authViewModel.updatePwd(it) },
                                createUserListener = { email, pwd -> authViewModel.createUser(email, pwd) },
                                logUserListener = { email, pwd -> authViewModel.logUser(email, pwd) },
                            )
                        }
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(
                                state = homeViewModel.state.value,
                                logoutListener = { homeViewModel.logout() },
                                navigateAuthListener = {
                                    navController.navigate(route = Screen.AuthScreen.route) {
                                        popUpTo(Screen.HomeScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
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