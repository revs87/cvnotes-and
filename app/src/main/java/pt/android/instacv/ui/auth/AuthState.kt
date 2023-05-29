package pt.android.instacv.ui.auth

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)