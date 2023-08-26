@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import pt.android.cvnotes.domain.util.toSectionType
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.SectionListCard


@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun DashboardScreen(
    state: DashboardState = DashboardState(),
    onSectionClick: (Int) -> Unit = {},
    onSectionLongClick: (Int) -> Unit = {},
    saveToPrefs: (Int) -> Unit = {},
    initialScrollPosition: Int = 0,
) {
    val sectionsState by state.sectionsWithNotes.collectAsStateWithLifecycle(initialValue = emptyList())
    val hasSelectedSections by state.sectionsHasSelected.collectAsStateWithLifecycle(initialValue = false)
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
                verticalArrangement = if (sectionsState.isEmpty()) { Arrangement.Center }
                                      else { Arrangement.Top }
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
                    val lazyListState = rememberLazyListState(
                        initialFirstVisibleItemIndex = initialScrollPosition
                    )
                    LaunchedEffect(lazyListState) {
                        snapshotFlow {
                            lazyListState.firstVisibleItemIndex
                        }
                            .debounce(500L)
                            .collectLatest { index -> saveToPrefs.invoke(index) }
                    }
                    LaunchedEffect(sectionsState) {
                        if (state.scrollToBottom) {
                            lazyListState.animateScrollToItem(sectionsState.size - 1)
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = lazyListState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        itemsIndexed(sectionsState) { index, sectionWithNotes ->
                            SectionListCard(
                                modifier = Modifier.animateItemPlacement(),
                                type = sectionWithNotes.section.typeId.toSectionType(),
                                index = index,
                                description = sectionWithNotes.section.typeId.toSectionType().sectionName,
                                hasSelected = hasSelectedSections,
                                isSelected = sectionWithNotes.section.isSelected,
                                colorId = sectionWithNotes.section.colorId,
                                notes = sectionWithNotes.notes,
                                onClick = { onSectionClick.invoke(sectionWithNotes.section.id ?: 0) },
                                onLongClick = { onSectionLongClick.invoke(sectionWithNotes.section.id ?: 0) }
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