package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.asString
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.theme.BackgroundCardColor
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.theme.TextColor
import java.util.Date

@Composable
fun SectionDetailsCard(
    modifier: Modifier = Modifier,
    type: SectionType = SectionType.EDUCATION,
    colorId: Int = 0,
    notes: List<Note> = listOf(
        Note(1, 1, "Hello my friends!", "", Date().time),
        Note(1, 1, "Hello again!", "", Date().time),
        Note(1, 1, "Hello goddammit!", "", Date().time),
    )
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(BackgroundCardColor)
                .fillMaxSize()
                .padding(2.dp)
        ) {
            LazyColumn {
                item {
                    Text(
                        text = type.sectionName, fontSize = 38.sp,
                        color = TextColor
                    )
                }
                items(notes) { note ->
                    Text(
                        text = note.asString(),
                        fontSize = 16.sp,
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
        SectionDetailsCard()
    }
}