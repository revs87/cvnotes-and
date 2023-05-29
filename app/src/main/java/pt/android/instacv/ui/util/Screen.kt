package pt.android.instacv.ui.util

sealed class Screen(val route: String) {
    object SplashScreen: Screen("splash_screen")
    object AboutScreen: Screen("about_screen")

    object Auth: Screen("auth")
    object IntroScreen: Screen("intro_screen")
    object RegisterScreen: Screen("register_screen")
    object LoginScreen: Screen("login_screen")

    object Home: Screen("home")
    object HomeScreen: Screen("home_screen")
}
