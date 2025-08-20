package pt.rvcoding.cvnotes.domain.repository

interface SharedPreferencesRepository {
    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean

    fun putString(key: String, value: String)
    fun getString(key: String): String
    fun purgeAll()
}