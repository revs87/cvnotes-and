package pt.rvcoding.cvnotes.ui.profession

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import javax.inject.Inject


@HiltViewModel
class ProfessionViewModel @Inject constructor(
    private val spRepository: SharedPreferencesRepository
) : ViewModel() {

    private val _profession: MutableStateFlow<String> = MutableStateFlow(spRepository.getString(SPKey.PROFESSION.key))
    val profession: StateFlow<String> = _profession.asStateFlow()

    fun updateProfession(newProfession: String) { _profession.update { newProfession } }
    fun submitProfession(profession: String) {spRepository.putString(SPKey.PROFESSION.key, profession) }
    fun hasProfessionPreference(): Boolean = spRepository.getString(SPKey.PROFESSION.key).isNotBlank()
    fun hasProfessionOverridePreference(): Boolean = spRepository.getBoolean(SPKey.PROFESSION_OVERRIDE.key)
    fun clearProfessionOverridePreference() = spRepository.putBoolean(SPKey.PROFESSION_OVERRIDE.key, false)
}