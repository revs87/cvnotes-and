package pt.rvcoding.cvnotes.domain

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class CVGenerativeLLMTest : CVGenerativeLLM() {
    private val content by lazy {
        """
            Okay, here are 5 title suggestions for a CV section for an Android Developer, using the \"START:\" prefix and \"END\" suffix:\n\n
            1.  START:Android Development Skills & Technologies  END\n
            2.  START:Technical Proficiency (Android)  END\n
            3.  START:Android Development Expertise  END\n
            4.  START:Key Android Technologies & Frameworks  END\n
            5.  START:Android Development Skillset  END\n\n
        """
    }

    @Test
    fun `parseGeneratedList returns correct listing`() = runBlocking {
        val sections = parseGeneratedList(content, numberOfSuggestions = 5)
        println(sections)
        assert(sections.size == 5)
        assert(sections[0] == "Android Development Skills & Technologies")
        assert(sections[4] == "Android Development Skillset")
    }
}