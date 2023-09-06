package pt.rvcoding.cvnotes.data.repository

import android.content.Context
import android.content.SharedPreferences
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferencesRepositoryImpl @Inject constructor(
    context: Context
) : SharedPreferencesRepository {
    private var sp: SharedPreferences = context.getSharedPreferences(context.packageName + ".sp", Context.MODE_PRIVATE)

    override fun putString(key: String, value: String) { sp.edit().putString(key, value).apply() }
    override fun getString(key: String): String = sp.getString(key, "") ?: ""
    override fun purgeAll() { SPKey.values().forEach { sp.edit().remove(it.key).apply() } }
}