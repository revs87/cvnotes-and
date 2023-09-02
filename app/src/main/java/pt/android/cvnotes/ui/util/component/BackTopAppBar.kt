package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    title: String = "Title",
    bgColor: Color = Blue500,
    contentColor: Color = Blue500_Background3,
    expanded: Boolean = false,
    menuIconVisible: Boolean = false,
    onBackPressed: () -> Unit = {}
) {
    if (expanded) {
        MediumTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = { TitleBox(title) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor),
            navigationIcon = { TitleNavBack(onBackPressed) },
            actions = { if (menuIconVisible) { TitleMoreMenu() } }
        )
    } else {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = { TitleBox(title) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor),
            navigationIcon = { TitleNavBack(onBackPressed) },
            actions = { if (menuIconVisible) { TitleMoreMenu() } }
        )
    }
}

@Composable
private fun TitleBox(
    title: String = "",
    contentColor: Color = Blue500_Background3
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        CVNText(
            text = title,
            color = contentColor
        )
    }
}


@Composable
private fun TitleNavBack(
    onBackPressed: () -> Unit = {},
    contentColor: Color = Blue500_Background3
) {
    Icon(
        modifier = Modifier.padding(12.dp).clickable { onBackPressed.invoke() },
        imageVector = Icons.Filled.ArrowBack,
        contentDescription = "TitleNavArrowBack",
        tint = contentColor
    )
}

@Composable
private fun TitleMoreMenu(
    contentColor: Color = Blue500_Background3
) {
    Icon(
        modifier = Modifier.padding(12.dp),
        imageVector = Icons.Filled.MoreVert,
        contentDescription = "TitleMoreMenu",
        tint = contentColor
    )
}