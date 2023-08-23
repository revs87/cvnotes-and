package pt.android.cvnotes.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.android.cvnotes.domain.util.toNoteType
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.NoteCard
import java.util.Date


@Composable
fun DashboardScreen(
    state: DashboardState = DashboardState(),
) {
    val notesState = state.notes.collectAsStateWithLifecycle(initialValue = emptyList())
    val notes = remember { notesState.value }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LoadingIndicator(state.isLoading)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Text(text = "Testing")
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(notes) { index, note ->
                    NoteCard(
                        type = note.type.toNoteType(),
                        text = note.content1 + note.content2,
                        editTimestamp = Date(note.timestamp).toString() )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    MyTheme {
        DashboardScreen()
    }
}