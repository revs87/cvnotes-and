package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.android.cvnotes.R
import pt.android.cvnotes.theme.button.PrimaryButton
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthFields(
    title: String = "Title",
    emailTitle: String = "Email title",
    pwdTitle: String = "Pwd title",
    btnText: String = "Btn text",
    emailValue: String = "",
    pwdValue: String = "",
    submitBtnEnabled: Boolean = false,
    updateEmail: (newValue: String) -> Unit = {},
    updatePwd: (newValue: String) -> Unit = {},
    createUser: ((email: String, pwd: String) -> Unit)? = null,
    logUser: ((email: String, pwd: String) -> Unit?)? = null,
    keyboardController: SoftwareKeyboardController? = null,
    focusManager: FocusManager? = null
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CVNText(text = title)
        EmailField(emailTitle, emailValue, { updateEmail.invoke(it) }) {
            focusManager?.moveFocus(FocusDirection.Down)
        }
        PasswordField(pwdTitle, pwdValue, { updatePwd.invoke(it) }) {
            keyboardController?.hide()
            focusManager?.clearFocus()
            createUser?.invoke(emailValue, pwdValue)
            logUser?.invoke(emailValue, pwdValue)
        }
        PrimaryButton(
            enabled = submitBtnEnabled,
            onClick = {
                keyboardController?.hide()
                focusManager?.clearFocus()
                createUser?.invoke(emailValue, pwdValue)
                logUser?.invoke(emailValue, pwdValue)
            }) {
            CVNText(text = btnText.uppercase())
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun EmailField(
    title: String = "",
    value: String = "",
    onValueChange: (newValue: String) -> Unit = {},
    onKeyboardAction: () -> Unit = {}
) {
    TextField(
        modifier = Modifier.autofill(
            autofillTypes = listOf(AutofillType.EmailAddress),
            onFill = { onValueChange.invoke(it) },
        ),
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
        )
    )
}

@Composable
private fun PasswordField(
    title: String = "",
    value: String = "",
    onValueChange: (newValue: String) -> Unit = {},
    onKeyboardAction: () -> Unit = {}
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }

    TextField(
        placeholder = { CVNText(text = title.uppercase()) },
        value = value,
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { onValueChange.invoke(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = { onKeyboardAction.invoke() }
        ),
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    modifier = Modifier.size(45.dp).padding(8.dp),
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off),
                    contentDescription = ""
                )
            }
        },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this
        .onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
        .onFocusChanged { focusState ->
            autofill?.run {
                if (focusState.isFocused) {
                    requestAutofillForNode(autofillNode)
                } else {
                    cancelAutofillForNode(autofillNode)
                }
            }
        }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    AuthFields()
}