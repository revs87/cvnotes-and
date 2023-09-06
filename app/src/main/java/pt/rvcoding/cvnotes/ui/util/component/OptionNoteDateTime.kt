package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.Gray300
import pt.rvcoding.cvnotes.theme.SpSmall
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@Composable
fun OptionNoteDateTime(
    modifier: Modifier = Modifier,
    datetime: String = "18:00 03/09/2023", // Local time
) {
    CVNText(
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp, top = 4.5.dp),
        text = datetime,
        fontSize = SpSmall,
        color = Gray300
    )
}