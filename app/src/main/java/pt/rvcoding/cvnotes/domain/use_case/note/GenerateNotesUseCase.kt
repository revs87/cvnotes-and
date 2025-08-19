package pt.rvcoding.cvnotes.domain.use_case.note

import com.google.ai.client.generativeai.GenerativeModel
import pt.rvcoding.cvnotes.domain.CVGenerativeLLM
import pt.rvcoding.cvnotes.domain.repository.NoteRepository
import pt.rvcoding.cvnotes.ui.util.L

class GenerateNotesUseCase(
    private val model: GenerativeModel,
    private val noteRepository: NoteRepository
) : CVGenerativeLLM() {
    suspend operator fun invoke(
        profession: String,
        section: String,
        sectionId: Int
    ): List<String> {
        L.i(TAG, "Invoked with profession: $profession, section: $section")
        return try {
            val notes = mutableListOf<String>()
            val currentNotes = noteRepository
                .getNotesList(sectionId)
                .map { note -> "${note.content1} ${note.content2}".trim() }
                .distinct()
                .toMutableList()
            val currentNotesStr = if (currentNotes.isNotEmpty()) currentNotes.joinToString(", ") else ""
            val prompt = """
                Propose a list of $NUMBER_OF_SUGGESTIONS suggestions of contents for a CV under the profession of $profession and under the section of $section.
                ${currentNotesStr.ifEmpty { "" }}
                $QUERY_RESPONSE_FORMAT
            """

            val response = model.generateContent(prompt)
            val responseStr = response.text ?: "No response text found."
            notes.addAll(parseGeneratedList(responseStr))
            L.i(TAG, "Notes generated and parsed: $notes")
            return notes
        } catch (e: Exception) {
            L.e(TAG, "Error: ${e.localizedMessage}")
            emptyList()
        }
    }

    companion object Companion {
        private const val TAG = "GenerateNotesUseCase"
    }
}