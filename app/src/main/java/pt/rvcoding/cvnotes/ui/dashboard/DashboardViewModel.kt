package pt.rvcoding.cvnotes.ui.dashboard

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.rvcoding.cvnotes.data.SPKey
import pt.rvcoding.cvnotes.domain.model.Note
import pt.rvcoding.cvnotes.domain.model.Section
import pt.rvcoding.cvnotes.domain.repository.SharedPreferencesRepository
import pt.rvcoding.cvnotes.domain.use_case.NoteUseCases
import pt.rvcoding.cvnotes.domain.use_case.SectionUseCases
import pt.rvcoding.cvnotes.domain.use_case.section.GenerateSectionsUseCase
import pt.rvcoding.cvnotes.domain.util.NoteType
import pt.rvcoding.cvnotes.domain.util.SectionType
import pt.rvcoding.cvnotes.ui.util.PdfGenerator
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sectionUseCases: SectionUseCases,
    private val noteUseCases: NoteUseCases,
    private val generateSectionsUseCase: GenerateSectionsUseCase,
    private val spRepository: SharedPreferencesRepository
) : ViewModel() {

    private val _state = mutableStateOf(DashboardState())
    val state: State<DashboardState> = _state

    fun getAllNotes() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
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
            viewModelScope.launch(Dispatchers.IO) {
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

    fun generateSections() {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            val generatedSections = generateSectionsUseCase.invoke(profession = spRepository.getString(SPKey.PROFESSION.key))
            generatedSections.forEach { sectionName ->
                sectionUseCases.insertSection(
                    Section(
                        typeId = SectionType.AI.typeId,
                        description = sectionName,
                        colorId = Random.nextInt(from = 0, until = Section.Colors.size)
                    )
                )
            }
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

    fun addSectionOtherType(sectionName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val section = Section(
                typeId = SectionType.OTHER.typeId,
                description = sectionName
            )
            sectionUseCases.insertSection(section)
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

    fun selectSection(id: Int) {
        _state.value = _state.value.copy(scrollToBottom = false, isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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

    fun addMyHardcodedData() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            sectionUseCases.getSectionsWithNotes(this).value.forEach {
                sectionUseCases.deleteSection(it.section)
            }

            sectionUseCases.insertSection(Section(SectionType.PROFILE.typeId, description = SectionType.PROFILE.sectionName))
            sectionUseCases.insertSection(Section(SectionType.SUMMARY.typeId, description = SectionType.SUMMARY.sectionName))
            sectionUseCases.insertSection(Section(SectionType.EXPERIENCE.typeId, description = SectionType.EXPERIENCE.sectionName))

            sectionUseCases.getSectionsWithNotes(this).value.map { sectionWithNotes ->
                when (sectionWithNotes.section.typeId) {
                    SectionType.PROFILE.typeId -> addMyProfileNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.SUMMARY.typeId -> addMySummaryNotes(sectionWithNotes.section.id ?: 0)
                    SectionType.EXPERIENCE.typeId -> addMyExperienceNotes(sectionWithNotes.section.id ?: 0)
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

    fun exportToPdf(
        context: Context,
        fileName: String,
        screenWidthPx: Int
    ) {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val sectionWithNotes = sectionUseCases.getSectionsWithNotes(this).value
            withContext(Dispatchers.Main) {
                PdfGenerator(context).generatePDF(
                    fileName,
                    sectionWithNotes,
                    screenWidthPx
                )
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun addMyProfileNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "My Name"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "## y/o"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "\uD83C\uDDF5\uD83C\uDDF9 (+351) XXX XXX XXX"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "my@email.com"))
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "Porto, Portugal"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "LinkedIn", "https://www.linkedin.com/in/myId"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "X", "https://x.com/myId"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED.id, "GitHub",  "https://github.com/myId"))
    }
    private suspend fun addMySummaryNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.TEXT.id, "With over a decade of experience, I am seeking a professional position.\n" +
                "Offering solid and clean coding principles around Android’s infrastructure while promoting quality via pair-programming and peer-review. I aim to take a development role of short- and long-term impact:"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "planning and implementing top to bottom and full fledged features;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "taking and anticipating challenges according to the company's strategy/vision;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "reviewing and scaling the current code base;"))
        noteUseCases.insertNote(Note(sectionId, NoteType.BULLET.id, "mentoring, delegating and documenting."))
    }
    private suspend fun addMyExperienceNotes(sectionId: Int) {
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id,"Software development", "15+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "Android development", "10+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "\uD83C\uDDEC\uD83C\uDDE7 employee", "6+ years"))
        noteUseCases.insertNote(Note(sectionId, NoteType.KEY_VALUE_COLON_SEPARATED_WITH_BULLET.id, "\uD83C\uDDF5\uD83C\uDDF9 employee", "6+ years"))
    }

    companion object {
        const val MAX_SECTION_NAME_SIZE = 24
    }
}