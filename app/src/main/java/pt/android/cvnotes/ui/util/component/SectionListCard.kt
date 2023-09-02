package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.BackgroundCardColor
import pt.android.cvnotes.theme.Gray300
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.SpNormal
import pt.android.cvnotes.theme.TextColor
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionListCard(
    modifier: Modifier = Modifier,
    type: SectionType = SectionType.EDUCATION,
    index: Int = 1,
    description: String = type.sectionName,
    hasSelected: Boolean = false,
    isSelected: Boolean = false,
    colorId: Int = 0,
    notes: List<Note> = listOf(
        Note(1, 1, "Hello my friends!", "", timestamp = Date().time),
        Note(1, 1, "Hello again!", "", timestamp = Date().time),
        Note(1, 1, "Hello goddammit!", "", timestamp = Date().time),
    ),
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 2.dp, top = if (index == 0) 4.dp else 2.dp)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = { onLongClick.invoke() },
            )
    ) {
        Box(
            modifier = Modifier
                .background(BackgroundCardColor)
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .padding(2.dp)
        ) {
            Column {
                Text(
                    text = description,
                    fontSize = 38.sp,
                    color = TextColor
                )
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    notes.forEach { note ->
                        Text(
                            text = note.asString(),
                            lineHeight = SpNormal,
                            fontSize = SpNormal,
                            textAlign = TextAlign.Justify,
                            color = TextColor
                        )
                    }
                }
            }
            if (hasSelected) {
                Image(
                    modifier = Modifier.align(Alignment.TopEnd),
                    imageVector = when (isSelected) {
                        true -> Icons.Filled.CheckBox
                        false -> Icons.Filled.CheckBoxOutlineBlank
                    },
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(color = Gray300)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SectionListCard()
    }
}