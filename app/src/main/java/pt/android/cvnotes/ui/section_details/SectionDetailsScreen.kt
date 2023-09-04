package pt.android.cvnotes.ui.section_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.android.cvnotes.theme.BackgroundColor
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.theme.Blue700
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.SpNormal
import pt.android.cvnotes.ui.util.component.BackTopAppBar
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.SectionDetailsNoteCards
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDetailsScreen(
    state: SectionDetailsState = SectionDetailsState(),
    sectionNameEditState: String = "",
    editSectionNameTextListener: (newChange: String) -> Unit = {},
    addNoteListener: (Int) -> Unit = {},
    editSectionListener: (sectionId: Int, newName: String) -> Unit = {_, _ -> },
    editNoteListener: (noteId: Long) -> Unit = {_ -> },
    onBackPressed: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val notes by state.notes.collectAsStateWithLifecycle(initialValue = emptyList())
    val hasSelectedNote by state.hasSelectedNote.collectAsStateWithLifecycle(initialValue = false)

    val appBarState = rememberTopAppBarState(initialHeightOffset = 0f)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)

    LaunchedEffect(state.section.description) {
        editSectionNameTextListener(state.section.description)
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            BackTopAppBar(
                title1 = state.section.description,
                title2 = "Notes",
                sectionNameEditState = sectionNameEditState,
                editSectionNameTextListener = editSectionNameTextListener,
                focusColor = Blue700,
                backgroundColor = Blue500,
                contentColor = Blue500_Background3,
                scrollBehavior = scrollBehavior,
                onTitleSave = { newName -> editSectionListener.invoke(state.section.id ?: 0, newName) },
                onBackPressed = { onBackPressed.invoke() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(15.dp),
                contentColor = Blue500_Background3,
                containerColor = Blue500,
                onClick = { addNoteListener.invoke(state.section.id ?: 0) }
            ) {
                Icon(
                    imageVector = when {
                        hasSelectedNote -> Icons.Filled.EditNote
                        else -> Icons.Filled.NoteAdd
                    },
                    tint = Blue500_Background3,
                    contentDescription = "Add note"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(padding)
        ) {
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CVNText(
                        text = "There are no Notes available.\nPlease add a New Note.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = SpNormal,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                SectionDetailsNoteCards(
                    modifier = Modifier,
                    description = state.section.description,
                    onNoteClick = { noteId -> editNoteListener.invoke(noteId) },
                    notes = notes
                )
                //TODO select note - edit Note button
                //TODO add new Note button
            }
            LoadingIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd),
                state.isLoading
            )
        }
        LaunchedEffect(state) {
            if (state.errorMessage.isNotBlank()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SectionDetailsScreen()
    }
}