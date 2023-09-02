package pt.android.cvnotes.ui.editnote

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background2
import pt.android.cvnotes.theme.Green500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.TextColor
import pt.android.cvnotes.ui.util.component.BackTopAppBar
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.OptionNoteType


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditNoteScreen(
    state: EditNoteState = EditNoteState(),
    title: String = "",
    isNoteValid: Boolean = false,
    savePartialListener: (Note) -> Note = { Note.default },
    saveNoteListener: (Note) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var noteTypeState by remember { mutableStateOf(NoteType.NONE) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = { BackTopAppBar(title, Green500, Green500_Background3) { onBackPressed.invoke() } },
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
                            val action = if (title == "New Note") { "added" } else { "saved" }
                            state.note?.let { snackbarHostState.showSnackbar("Note $action: ${it.asString()}") }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (title == "New Note") { Icons.Filled.NoteAdd } else { Icons.Filled.Save },
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
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green500)
                        .padding(top = 10.dp, bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Green500_Background2)
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = when {
                                    state.note == null -> "New Note!"
                                    state.note.content1.isBlank() -> "New Note!"
                                    else -> state.note.asString()
                                },
                                lineHeight = 18.sp,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Justify,
                                color = TextColor
                            )
                        }
                    }
                }
                OptionNoteType(
                    initialOption = noteTypeState,
                    onOptionSelected = { noteType ->
                        noteTypeState = noteType
                        savePartialListener.invoke(
                            state.note?.copy(type = noteType.id, content1 = "Wassup", content2 = "Dog?!") ?: Note.default)
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