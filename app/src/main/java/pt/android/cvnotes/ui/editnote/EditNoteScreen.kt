package pt.android.cvnotes.ui.editnote

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.model.isDoubleContent
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.domain.util.toNoteType
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background2
import pt.android.cvnotes.theme.Green500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.SpMedium
import pt.android.cvnotes.theme.TextColor
import pt.android.cvnotes.theme.White
import pt.android.cvnotes.ui.util.component.BackTopAppBar
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.OptionNoteContentTextField
import pt.android.cvnotes.ui.util.component.OptionNoteDateTime
import pt.android.cvnotes.ui.util.component.OptionNoteType
import pt.android.cvnotes.ui.util.component.cvn.CVNText
import java.util.Date


@Composable
fun EditNoteScreen(
    state: EditNoteState = EditNoteState(),
    title: String = "Title",
    isNoteValid: Boolean = false,
    updateContent1: (note: Note?, newText: String) -> Unit = { _, _ ->},
    updateContent2: (note: Note?, newText: String) -> Unit = { _, _ ->},
    savePartialListener: (Note) -> Note = { Note.Default },
    saveNoteListener: (Note) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isDoubleContent = state.note.isDoubleContent()
    var noteTypeState = state.note?.type?.toNoteType() ?: NoteType.NONE
    val isUnselected = noteTypeState.id == NoteType.NONE.id

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
                        saveNoteListener.invoke(state.note ?: Note.Default)
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Preview:",
                    textColor = Green500_Background3,
                    bgColor = Green500,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green500)
                        .padding(top = 12.dp, bottom = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .heightIn(max = 250.dp)
                            .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Green500_Background2)
                        ) {
                            CVNText(
                                modifier = Modifier
                                    .padding(8.dp),
                                text = when {
                                    state.note == null -> "New Note!"
                                    state.note.content1.isBlank() -> "New Note!"
                                    else -> state.note.asString().ifEmpty { "New Note!" }
                                },
                                lineHeight = SpMedium,
                                fontSize = SpMedium,
                                textAlign = TextAlign.Start,
                                color = TextColor,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .animateContentSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    ) {
                        Header(
                            text = "Note type:",
                            textColor = TextColor,
                            bgColor = Green500_Background3,
                        )
                        OptionNoteType(
                            initialOption = noteTypeState,
                            isUnselected = isUnselected,
                            onOptionSelected = { noteType ->
                                noteTypeState = noteType
                                val newNote = state.note?.copy(
                                    type = noteType.id,
                                    content1 = state.note.content1,
                                    content2 = state.note.content2
                                ) ?: Note.Default
                                savePartialListener.invoke(newNote)
                                isDoubleContent = newNote.isDoubleContent()
                            }
                        )
                    }
                    if (!isUnselected) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                        ) {
                            Header(
                                text = if (isDoubleContent) "Content 1:" else "Content:",
                                textColor = TextColor,
                                bgColor = Green500_Background3,
                            )
                            OptionNoteContentTextField(
                                value = state.note?.content1 ?: "",
                                onContentChange = { updateContent1.invoke(state.note, it) },
                            )
                        }
                    }
                    if (!isUnselected && isDoubleContent) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                        ) {
                            Header(
                                text = "Content 2:",
                                textColor = TextColor,
                                bgColor = Green500_Background3,
                            )
                            OptionNoteContentTextField(
                                value = state.note?.content2 ?: "",
                                onContentChange = { updateContent2.invoke(state.note, it) },
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                    ) {
                        Header(
                            text = "Modified:",
                            textColor = TextColor,
                            bgColor = Green500_Background3,
                        )
                        OptionNoteDateTime(
                            datetime = Date(state.note?.timestamp ?: 0).toString()
                        )
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
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


@Composable
private fun Header(
    modifier: Modifier = Modifier,
    text: String = "",
    textColor: Color = TextColor,
    bgColor: Color = White
) {
    CVNText(
        modifier = modifier
            .background(bgColor)
            .padding(start = 12.dp, end = 12.dp),
        text = text,
        fontSize = SpMedium,
        fontWeight = FontWeight.SemiBold,
        color = textColor
    )
}