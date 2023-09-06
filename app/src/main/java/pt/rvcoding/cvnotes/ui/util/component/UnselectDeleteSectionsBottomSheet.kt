package pt.rvcoding.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText

data class SelectedSectionsOption(
    val title: String = "",
    var onAction: () -> Unit = {}
) {
    companion object {
        val Options = listOf(
            SelectedSectionsOption("Delete selected Sections"),
            SelectedSectionsOption("Unselect all selected Sections"),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnselectDeleteSectionsBottomSheet(
    bottomSheetVisible: Boolean = false,
    unselectAllSelected: () -> Unit = {},
    deleteAllSelected: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    if (bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss.invoke() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 6.dp,
                        bottom = 100.dp
                    )
            ) {
                Column {
                    CVNText(text = "Selected Sections:", fontSize = 22.sp)
                    LazyColumn {
                        itemsIndexed(SelectedSectionsOption.Options) { index, option ->
                            when (index) {
                                0 -> option.onAction = deleteAllSelected
                                else -> option.onAction = unselectAllSelected
                            }
                            TextButton(
                                onClick = {
                                    option.onAction.invoke()
                                    onDismiss.invoke()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CVNText(
                                        text = option.title,
                                        fontSize = 22.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}