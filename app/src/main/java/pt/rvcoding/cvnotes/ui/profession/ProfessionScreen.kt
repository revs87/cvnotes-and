package pt.rvcoding.cvnotes.ui.profession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.rvcoding.cvnotes.theme.FontSourceSansPro
import pt.rvcoding.cvnotes.theme.MyTheme
import pt.rvcoding.cvnotes.theme.button.PrimaryButton
import pt.rvcoding.cvnotes.ui.util.component.cvn.CVNText

@Composable
fun ProfessionScreenRoot(
    vm: ProfessionViewModel = hiltViewModel<ProfessionViewModel>(),
    onDone: () -> Unit = {}
) {
    val professionValue = vm.profession.collectAsStateWithLifecycle().value
    ProfessionScreen(
        professionValue = professionValue,
        updateProfession = { vm.updateProfession(it) },
        submitBtnEnabled = professionValue.isNotBlank(),
        submitProfession = {
            vm.submitProfession(it)
            vm.clearProfessionOverridePreference()
            CoroutineScope(Dispatchers.Main).launch {
                delay(500L)
                onDone.invoke()
            }
        }
    )
}

@Composable
fun ProfessionScreen(
    modifier: Modifier = Modifier,
    professionTitle: String = "Your role",
    professionValue: String = "",
    btnText: String = "Save",
    submitBtnEnabled: Boolean = false,
    updateProfession: (newValue: String) -> Unit = {},
    submitProfession: (newValue: String) -> Unit = {},
    keyboardController: SoftwareKeyboardController? = null,
    focusManager: FocusManager? = null
) {
    MyTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CVNText(modifier = Modifier.padding(bottom = 8.dp), text = "Choose the profession/role you want to apply for.")
                ProfessionField(professionTitle, professionValue, { updateProfession.invoke(it) }) {
                    focusManager?.moveFocus(FocusDirection.Down)
                }
                PrimaryButton(
                    modifier = Modifier.padding(top = 6.dp),
                    enabled = submitBtnEnabled,
                    onClick = {
                        keyboardController?.hide()
                        focusManager?.clearFocus()
                        submitProfession.invoke(professionValue)
                    }) {
                    CVNText(text = btnText.uppercase())
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfessionScreenPreview() {
    ProfessionScreen()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ProfessionField(
    title: String = "",
    value: String = "",
    onValueChange: (newValue: String) -> Unit = {},
    onKeyboardAction: () -> Unit = {}
) {
    TextField(
        modifier = Modifier,
        singleLine = true,
        placeholder = { CVNText(text = title.uppercase()) },
        value = value,
        onValueChange = { onValueChange.invoke(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onKeyboardAction.invoke() }
        ),
        textStyle = TextStyle(fontFamily = FontSourceSansPro)
    )
}