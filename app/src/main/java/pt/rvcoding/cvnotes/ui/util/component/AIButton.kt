package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.rvcoding.cvnotes.theme.aiRadialColors


@Composable
fun AIButton(
    modifier: Modifier = Modifier,
    generateListener: () -> Unit = {},
    visible: Boolean = true
) {
    // ✨ Text font weight animation
    val infiniteTransition = rememberInfiniteTransition(label = "fontWeightAnimation")
    val animatedFontWeight by infiniteTransition.animateFloat(
        initialValue = 300f, // Corresponds to FontWeight.Light
        targetValue = 900f,  // Corresponds to FontWeight.Black
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2500,
                easing = EaseOutBack // https://easings.net/
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fontWeightValue"
    )

    // ✨ Text dynamic font animation
    val baseFontSize = 18.sp
    val bumpAmount = 4.sp // How much bigger the text should get at its peak
    // Calculate the animation's progress (from 0.0 to 1.0+)
    // We normalize the animated weight to get a progress value.
    // Because of EaseOutBack, this value will briefly go above 1.0, creating the bump.
    val animationProgress = (animatedFontWeight - 300f) / (900f - 300f)
    val dynamicFontSize = (baseFontSize.value + (bumpAmount * animationProgress).value).sp

    // ✨ Brush radial animation
    val buttonSizePx = with(LocalDensity.current) { 45.dp.toPx() }
    // Animate the radius from a small point to fill the button
    val animatedRadius by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = buttonSizePx, // Animate up to the full size
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = EaseOutBack),
            repeatMode = RepeatMode.Reverse // Expand and contract smoothly
        ),
        label = "gradientRadius"
    )

    // ✨ Button visibility animation
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(animationSpec = tween(200)) + fadeIn(animationSpec = tween(200)),
        exit = scaleOut(animationSpec = tween(200)) + fadeOut(animationSpec = tween(200))
    ) {
        Surface(
            modifier = modifier.size(45.dp),
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 6.dp,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        brush = Brush.radialGradient(
                            colors = aiRadialColors,
                            // The center of the gradient is the center of the button
                            center = Offset(buttonSizePx / 2f, buttonSizePx / 2f),
                            // The radius is now controlled by our animation
                            radius = animatedRadius.coerceAtLeast(0.1f)
                        )
                    )
                    .clickable { generateListener.invoke() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI",
                    color = Color.White,
                    fontWeight = FontWeight(animatedFontWeight.toInt()),
                    fontSize = dynamicFontSize,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.4f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun AIButtonPreview() {
    AIButton()
}