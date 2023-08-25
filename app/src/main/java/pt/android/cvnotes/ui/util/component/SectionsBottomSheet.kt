@file:OptIn(ExperimentalMaterial3Api::class)

package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.util.SectionType


@Composable
fun AddSectionBottomSheet(
    bottomSheetVisible: Boolean = false,
    onDismiss: (SectionType) -> Unit = {}
) {
    if (bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss.invoke(SectionType.NONE) }
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
                    Text(text = "Add a Section:", fontSize = 22.sp)
                    LazyColumn {
                        items(Section.Sections) { sectionType ->
                            TextButton(
                                onClick = { onDismiss.invoke(sectionType) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = sectionType.sectionName,
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