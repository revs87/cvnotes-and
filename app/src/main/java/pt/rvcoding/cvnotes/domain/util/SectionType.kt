package pt.rvcoding.cvnotes.domain.util


enum class SectionType(val typeId: Int, val order: Int, val sectionName: String, val color: Int = 0) {
    NONE(-1, -1, ""),
    ALL(0, 0, ""),
    PROFILE(1, 2, "Profile"),
    SUMMARY(2, 3, "Professional Summary"),
    EXPERIENCE(3, 4, "Experience"),
    SKILLS(4, 5, "Skills"),
    HISTORY(5, 6, "Work History"),
    EDUCATION(6, 7, "Education"),
    OTHER(7, 8, "Other"),
    MY_HARDCODED_DATA(8, 9, "Template"),
    AI(9, 1, "Ask AI"),
}

fun Int.toSectionType(): SectionType = when (this) {
    SectionType.PROFILE.typeId -> SectionType.PROFILE
    SectionType.SUMMARY.typeId -> SectionType.SUMMARY
    SectionType.EXPERIENCE.typeId -> SectionType.EXPERIENCE
    SectionType.SKILLS.typeId -> SectionType.SKILLS
    SectionType.HISTORY.typeId -> SectionType.HISTORY
    SectionType.EDUCATION.typeId -> SectionType.EDUCATION
    SectionType.OTHER.typeId -> SectionType.OTHER
    SectionType.MY_HARDCODED_DATA.typeId -> SectionType.MY_HARDCODED_DATA
    else -> SectionType.ALL
}