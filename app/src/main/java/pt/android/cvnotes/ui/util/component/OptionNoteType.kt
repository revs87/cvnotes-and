package pt.android.cvnotes.ui.util.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background2
import pt.android.cvnotes.theme.SpMedium
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@Composable
fun OptionNoteType(
    initialOption: NoteType = NoteType.NONE,
    isUnselected: Boolean = true,
    onOptionSelected: (NoteType) -> Unit = {}
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var noteTypeState = initialOption

    if (isUnselected) {
        ElevatedCard(
            modifier = Modifier
                .animateContentSize()
                .padding(start = 12.dp, end = 12.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Green500,
                contentColor = Green500_Background2
            )
        ) {
            Box(modifier = Modifier
                .background(Green500)
                .clickable { menuExpanded = !menuExpanded }
                .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 8.dp)
            ) {
                CVNText(text = "Unselected".uppercase(), fontSize = SpMedium, fontWeight = FontWeight.SemiBold)
            }
        }
    } else {
        OutlinedCard(
            modifier = Modifier
                .animateContentSize()
                .padding(start = 12.dp, end = 12.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Green500_Background2
            ),
            border = BorderStroke(1.dp, Green500),
        ) {
            Box(modifier = Modifier
                .background(Green500_Background2)
                .clickable { menuExpanded = !menuExpanded }
                .padding(start = 12.dp, end = 12.dp, bottom = 8.dp, top = 8.dp)
            ) {
                when (noteTypeState) {
                    NoteType.NONE -> { /* do nothing */ }
                    else -> {
                        CVNText(text = noteTypeState.example, fontSize = SpMedium, fontWeight = FontWeight.SemiBold)
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