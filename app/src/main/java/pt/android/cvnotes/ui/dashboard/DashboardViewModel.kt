package pt.android.cvnotes.ui.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.android.cvnotes.domain.model.Note
import pt.android.cvnotes.domain.model.Section
import pt.android.cvnotes.domain.use_case.NoteUseCases
import pt.android.cvnotes.domain.use_case.SectionUseCases
import pt.android.cvnotes.domain.util.NoteType
import pt.android.cvnotes.domain.util.SectionType
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    fun getAllNotes() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun addSection(sectionType: SectionType) {
        if (sectionType != SectionType.NONE) {
            _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
            viewModelScope.launch(Dispatchers.Default) {
                sectionUseCases.insertSection(
                    Section(
                        typeId = sectionType.typeId,
                        description = sectionType.sectionName,
                        colorId = Random.nextInt(from = 0, until = Section.Colors.size)
                    )
                )
                val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)

                withContext(Dispatchers.Main) {
                    _state.value = _state.value.copy(
                        sectionsWithNotes = sectionWithNotes,
                        sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                        scrollToBottom = true,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectSection(id: Int) {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.selectSection(id)
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun unselectAllSelectedSections() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.unselectAllSections()
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun deleteSelectedSections() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.deleteSelectedSections()
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)

            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }

    fun addPersonalDataFromRuiVieira() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            sectionUseCases.getSectionsWithNotes(this).value.forEach {
                sectionUseCases.deleteSection(it.section)
            }

            sectionUseCases.insertSection(Section(SectionType.PROFILE.typeId))
            sectionUseCases.insertSection(Section(SectionType.SUMMARY.typeId))
            sectionUseCases.insertSection(Section(SectionType.EXPERIENCE.typeId))
//            sectionUseCases.insertSection(Section(SectionType.SKILLS.typeId))
//            sectionUseCases.insertSection(Section(SectionType.HISTORY.typeId))
//            sectionUseCases.insertSection(Section(SectionType.EDUCATION.typeId))

            sectionUseCases.getSectionsWithNotes(this).value.map { sectionWithNotes ->
                when (sectionWithNotes.section.typeId) {
                    SectionType.PROFILE.typeId -> addRuiVieiraProfileNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.SUMMARY.typeId -> addRuiVieiraSummaryNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.EXPERIENCE.typeId -> addRuiVieiraExperienceNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.SKILLS.typeId -> addRuiVieiraSkillsNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.HISTORY.typeId -> addRuiVieiraHistoryNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.EDUCATION.typeId -> addRuiVieiraEducationNotes(sectionWithNotes.section.id ?: 0)
                    else -> {}
                }
            }

            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this)
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    sectionsWithNotes = sectionWithNotes,
                    sectionsHasSelected = sectionUseCases.hasSelectedSections(),
                    isLoading = false
                )
            }
        }
    }


    private suspend fun addRuiVieiraProfileNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "RUI VIEIRA"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "36 y/o"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "\uD83C\uDDF5\uD83C\uDDF9 (+351) XXX XXX XXX"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "revs87@msn.com"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "Porto, Portugal"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "LinkedIn", "https://www.linkedin.com/in/revs87"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "Twitter", "https://twitter.com/revs87"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "X", "https://x.com/revs87"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "GitHub",  "https://github.com/revs87"))
    }
    private suspend fun addRuiVieiraSummaryNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "With over a decade of experience, I am seeking a professional position in Schweiz - whereas I intend to become firmly established.\n" +
                "Offering solid and clean coding principles around Android’s infrastructure while promoting quality via pair-programming and peer-review. I aim to take a development role of short- and long-term impact:"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "planning and implementing top to bottom and full fledged features;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "taking and anticipating challenges according to the company's strategy/vision;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "reviewing and scaling the current code base;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "mentoring, delegating and documenting."))
    }
    private suspend fun addRuiVieiraExperienceNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id,"Software development", "12+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "Android coder", "10 years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "\uD83C\uDDEC\uD83C\uDDE7 employee", "6+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "\uD83C\uDDF5\uD83C\uDDF9 employee", "3+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "\uD83C\uDDE8\uD83C\uDDED employee", "0 days"))
    }
    private suspend fun addRuiVieiraSkillsNotes(sectionId: Int) {}
    private suspend fun addRuiVieiraHistoryNotes(sectionId: Int) {}
    private suspend fun addRuiVieiraEducationNotes(sectionId: Int) {}
}