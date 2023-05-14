package pt.android.instacv.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

interface SharedPreferencesRepository {
    fun putString(key: String, value: String)
    fun getString(key: String): String
    fun purgeAll()
}

@Singleton
class SharedPreferencesRepositoryImpl @Inject constructor(
    context: Context
) : SharedPreferencesRepository {
    var sp: SharedPreferences = context.getSharedPreferences(context.packageName + ".sp", Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) { sp.edit().putString(key, value).apply() }
    override fun getString(key: String): String = sp.getString(key, "") ?: ""
    override fun purgeAll() { SPKey.values().forEach { sp.edit().remove(it.key).apply() } }
}