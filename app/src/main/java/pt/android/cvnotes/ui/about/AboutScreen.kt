package pt.android.cvnotes.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.inditex.itxmoviand.ui.component.ITXTopAppBar
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.button.TertiaryButton
import pt.android.cvnotes.ui.util.Screen.About
import pt.android.cvnotes.ui.util.component.LoadingIndicator


@Composable
fun AboutScreen(
    state: AboutState = AboutState(),
    profileState: AboutProfileState = AboutProfileState(),
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
        modifier = Modifier.fillMaxSize(),
        topBar = { ITXTopAppBar(About.title) }
    ) { padding ->
        LoadingIndicator(state.isLoading)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Logged as: ${profileState.email}")
            TertiaryButton(onClick = { logoutListener.invoke() }) {
                Text(text = "Log out".uppercase())
            }
        }
        LaunchedEffect(System.currentTimeMillis()) {
            if (state.errorMessage.isNotBlank()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyTheme {
        AboutScreen()
    }
}