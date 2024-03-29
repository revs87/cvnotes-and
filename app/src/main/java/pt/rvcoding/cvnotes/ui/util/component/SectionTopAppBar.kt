package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionTopAppBar(
    title: String = "Title"
) {
    LargeTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Box(
                modifier = Modifier.fillMaxSize().padding(2.dp),
                contentAlignment = Alignment.TopStart
            ) {
                CVNText(text = title, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SectionTopAppBar()
    }
}