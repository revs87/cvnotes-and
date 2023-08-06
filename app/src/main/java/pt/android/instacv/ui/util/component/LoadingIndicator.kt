package pt.android.instacv.ui.util.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import pt.android.instacv.theme.MyTheme

@Composable
fun LoadingIndicator(isLoading: Boolean = false) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme {
        LoadingIndicator(true)
        val playing by remember { mutableStateOf(true) }
        LoadingAnimation(playing)
    }
}

@Composable
fun LoadingAnimation(
    isPlaying: Boolean = false
) {
    val composition: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.Asset("animations/loading-lines.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever,
        speed = 1f,
        isPlaying = isPlaying
    )
    LottieAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        composition = composition.value,
        progress = { progress },
    )
}