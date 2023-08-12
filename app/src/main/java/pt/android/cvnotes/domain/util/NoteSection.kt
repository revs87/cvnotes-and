package pt.android.cvnotes.domain.util


enum class NoteSection(val id: Int, val sectionName: String) {
    ALL(0, ""),
    PROFILE(1, "Profile"),
    SUMMARY(2, "Professional Summary"),
    EXPERIENCE(3, "Experience"),
    SKILLS(4, "Skills"),
    HISTORY(5, "Work History"),
    EDUCATION(6, "Education"),
}

fun Int.toNoteSection(): NoteSection = when (this) {
    NoteSection.PROFILE.id -> NoteSection.PROFILE
    NoteSection.SUMMARY.id -> NoteSection.SUMMARY
    NoteSection.EXPERIENCE.id -> NoteSection.EXPERIENCE
    NoteSection.SKILLS.id -> NoteSection.SKILLS
    NoteSection.HISTORY.id -> NoteSection.HISTORY
    NoteSection.EDUCATION.id -> NoteSection.EDUCATION
    else -> NoteSection.ALL
}