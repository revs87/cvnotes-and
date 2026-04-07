
# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Comment prefixes:
🟢 Added
🟡 Fixed
🟠 Updated
🔵 Upgrade
🔴 Deleted

## | [1.2.2-SNAPSHOT] |

## | [1.2.1] | 2026-04-07 |
#### 2026-04-07
- 🟢 Added kotlinx-collections-immutable dependency
- 🟠 Annotated Section, SectionWithNotes, and DashboardState with Compose Immutable for recomposition stability
- 🔵 Upgraded Kotlin to 2.3.20 and KSP to 2.3.6 (KSP2 line; aligned with current stable toolchain)
- 🔵 Upgraded Android Gradle Plugin to 8.13.2 (minimum required for Kotlin 2.3 per Android Kotlin support matrix; R8/D8 compatibility)
- 🔵 Set Dagger Hilt to 2.57.2 (AGP 8.x; Hilt 2.59+ requires AGP 9+) and added explicit `ksp` dependency on `kotlin-metadata-jvm` aligned with the Kotlin version so Hilt can read Kotlin 2.3 metadata
- 🟢 Added AI button rate limiting (10 clicks per 10 minutes, sliding window) with click timestamps persisted in SharedPreferences via `SharedPreferencesRepository` and a Hilt `EntryPoint` (survives process death)
- 🟢 Added snackbar feedback when the AI rate limit is reached; wired `SnackbarHostState` from dashboard and section-details screens into `AIButton`
- 🟢 Added Jetpack AppFunctions (experimental; Android 16+ pipeline): `CVNotesAppFunctions` for navigation, sections/notes CRUD, and discovery lists; `AppFunctionConfiguration.Provider` on `CVNotesApp`, Hilt `AppFunctionsEntryPoint`, `PendingNavigationHolder` + `MainActivity` destination listener, KSP `appfunctions-compiler`, and ProGuard keeps
- 🟢 Added `docs/APPFUNCTIONS.md` documenting caller integration with `AppFunctionManager`, AI agents, and Gemini / assistant caveats per Google’s AppFunctions overview
#### 2025-09-07
🟠 Updated README file with new screenshots
## | [1.2.0] | 2025-08-20 |
#### 2025-08-20
- 🟠 Updated notes selection UI
- 🟠 Updated sections selection UI
- 🟠 Updated generative AI dedicated button UI
- 🟡 Fixed pdf generation bug
- 🟠 Updated pdf document format
#### 2025-08-19
- 🟢 Added generative AI for note suggestions
- 🟢 Added generative AI dedicated button
- 🟠 Updated README and LICENCE
- 🟡 Fixed notes selection bug
- 🟡 Fixed auth error handling bug
- 🟡 Fixed unique profession key per user bug
#### 2025-08-16
- 🟢 Added generative AI for section name suggestions
- 🟢 Added applicant role context and UI
- 🔵 Upgraded kotlin version (2.2.10) and several libraries
## | [1.1.2] | 2025-07-29 |
#### 2025-07-29
- 🔵 Upgraded kotlin version (2.2.0) and several libraries
- 🔵 Upgraded android targetSdk to 36
- 🟢 Added edge-to-edge UI compliant with android targetSdk 36
- 🔴 Deleted system ui controller accompanist
- 🟠 Updated top bar default state to collapsed
- 🟡 Gradle build properties contents are now sourced externally
#### 2025-07-28
- 🟠 Updated hardcoded content
- 🟢 Changelog format updated
## | [1.1.1] | 2024-08-11 |
#### 2024-08-11
- 🟡 Permissions fixed on Android 9, 10, 11 and 13
- 🟡 Pdf fix
- 🟢 Added KotlinDSL migration
## | [1.1.0] |

[1.2.2-SNAPSHOT]: https://github.com/revs87/cvnotes-and/compare/v1.2.1...dev
[1.2.1]: https://github.com/revs87/cvnotes-and/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/revs87/cvnotes-and/compare/v1.1.2...v1.2.0
[1.1.2]: https://github.com/revs87/cvnotes-and/compare/v1.1.1...v1.1.2
[1.1.1]: https://github.com/revs87/cvnotes-and/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/revs87/cvnotes-and/compare/v1.0.0...v1.1.0
