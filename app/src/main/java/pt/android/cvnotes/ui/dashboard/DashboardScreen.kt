@file:OptIn(ExperimentalFoundationApi::class)

package pt.android.cvnotes.ui.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.android.cvnotes.domain.util.toSectionType
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.SectionCard


@Composable
fun DashboardScreen(
    state: DashboardState = DashboardState(),
) {
    val sectionsState by state.sectionsWithNotes.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                // TODO: Add StatsBoard
                if (sectionsState.isEmpty()) {
                    Text(
                        text = "There are no Sections available.\nPlease create a Section and then add Notes to it.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        itemsIndexed(sectionsState) { index, sectionWithNotes ->
                            SectionCard(
                                modifier = Modifier.animateItemPlacement(),
                                type = sectionWithNotes.section.typeId.toSectionType(),
                                description = sectionWithNotes.section.typeId.toSectionType().sectionName,
                                color = sectionWithNotes.section.color,
                                notes = sectionWithNotes.notes
                            )
                        }
                    }
                }
            }
            LoadingIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd),
                state.isLoading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        DashboardScreen()
    }
}