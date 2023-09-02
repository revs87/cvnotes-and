package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.BackgroundColor
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.TextColor
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionDetailsNoteCards(
    modifier: Modifier = Modifier,
    type: SectionType = SectionType.EDUCATION,
    onNoteClick: (Long) -> Unit = {},
    notes: List<Note> = listOf(
        Note(1, 1, "Hello my friends!", "", timestamp = Date().time, id = 0),
        Note(1, 1, "Hello again!", "", timestamp = Date().time, id = 1),
        Note(1, 1, "Hello goddammit!", "", timestamp = Date().time, id = 2),
    )
) {
    Box(
        modifier = Modifier.background(BackgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            item(key = -1L) {
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 10.dp),
                    text = type.sectionName,
                    fontSize = 38.sp,
                    color = TextColor
                )
            }
            items(
                items = notes,
                key = { note -> note.id ?: 0L }
            ) { note ->
                Card(
                    modifier = modifier
                        .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                ) {
                    Box(
                        modifier = modifier
                            .background(Blue500_Background3)
                            .combinedClickable(
                                onClick = { onNoteClick.invoke(note.id ?: 0L) },
                                onLongClick = {},
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(8.dp),
                            text = note.asString(),
                            lineHeight = 18.sp,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Justify,
                            color = TextColor
                        )
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
        SectionDetailsNoteCards()
    }
}