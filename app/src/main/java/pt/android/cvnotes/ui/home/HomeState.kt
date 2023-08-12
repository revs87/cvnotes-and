package pt.android.cvnotes.ui.home

enum class HomeSection {
    AUTH, MAIN
}

data class HomeState(
    val section: HomeSection = HomeSection.MAIN,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)

data class HomeProfileState(
    val email: String = "",
)