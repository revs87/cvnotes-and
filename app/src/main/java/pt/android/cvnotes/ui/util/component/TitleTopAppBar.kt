package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.theme.Blue500


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopAppBar(
    title: String = "Title",
    bgColor: Color = Blue500,
) {
    CenterAlignedTopAppBar(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
    )
}