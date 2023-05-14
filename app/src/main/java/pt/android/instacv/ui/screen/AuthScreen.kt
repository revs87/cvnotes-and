package pt.android.instacv.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pt.android.instacv.theme.MyTheme
import pt.android.instacv.ui.util.Screen

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state: AuthState = viewModel.state.value
    val fieldsState: AuthFieldsState = viewModel.fieldsState.value
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.isLoggedIn) { navController.navigate(route = Screen.HomeScreen.route) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(40.dp).align(Alignment.BottomEnd).padding(16.dp)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = fieldsState.emailValue, onValueChange = { viewModel.updateEmail(it) })
            TextField(
                value = fieldsState.pwdValue,
                onValueChange = { viewModel.updatePwd(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
            )
            Button(onClick = { viewModel.createUser(fieldsState.emailValue, fieldsState.pwdValue) }) {
                Text(text = "Sign in".uppercase())
            }
        }
        if (state.errorMessage.isNotBlank()) {
            LaunchedEffect(state.errorMessage) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        val navController = rememberNavController()
        AuthScreen(navController)
    }
}