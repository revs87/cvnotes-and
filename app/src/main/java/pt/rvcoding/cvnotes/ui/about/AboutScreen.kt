package pt.rvcoding.cvnotes.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pt.rvcoding.cvnotes.theme.Blue500_Background1
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.button.TertiaryButton
import pt.rvcoding.cvnotes.ui.util.Screen.About
import pt.rvcoding.cvnotes.ui.util.component.CenteredTopAppBar
import pt.rvcoding.cvnotes.ui.util.component.LoadingIndicator
import pt.rvcoding.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@Composable
fun AboutScreen(
    state: State<AboutState> = mutableStateOf(AboutState()),
    profileState: State<AboutProfileState> = mutableStateOf(AboutProfileState()),
    logoutListener: () -> Unit = {},
    navigateAuthListener: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    if (state.value.section == HomeSection.AUTH) {
        LaunchedEffect(state) { navigateAuthListener.invoke() }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { CenteredTopAppBar(About.title) }
    ) { padding ->
        Box {
            Column(
                modifier = Modifier.background(Blue500_Background1)
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CVNText(text = "Version: ${state.value.version}")
                CVNText(text = "Logged as: ${profileState.value.email}")
                TertiaryButton(onClick = { logoutListener.invoke() }) {
                    CVNText(text = "Log out".uppercase())
                }
            }
            LoadingIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd),
                state.value.isLoading
            )
        }
        LaunchedEffect(true) {
            if (state.value.errorMessage.isNotBlank()) {
                snackbarHostState.showSnackbar(state.value.errorMessage)
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