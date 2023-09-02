package pt.android.cvnotes.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.button.PrimaryButton
import pt.android.cvnotes.ui.util.component.cvn.CVNText

@Composable
fun IntroScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {
    MyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CVNText(modifier = Modifier.padding(bottom = 8.dp), text = "Do you already have an account?")
                PrimaryButton(modifier = Modifier.padding(bottom = 6.dp), onClick = { onRegisterClick.invoke() }) { CVNText(text = "Create account".uppercase()) }
                PrimaryButton(onClick = { onLoginClick.invoke() }) { CVNText(text = "Login".uppercase()) }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    IntroScreen()
}