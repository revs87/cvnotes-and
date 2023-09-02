package pt.android.cvnotes.ui.util.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background2
import pt.android.cvnotes.theme.SpLarge
import pt.android.cvnotes.theme.SpXLarge
import pt.android.cvnotes.ui.util.component.cvn.CVNText

@Composable
fun OptionNoteType(
    initialOption: NoteType = NoteType.NONE,
    onOptionSelected: (NoteType) -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var noteTypeState = initialOption

    OutlinedCard(
        modifier = Modifier
            .animateContentSize()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 10.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Green500_Background2
        ),
        border = BorderStroke(1.dp, Green500),
    ) {
        Box(modifier = Modifier
            .background(Green500_Background2)
            .clickable { menuExpanded = !menuExpanded }
            .padding(4.dp)
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (noteTypeState.id == 0) {
                    CVNText(text = "Click HERE to select a Note type.")
                } else {
                    CVNText(text = "Note type selected:\n")
                    CVNText(text = noteTypeState.typeName.uppercase(), fontSize = SpLarge)
                    when (noteTypeState) {
                        NoteType.NONE -> { /* do nothing */}
                        else -> { CVNText(text = noteTypeState.example, fontSize = SpXLarge) }
                    }
                }
            }
        }
    }
    DropDownNoteType(
        modifier = Modifier.background(Green500_Background2),
        expanded = menuExpanded,
        onDismissRequest = { menuExpanded = false },
        onItemClick = { noteType ->
            noteTypeState = noteType
            menuExpanded = false
            onOptionSelected.invoke(noteType)
        },
    )
}