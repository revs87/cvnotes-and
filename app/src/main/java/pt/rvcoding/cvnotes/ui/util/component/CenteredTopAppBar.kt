package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import pt.rvcoding.cvnotes.theme.Blue500
import pt.rvcoding.cvnotes.theme.Blue500_Background3
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopAppBar(
    title: String = "Title",
    bgColor: Color = Blue500,
    contentColor: Color = Blue500_Background3
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { TitleBox(title) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor),
    )
}

@Composable
private fun TitleBox(
    title: String = "",
    contentColor: Color = Blue500_Background3
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CVNText(
            text = title,
            color = contentColor
        )
    }
}