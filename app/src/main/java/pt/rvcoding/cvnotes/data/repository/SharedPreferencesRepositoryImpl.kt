package pt.rvcoding.cvnotes.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.data.SPKey.PROFESSION
import pt.rvcoding.cvnotes.data.SPKey.PROFESSION_OVERRIDE
import pt.rvcoding.cvnotes.data.SPKey.UID
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class SharedPreferencesRepositoryImpl @Inject constructor(
    context: Context
) : SharedPreferencesRepository {
    private var sp: SharedPreferences = context.getSharedPreferences(context.packageName + ".sp", Context.MODE_PRIVATE)
    override fun putBoolean(key: String, value: Boolean) { sp.edit { putBoolean(unique(key), value) } }
    override fun getBoolean(key: String): Boolean = sp.getBoolean(unique(key), false)
    override fun putString(key: String, value: String) { sp.edit { putString(unique(key), value) } }
    override fun getString(key: String): String = sp.getString(unique(key), "") ?: ""
    override fun purgeAll() { SPKey.entries.forEach { sp.edit { remove(it.key) } } }

    // Guarantees unique profession key per user
    private fun unique(key: String): String = when (key) {
        PROFESSION.key, PROFESSION_OVERRIDE.key -> "${sp.getString(UID.key, "") ?: ""}_$key"
        else -> key
    }
}