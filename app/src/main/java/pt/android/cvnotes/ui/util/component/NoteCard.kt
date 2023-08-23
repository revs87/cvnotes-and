@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.util.NoteType

@Composable
fun NoteCard(
    type: NoteType = NoteType.ALL,
    text: String,
    onTextChange: (newValue: String) -> Unit = {},
    editTimestamp: String,
    editMode: Boolean = false,
    editModeToggle: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = { editModeToggle.invoke() },
            )
    ) {
        Column {
            if (editMode) {
                TextField(value = text, onValueChange = onTextChange)
            } else {
                Text(text = text)
            }
            Text(text = editTimestamp, fontSize = 8.sp)
        }
    }
}