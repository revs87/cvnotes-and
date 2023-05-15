package pt.android.instacv.ui.auth

enum class AuthSection {
    INTRO, REGISTER, LOGIN
}

data class AuthState(
    val section: AuthSection = AuthSection.INTRO,
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)