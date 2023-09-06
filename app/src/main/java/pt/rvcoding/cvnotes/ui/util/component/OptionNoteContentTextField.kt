package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.theme.FontSourceSansPro


@Composable
fun OptionNoteContentTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onContentChange: (newValue: String) -> Unit = {},
    onKeyboardAction: () -> Unit = {}
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp)
            .padding(start = 12.dp, end = 12.dp),
        singleLine = false,
        value = value,
        onValueChange = { onContentChange.invoke(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onNext = { onKeyboardAction.invoke() }
        ),
        textStyle = TextStyle(fontFamily = FontSourceSansPro)
    )

}