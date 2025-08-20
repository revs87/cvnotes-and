@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package pt.rvcoding.cvnotes.ui.dashboard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import pt.rvcoding.cvnotes.domain.model.SectionWithNotes
import pt.rvcoding.cvnotes.theme.BackgroundColor
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.SpNormal
import pt.rvcoding.cvnotes.ui.util.component.LoadingIndicator
import pt.rvcoding.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.rvcoding.cvnotes.ui.util.component.SectionListCard
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalFoundationApi::class, FlowPreview::class)
@Composable
fun DashboardScreen(
    state: DashboardState = DashboardState(),
    sectionsWithNotes: List<SectionWithNotes> = emptyList(),
    hasSelectedSections: Boolean = false,
    onSectionClick: (Int) -> Unit = {},
    onSectionLongClick: (Int) -> Unit = {},
    saveToPrefs: (Int) -> Unit = {},
    initialScrollPosition: Int = 0,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .background(BackgroundColor)
                .fillMaxWidth()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                verticalArrangement = if (sectionsWithNotes.isEmpty()) { Arrangement.Center }
                                      else { Arrangement.Top }
            ) {
                // TODO: Add StatsBoard
                if (sectionsWithNotes.isEmpty()) {
                    CVNText(
                        text = "There are no Sections available.\nPlease create a Section and then add Notes to it.",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = SpNormal,
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
                    LaunchedEffect(sectionsWithNotes) {
                        if (state.scrollToBottom) {
                            lazyListState.animateScrollToItem(sectionsWithNotes.size - 1)
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().animateContentSize(),
                        state = lazyListState,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        itemsIndexed(
                            items = sectionsWithNotes,
                            key = { _, sectionWithNotes -> sectionWithNotes.section.id ?: 0 }
                        ) { index, sectionWithNotes ->
                            SectionListCard(
                                modifier = Modifier
                                    .animateItem(
                                        fadeInSpec = tween(durationMillis = 500),
                                        fadeOutSpec = tween(durationMillis = 300)
                                    ),
                                index = index,
                                description = sectionWithNotes.section.description,
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
                    .align(Alignment.BottomCenter),
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