package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import pt.rvcoding.cvnotes.domain.util.NoteType
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText

@Composable
fun DropDownNoteType(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onDismissRequest: ()-> Unit = {},
    onItemClick: (NoteType) -> Unit = {},
    items: List<NoteType> = NoteType.values().asList()
) {
    DropdownMenu(
        modifier = modifier,
        offset = DpOffset(x = (-66).dp, y = (-10).dp),
        expanded = expanded,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
        onDismissRequest = { onDismissRequest.invoke() }
    ) {
        items.forEach {
            DropdownMenuItem(
                text = { CVNText(text = it.example) },
                onClick = { onItemClick.invoke(it) }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        DropDownNoteType()
    }
}