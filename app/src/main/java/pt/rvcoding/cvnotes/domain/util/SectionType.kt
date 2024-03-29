package pt.rvcoding.cvnotes.domain.util


enum class SectionType(val typeId: Int, val sectionName: String, val color: Int = 0) {
    NONE(-1, ""),
    ALL(0, ""),
    PROFILE(1, "Profile"),
    SUMMARY(2, "Professional Summary"),
    EXPERIENCE(3, "Experience"),
    SKILLS(4, "Skills"),
    HISTORY(5, "Work History"),
    EDUCATION(6, "Education"),
    OTHER(7, "Other"),
    RUI_VIEIRA_HARDCODED_DATA(8, "Rui Vieira's Template"),
}

fun Int.toSectionType(): SectionType = when (this) {
    SectionType.PROFILE.typeId -> SectionType.PROFILE
    SectionType.SUMMARY.typeId -> SectionType.SUMMARY
    SectionType.EXPERIENCE.typeId -> SectionType.EXPERIENCE
    SectionType.SKILLS.typeId -> SectionType.SKILLS
    SectionType.HISTORY.typeId -> SectionType.HISTORY
    SectionType.EDUCATION.typeId -> SectionType.EDUCATION
    SectionType.OTHER.typeId -> SectionType.OTHER
    SectionType.RUI_VIEIRA_HARDCODED_DATA.typeId -> SectionType.RUI_VIEIRA_HARDCODED_DATA
    else -> SectionType.ALL
}