package pt.android.instacv.ui.screen

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)