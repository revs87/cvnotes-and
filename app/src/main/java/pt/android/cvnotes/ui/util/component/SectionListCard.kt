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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.Gray500
import pt.android.cvnotes.theme.MyTheme
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
        Note(1, 1, "Hello my friends!", "", Date().time),
        Note(1, 1, "Hello again!", "", Date().time),
        Note(1, 1, "Hello goddammit!", "", Date().time),
    ),
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, bottom = 2.dp, top = if (index == 0) 4.dp else 2.dp)
            .combinedClickable(
                onClick = { onClick.invoke() },
                onLongClick = { onLongClick.invoke() },
            )
    ) {
        Box(
            modifier = Modifier
                .background(color = Section.Colors[colorId])
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .padding(2.dp)
        ) {
            Column {
                Text(text = description, fontSize = 38.sp)
                Column {
                    notes.forEach { note ->
                        Text(
                            text = note.content1,
                            fontSize = 11.sp
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
                    colorFilter = ColorFilter.tint(color = Gray500)
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