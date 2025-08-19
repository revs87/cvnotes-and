package pt.rvcoding.cvnotes.domain.use_case.section

import com.google.ai.client.generativeai.GenerativeModel
import pt.rvcoding.cvnotes.domain.CVGenerativeLLM
import pt.rvcoding.cvnotes.ui.util.L

class GenerateSectionsUseCase(
    private val model: GenerativeModel
) : CVGenerativeLLM() {
    suspend operator fun invoke(profession: String): List<String> {
        L.i(TAG, "Invoked with profession: $profession")
        val sections = mutableListOf<String>()
        try {
            val prompt = """
                Propose a list of $NUMBER_OF_SUGGESTIONS suggestions of a title for a CV section under the profession of $profession.
                Suggestions similar as: Profile, Professional Summary, Experience, Skills, Work History or Education.
                $QUERY_RESPONSE_FORMAT
            """

            val response = model.generateContent(prompt)
            val responseStr = response.text ?: "No response text found."
            sections.addAll(parseGeneratedList(responseStr))
            L.i(TAG, "Sections generated: $sections")
        } catch (e: Exception) {
            L.e(TAG, "Error: ${e.localizedMessage}")
        }
        return sections
    }

    companion object Companion {
        private const val TAG = "GenerateSectionsUseCase"
    }
}