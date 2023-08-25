@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.MyTheme
import java.util.Date

@Composable
fun SectionCard(
    type: SectionType = SectionType.EDUCATION,
    description: String = type.sectionName,
    color: Int = 0,
    notes: List<Note> = listOf(
        Note(1, 1, "Hello my friends!", "", Date().time),
        Note(1, 1, "Hello again!", "", Date().time),
        Note(1, 1, "Hello goddammit!", "", Date().time),
    ),
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .background(color = Color(color))
            .fillMaxWidth()
            .padding(4.dp)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = { onLongClick.invoke() },
            )
    ) {
        Box(
            modifier = Modifier.padding(2.dp)
        ) {
            Column {
                Text(text = description, fontSize = 38.sp)
                LazyColumn {
                    items(notes) { note ->
                        Text(text = note.content1, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SectionCard()
    }
}