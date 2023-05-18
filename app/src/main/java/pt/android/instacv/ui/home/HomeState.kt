package pt.android.instacv.ui.home

enum class HomeSection {
    AUTH, MAIN
}

data class HomeState(
    val section: HomeSection = HomeSection.MAIN,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)