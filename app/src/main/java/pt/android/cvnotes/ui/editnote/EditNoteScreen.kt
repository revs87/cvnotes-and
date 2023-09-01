package pt.android.cvnotes.ui.editnote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.OptionNoteType
import pt.android.cvnotes.ui.util.component.TitleTopAppBar


@Composable
fun EditNoteScreen(
    state: EditNoteState = EditNoteState(),
    title: String = "",
    isNoteValid: Boolean = false,
    savePartialListener: (Note) -> Note = { Note.default },
    saveNoteListener: (Note) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var noteTypeState by remember { mutableStateOf(NoteType.NONE) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { TitleTopAppBar(title, Green500) },
        floatingActionButton = {
            if (isNoteValid) {
                FloatingActionButton(
                    modifier = Modifier.size(75.dp),
                    shape = RoundedCornerShape(15.dp),
                    contentColor = Green500_Background3,
                    containerColor = Green500,
                    onClick = {
                        saveNoteListener.invoke(state.note ?: Note.default)
                        coroutineScope.launch {
                            state.note?.let { snackbarHostState.showSnackbar("Note added: ${it.asString()}") }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add note"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Box(
            modifier = Modifier
                .background(Green500_Background3)
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OptionNoteType(
                    initialOption = noteTypeState,
                    onOptionSelected = { noteType ->
                        noteTypeState = noteType
                        savePartialListener.invoke(
                            state.note?.copy(type = noteType.id, content1 = "Wassup?!") ?: Note.default)
                    }
                )
                // TODO

            }
            LoadingIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd),
                state.isLoading
            )
        }
        LaunchedEffect(true) {
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
        EditNoteScreen()
    }
}