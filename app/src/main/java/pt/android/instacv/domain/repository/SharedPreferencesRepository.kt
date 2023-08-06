package pt.android.instacv.domain.repository

interface SharedPreferencesRepository {
    fun putString(key: String, value: String)
    fun getString(key: String): String
    fun purgeAll()
}