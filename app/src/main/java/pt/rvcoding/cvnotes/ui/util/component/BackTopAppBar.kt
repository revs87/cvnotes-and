package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.Blue500
import pt.rvcoding.cvnotes.theme.Blue500_Background3
import pt.rvcoding.cvnotes.theme.Blue700
import pt.rvcoding.cvnotes.theme.FontSourceSansPro
import pt.rvcoding.cvnotes.theme.SpLarge
import pt.rvcoding.cvnotes.theme.SpXLarge
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopAppBar(
    title1: String = "Title1",
    title2: String = "Title2",
    sectionNameEditState: String = "Title1",
    editSectionNameTextListener: (newChange: String) -> Unit = {},
    focusColor: Color = Blue700,
    backgroundColor: Color = Blue500,
    contentColor: Color = Blue500_Background3,
    menuIconVisible: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onTitleSave: (newName: String) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    // Forcefully set the initial state
    LaunchedEffect(Unit) {
        scrollBehavior.collapse()
    }

    var isEditEnabled by remember { mutableStateOf(false) }
    var isExpanded = !scrollBehavior.isCollapsed()

    if (!isExpanded) { isEditEnabled = false }
    val backIcon: ImageVector =
        if (isExpanded) { Icons.Filled.ArrowUpward }
        else { Icons.AutoMirrored.Filled.ArrowBack }
    val onBack =
        when {
            isEditEnabled -> { { isEditEnabled = false } }
            isExpanded -> { { scrollBehavior.collapse() } }
            else -> onBackPressed
        }

    LargeTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            TitleBox(
                title1,
                title2,
                sectionNameEditState = sectionNameEditState,
                editSectionNameTextListener = editSectionNameTextListener,
                isExpanded,
                isEditEnabled,
                focusColor = focusColor,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                onTitleSave = {
                    onTitleSave.invoke(it)
                    isExpanded = true
                    isEditEnabled = false
                },
                onTitleClick = {
                    if (!isExpanded) { scrollBehavior.expand() }
                    else { isEditEnabled = true }
                }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor
        ),
        navigationIcon = { TitleNavBack(onBack, icon = backIcon) },
        scrollBehavior = scrollBehavior,
        actions = { if (menuIconVisible) { TitleMoreMenu() } }
    )
}

@Composable
private fun TitleBox(
    title1: String = "Title1",
    title2: String = "Title2",
    sectionNameEditState: String = "Title1",
    editSectionNameTextListener: (newChange: String) -> Unit = {},
    isExpanded: Boolean = true,
    isEdit: Boolean = true,
    onTitleClick: () -> Unit = {},
    onTitleSave: (newName: String) -> Unit = {},
    focusColor: Color = Blue700,
    backgroundColor: Color = Blue500,
    contentColor: Color = Blue500_Background3,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onTitleClick.invoke() },
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            if (isExpanded) {
                if (isEdit) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp, end = 12.dp),
                        value = sectionNameEditState,
                        onValueChange = { editSectionNameTextListener(it) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = backgroundColor,
                            unfocusedContainerColor = backgroundColor,
                            focusedTextColor = contentColor,
                            unfocusedTextColor = contentColor,
                            cursorColor = contentColor,
                            selectionColors = TextSelectionColors(
                                handleColor = focusColor,
                                backgroundColor = focusColor
                            ),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.clickable { onTitleSave.invoke(sectionNameEditState) },
                                imageVector = Icons.Filled.Save,
                                contentDescription = "titleNavEdit_$title1",
                                tint = contentColor
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = FontSourceSansPro,
                            fontSize = SpXLarge
                        )
                    )
                } else {
                    CVNText(
                        text = title1,
                        color = contentColor
                    )
                }
            }
            if (!isEdit) {
                CVNText(
                    text = if (isExpanded) { title2 }
                           else { "$title1 $title2" },
                    color = contentColor,
                    fontSize = SpLarge,
                )
            }
        }
    }
}

@Composable
private fun TitleNavBack(
    onBackPressed: () -> Unit = {},
    contentColor: Color = Blue500_Background3,
    icon: ImageVector = Icons.Filled.ArrowBack
) {
    IconButton(
        onClick = { onBackPressed.invoke() },
        modifier = Modifier.padding(6.dp)
    ){
        Icon(
            imageVector = icon,
            contentDescription = "TitleNavArrowBack",
            tint = contentColor
        )
    }
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

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior?.collapse() {
    this ?: return
    this.state.heightOffset = -Float.MAX_VALUE
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior?.expand() {
    this ?: return
    this.state.heightOffset = 0f
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarScrollBehavior?.isCollapsed(): Boolean {
    if (this == null || this.state.heightOffset >= 0.0) { return false }
    return true
}
