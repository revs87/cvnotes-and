package pt.android.cvnotes.domain.util


enum class SectionType(val id: Int, val sectionName: String, val color: Int = 0) {
    NONE(-1, ""),
    ALL(0, ""),
    PROFILE(1, "Profile"),
    SUMMARY(2, "Professional Summary"),
    EXPERIENCE(3, "Experience"),
    SKILLS(4, "Skills"),
    HISTORY(5, "Work History"),
    EDUCATION(6, "Education"),
}

fun Int.toSectionType(): SectionType = when (this) {
    SectionType.PROFILE.id -> SectionType.PROFILE
    SectionType.SUMMARY.id -> SectionType.SUMMARY
    SectionType.EXPERIENCE.id -> SectionType.EXPERIENCE
    SectionType.SKILLS.id -> SectionType.SKILLS
    SectionType.HISTORY.id -> SectionType.HISTORY
    SectionType.EDUCATION.id -> SectionType.EDUCATION
    else -> SectionType.ALL
}