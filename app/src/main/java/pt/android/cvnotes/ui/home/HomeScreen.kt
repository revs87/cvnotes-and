package pt.android.cvnotes.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.button.PrimaryButton
import pt.android.cvnotes.ui.util.component.LoadingIndicator


@Composable
fun HomeScreen(
    state: HomeState = HomeState(),
    profileState: HomeProfileState = HomeProfileState(),
    logoutListener: () -> Unit = {},
    navigateAuthListener: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.section == HomeSection.AUTH) {
        LaunchedEffect(Unit) {
            navigateAuthListener.invoke()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LoadingIndicator(state.isLoading)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logged as: ${profileState.email}")
            PrimaryButton(onClick = { logoutListener.invoke() }) {
                Text(text = "Log out".uppercase())
            }
        }
        if (state.errorMessage.isNotBlank()) {
            LaunchedEffect(System.currentTimeMillis()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyTheme {
        HomeScreen()
    }
}