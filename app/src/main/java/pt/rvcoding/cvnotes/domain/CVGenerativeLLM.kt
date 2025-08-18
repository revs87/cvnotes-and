package pt.rvcoding.cvnotes.domain

open class CVGenerativeLLM {

    protected fun parseGeneratedList(
        content: String,
        numberOfSuggestions: Int = NUMBER_OF_SUGGESTIONS
    ): List<String> {
        return content
            // 1. Split the string by the starting delimiter.
            .split(START_DELIMITER)
            // 2. Take the last 5 elements, excluding the text before the first match.
            .takeLast(numberOfSuggestions)
            // 3. For each resulting chunk, find the content before the end delimiter.
            .map { chunk ->
                chunk
                    .substringBefore(END_DELIMITER)
                    .trim()
            }
    }


    companion object {
        const val GEMINI_LLM_VERSION = "gemini-2.5-flash"

        private const val START_DELIMITER = "START:"
        private const val END_DELIMITER = "END"
        protected const val QUERY_RESPONSE_FORMAT = "Show the results with the prefix of \"$START_DELIMITER\" and suffix of \"$END_DELIMITER\" for each entry, unenumerated."
        protected const val NUMBER_OF_SUGGESTIONS = 5
    }
}
