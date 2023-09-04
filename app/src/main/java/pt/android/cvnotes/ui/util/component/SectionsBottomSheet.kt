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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.util.SectionType
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AddSectionBottomSheet(
    bottomSheetVisible: Boolean = false,
    onRuiVieiraClicked: () -> Unit = {},
    onOtherClicked: () -> Unit = {},
    onDismiss: (SectionType) -> Unit = {}
) {
    if (bottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss.invoke(SectionType.NONE) },
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
                    CVNText(text = "Add a Section:", fontSize = 22.sp)
                    LazyColumn {
                        items(Section.Sections) { sectionType ->
                            TextButton(
                                onClick = {
                                    when (sectionType) {
                                        SectionType.RUI_VIEIRA_HARDCODED_DATA -> {
                                            onRuiVieiraClicked.invoke()
                                            onDismiss.invoke(SectionType.NONE)
                                        }
                                        SectionType.OTHER -> {
                                            onOtherClicked.invoke()
                                            onDismiss.invoke(SectionType.NONE)
                                        }
                                        else -> onDismiss.invoke(sectionType)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CVNText(
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