package pt.android.cvnotes.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import pt.android.cvnotes.theme.Blue500

@Preview(showBackground = true)
@Composable
fun SplashScreen(
    continueListener: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SplashAnimation { continueListener.invoke() }
        Text(
            text = "InstaCV",
            fontSize = 24.sp,
            letterSpacing = 6.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold,
            color = Blue500
        )
    }
}

@Composable
fun SplashAnimation(
    onFinish: () -> Unit = {}
) {
    val composition: LottieCompositionResult =
        rememberLottieComposition(LottieCompositionSpec.Asset("animations/job-cv.json"))
    var isPlaying by remember { mutableStateOf(true) }
    val progress by animateLottieCompositionAsState(
        composition = composition.value,
        iterations = 1,
        speed = 2f,
        isPlaying = isPlaying
    )
    LottieAnimation(
        modifier = Modifier.fillMaxWidth().height(400.dp),
        composition = composition.value,
        progress = { progress },
    )
    if (progress == 1f) {
        LaunchedEffect(true) {
            isPlaying = false
            onFinish.invoke()
        }
    }
}