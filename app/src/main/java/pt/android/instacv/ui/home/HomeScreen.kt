package pt.android.instacv.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pt.android.instacv.ui.component.LoadingIndicator
import pt.android.instacv.ui.util.Screen


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state: HomeState = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.section == HomeSection.AUTH) {
        LaunchedEffect(Unit) {
            navController.navigate(route = Screen.AuthScreen.route) {
                popUpTo(Screen.HomeScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LoadingIndicator(state.isLoading)
        Button(onClick = { viewModel.logout() }) {
            Text(text = "Log out".uppercase())
        }
        if (state.errorMessage.isNotBlank()) {
            LaunchedEffect(System.currentTimeMillis()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}