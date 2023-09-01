package pt.android.cvnotes.ui.section_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.util.toSectionType
import pt.android.cvnotes.theme.BackgroundColor
import pt.android.cvnotes.theme.BackgroundSecondaryColor
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.theme.Green500
import pt.android.cvnotes.theme.Green500_Background1
import pt.android.cvnotes.theme.Green500_Background3
import pt.android.cvnotes.theme.MyTheme
import pt.android.cvnotes.ui.util.component.LoadingIndicator
import pt.android.cvnotes.ui.util.component.LoadingIndicatorSize
import pt.android.cvnotes.ui.util.component.SectionDetailsCard


@Composable
fun SectionDetailsScreen(
    state: SectionDetailsState = SectionDetailsState(),
    addNoteListener: () -> Unit = {},
    editNoteListener: (Note) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val notes by state.notes.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(75.dp),
                shape = RoundedCornerShape(15.dp),
                contentColor = Blue500_Background3,
                containerColor = Blue500,
                onClick = { addNoteListener.invoke() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add note"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Box(
            modifier = Modifier.background(BackgroundColor)
        ) {
            SectionDetailsCard(
                modifier = Modifier.padding(padding),
                type = state.section.typeId.toSectionType(),
                colorId = state.section.colorId,
                notes = notes
            )
                //TODO select note - edit Note button
                //TODO add new Note button
            LoadingIndicator(
                modifier = Modifier
                    .size(LoadingIndicatorSize)
                    .align(Alignment.BottomEnd),
                state.isLoading
            )
        }
        LaunchedEffect(state) {
            if (state.errorMessage.isNotBlank()) {
                snackbarHostState.showSnackbar(state.errorMessage)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTheme {
        SectionDetailsScreen()
    }
}