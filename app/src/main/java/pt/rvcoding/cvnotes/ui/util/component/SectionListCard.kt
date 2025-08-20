package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.asString
import pt.rvcoding.cvnotes.domain.util.SectionType
import pt.rvcoding.cvnotes.theme.BackgroundCardColor
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpHuge
import pt.rvcoding.cvnotes.theme.SpNormal
import pt.rvcoding.cvnotes.theme.TextColor
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionListCard(
    modifier: Modifier = Modifier,
    index: Int = 1,
    description: String = SectionType.EDUCATION.sectionName,
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
    SelectableCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 2.dp, top = if (index == 0) 4.dp else 2.dp),
        id = index.toLong(),
        isSelected = isSelected,
        hasSelected = hasSelected,
        onClick = onClick,
        onLongClick = onLongClick,
        cardContentColor = BackgroundCardColor,
        cardContentPadding = 2.dp,
        cardDefaultMinSize = 100.dp
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            CVNText(
                text = description,
                fontSize = SpHuge,
                color = TextColor
            )
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                notes.forEach { note ->
                    CVNText(
                        text = note.asString(),
                        lineHeight = SpNormal,
                        fontSize = SpNormal,
                        textAlign = TextAlign.Start,
                        color = TextColor
                    )
                }
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