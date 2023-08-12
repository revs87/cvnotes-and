package pt.android.cvnotes.ui.auth


sealed class AuthError(val message: String) {
    data class LoginError(val userMessage: String) : AuthError("${LoginError::class.simpleName}: $userMessage")
    data class RegisterError(val userMessage: String) : AuthError("${RegisterError::class.simpleName}: $userMessage")
}
