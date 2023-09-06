package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.asString
import pt.rvcoding.cvnotes.theme.BackgroundColor
import pt.rvcoding.cvnotes.theme.Blue500_Background3
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpMedium
import pt.rvcoding.cvnotes.theme.TextColor
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionDetailsNoteCards(
    modifier: Modifier = Modifier,
    hasSelectedNotes: Boolean = true,
    onNoteClick: (Long) -> Unit = {},
    onLongNoteClick: (Note) -> Unit = {},
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
                        Card(
                            modifier = modifier
                                .padding(
                                    start = 12.dp,
                                    end = if (hasSelectedNotes) { 0.dp } else { 12.dp },
                                    top = 4.dp,
                                    bottom = 4.dp
                                ),
                        ) {
                            Box(
                                modifier = modifier
                                    .background(Blue500_Background3)
                                    .combinedClickable(
                                        onClick = {
                                            if (hasSelectedNotes) {
                                                onLongNoteClick.invoke(note)
                                            } else {
                                                onNoteClick.invoke(note.id ?: 0L)
                                            }
                                        },
                                        onLongClick = { onLongNoteClick.invoke(note) },
                                    )
                            ) {
                                CVNText(
                                    modifier = Modifier.padding(8.dp),
                                    text = note.asString(),
                                    lineHeight = SpMedium,
                                    fontSize = SpMedium,
                                    textAlign = TextAlign.Start,
                                    color = TextColor
                                )
                            }
                        }
                    }
                    if (hasSelectedNotes) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(2f),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                modifier = Modifier.size(45.dp),
                                onClick = { onLongNoteClick.invoke(note) },
                            ) {
                                Icon(
                                    imageVector =
                                        if (note.isSelected) { Icons.Filled.CheckBox }
                                        else { Icons.Filled.CheckBoxOutlineBlank },
                                    contentDescription = "noteTick_${note.id ?: 0L}",
                                    tint = TextColor
                                )
                            }
                        }
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