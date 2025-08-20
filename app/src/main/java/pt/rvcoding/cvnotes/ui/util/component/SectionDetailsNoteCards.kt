package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.asString
import pt.rvcoding.cvnotes.theme.BackgroundCardColor
import pt.rvcoding.cvnotes.theme.BackgroundColor
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpMedium
import pt.rvcoding.cvnotes.theme.TextColor
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText
import java.util.Date

@Composable
fun SectionDetailsNoteCards(
    modifier: Modifier = Modifier,
    hasSelectedNotes: Boolean = true,
    onNoteClick: (Long) -> Unit = {},
    onLongNoteClick: (Long) -> Unit = {},
    notes: List<Note> = listOf(
        Note(1, 1, "Hello my friends!", "", timestamp = Date().time, id = 0, isSelected = true),
        Note(1, 1, "Hello again! Yes!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", "", timestamp = Date().time, id = 1),
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
            items(
                items = notes,
                key = { note -> note.id ?: 0L }
            ) { note ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = modifier.weight(11f)
                    ) {
                        SelectableCard(
                            modifier = modifier
                                .padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    top = 2.dp,
                                    bottom = 2.dp
                                ),
                            id = note.id ?: 0L,
                            isSelected = note.isSelected,
                            hasSelected = hasSelectedNotes,
                            onClick = { onNoteClick.invoke(note.id ?: 0L) },
                            onLongClick = { onLongNoteClick.invoke(note.id ?: 0L) },
                            cardContentColor = BackgroundCardColor,
                            cardContentPadding = 8.dp,
                            content = {
                                CVNText(
                                    modifier = Modifier.weight(1f),
                                    text = note.asString(),
                                    lineHeight = SpMedium,
                                    fontSize = SpMedium,
                                    textAlign = TextAlign.Start,
                                    color = TextColor
                                )
                            },
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