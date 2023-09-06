package pt.rvcoding.cvnotes.ui.util.component

data class AuthFieldsState(
    val emailValue: String = "",
    val pwdValue: String = "",
    val submitBtnEnabled: Boolean = false,
)