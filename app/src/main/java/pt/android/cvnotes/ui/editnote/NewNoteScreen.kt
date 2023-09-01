package pt.android.cvnotes.ui.editnote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.android.cvnotes.domain.model.Note
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
    saveNoteListener: (Note) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var noteTypeState by remember { mutableStateOf(NoteType.NONE) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { TitleTopAppBar(title, Green500) }
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
                    onOptionSelected = { noteTypeState = it }
                )



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