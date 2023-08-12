package pt.android.cvnotes.domain.repository

interface SharedPreferencesRepository {
    fun putString(key: String, value: String)
    fun getString(key: String): String
    fun purgeAll()
}