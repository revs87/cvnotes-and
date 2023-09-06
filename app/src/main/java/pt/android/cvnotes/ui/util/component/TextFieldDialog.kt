package pt.android.cvnotes.ui.util.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pt.android.cvnotes.theme.Blue500
import pt.android.cvnotes.theme.Blue500_Background1
import pt.android.cvnotes.theme.Blue500_Background3
import pt.android.cvnotes.theme.Gray100
import pt.android.cvnotes.theme.Gray300
import pt.android.cvnotes.theme.Gray500
import pt.android.cvnotes.theme.SpNormal
import pt.android.cvnotes.theme.SpXLarge
import pt.android.cvnotes.theme.button.PrimaryButton
import pt.android.cvnotes.ui.dashboard.DashboardViewModel.Companion.MAX_SECTION_NAME_SIZE
import pt.android.cvnotes.ui.util.component.cvn.CVNText


@Preview(showBackground = true)
@Composable
fun TextFieldDialog(
    title: String = "New section name",
    placeholder: String = "Enter name",
    initialValue: String = "",
    setShowDialog: (Boolean) -> Unit = {},
    setValue: (String) -> Unit = {}
) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(initialValue) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CVNText(
                            text = title,
                            style = TextStyle(
                                fontSize = SpXLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "$title-icon1",
                            tint = Gray500,
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Blue500_Background3,
                            unfocusedContainerColor = Blue500_Background1,
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DriveFileRenameOutline,
                                contentDescription = "$title-icon2",
                                tint = Blue500,
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        singleLine = true,
                        placeholder = { CVNText(text = placeholder.uppercase(), fontWeight = FontWeight.SemiBold) },
                        value = txtField.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = {
                            txtField.value = it.take(MAX_SECTION_NAME_SIZE)
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                    return@PrimaryButton
                                }
                                setValue(txtField.value)
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue500,
                                contentColor = Blue500_Background1,
                                disabledContainerColor = Gray100,
                                disabledContentColor = Gray300
                            )
                        ) {
                            CVNText(text = "Done".uppercase(), fontSize = SpNormal, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}