package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Box
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
import pt.rvcoding.cvnotes.theme.MyTheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

val LoadingIndicatorSize = 45.dp

@Preview(showBackground = true)
@Composable
fun PreviewLoading() {
    MyTheme {
        LoadingIndicator(isLoading = true)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAnimation() {
    MyTheme {
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