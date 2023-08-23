package pt.android.cvnotes.ui.about

enum class HomeSection {
    AUTH, MAIN
}

data class AboutState(
    val section: HomeSection = HomeSection.MAIN,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)

data class AboutProfileState(
    val email: String = "",
)