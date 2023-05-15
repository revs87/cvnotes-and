package pt.android.instacv.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun HomeScreen(
    navController: NavController
) {
//    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
////            if (state.isLoading) {
//                CircularProgressIndicator(
//                    color = MaterialTheme.colorScheme.primary,
//                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
//                    strokeWidth = 4.dp,
//                    modifier = Modifier
//                        .size(40.dp)
//                        .align(Alignment.BottomEnd)
//                        .padding(16.dp)
//                )
////            }
            Text(text = "HOME")
        }



//        if (state.errorMessage.isNotBlank()) {
//            LaunchedEffect(System.currentTimeMillis()) {
//                snackbarHostState.showSnackbar(state.errorMessage)
//            }
//        }
    }
}