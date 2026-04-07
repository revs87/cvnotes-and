# CVNotes AppFunctions integration

This document describes how **CVNotes** exposes [AppFunctions](https://developer.android.com/ai/appfunctions) and how a **separate caller** (agent app, assistant integration, or test harness) can invoke those functions using **`AppFunctionManager`**.

AppFunctions are **experimental** and target the **Android 16+** platform pipeline. See the official [overview](https://developer.android.com/ai/appfunctions) for limitations, permissions, and FAQ.

---

## Architecture: two roles

| Role | Responsibility | Uses `AppFunctionManager`? |
|------|------------------|----------------------------|
| **Host (CVNotes)** | Declares functions, ships schema, handles execution in-process | **No** — CVNotes implements `AppFunctionConfiguration.Provider` on `CVNotesApp` and registers `CVNotesAppFunctions`. |
| **Caller** (your agent/test app) | Discovers metadata and sends execute requests to CVNotes’ package | **Yes** — obtains `AppFunctionManager.getInstance(context)` and calls `executeAppFunction(...)`. |

CVNotes does **not** need a manager to *publish* functions. Only code that **calls** CVNotes from another package needs the manager.

---

## AI agents and assistants: can they trigger CVNotes?

**Yes.** [AppFunctions](https://developer.android.com/ai/appfunctions) are explicitly meant so that **the system and AI agents / assistants** can **discover** and **invoke** app capabilities on the device. CVNotes registers its tools (`CVNotesAppFunctions`); agents use the same platform pipeline as any other caller.

### How agents relate to `AppFunctionManager`

| Scenario | What happens |
|----------|----------------|
| **System or Google assistant–style agent** | The platform (or assistant stack) uses the AppFunctions runtime to match user intent to indexed functions and execute them in the target app. You do **not** embed custom code inside CVNotes for this path beyond **exposing** functions and schema. |
| **Your own agent app** | Your APK uses **`AppFunctionManager`** (see [Caller app: execute a function](#caller-app-execute-a-function-pattern)) to query metadata and call `executeAppFunction` against CVNotes’ package name. |
| **Hybrid assistants** | Docs describe agents combining **server-side tools** (e.g. remote MCP-style tools) with **on-device AppFunctions** so both cloud and local apps can participate in one workflow. |

### Permissions and trust

- Callers that discover or execute functions in **other** packages need the appropriate platform permission (typically **`EXECUTE_APP_FUNCTIONS`**), as noted in the [official overview](https://developer.android.com/ai/appfunctions).
- Only **authorized** callers should be able to drive CVNotes; treat exposed functions like a public API surface and keep validation in `CVNotesAppFunctions`.

### Experimental / production caveats

During the **experimental** preview, Google may **limit** which **system agents** and apps can use the **full** end-to-end pipeline in **production**. Development and testing are still supported; for broader production integration, see Google’s **FAQ** and **Early Access Program (EAP)** on the [AppFunctions overview](https://developer.android.com/ai/appfunctions).

### Gemini app: can I trigger CVNotes with a natural-language query?

**Sometimes, in principle — but not something CVNotes can promise by itself.**

The platform direction is that **on-device assistants** (including experiences related to **Gemini** on Android) can participate in the same **AppFunctions** pipeline as other agents, so a user query *could* be interpreted and routed to **local app tools** (like CVNotes’ `listSections`, `addTextNote`, etc.) **when** the **Gemini + Android stack** actually wires that query to indexed AppFunctions for **third-party** apps.

**What CVNotes does *not* control**

- Whether the **Gemini app** (or a specific Gemini surface on your device) **discovers**, **surfaces**, or **executes** CVNotes functions for arbitrary typed queries.
- That is determined by **Google’s product and platform rollout** (see the [FAQ](https://developer.android.com/ai/appfunctions): experimental phase, **limited** system agents, EAP for broader access).

**What must be true for a query → CVNotes path to work (when supported)**

| Requirement | Notes |
|-------------|--------|
| Device / OS | **Android 16+** AppFunctions pipeline available on the device. |
| CVNotes installed | Release or debug package installed; schema present (verify with `adb shell cmd app_function list-app-functions`). |
| Assistant integration | The **Gemini (or assistant) build** you use must **participate** in AppFunctions for **third-party** apps like CVNotes — not guaranteed for all users or all Gemini entry points during the preview. |

**Practical takeaway**

- **Do not rely** on “I typed a question in Gemini and CVNotes always runs” without confirming on a **supported** OS/Gemini combination and current Google documentation.
- Until first-party Gemini routing is broadly documented for your app category, prefer: **your own agent app** using [`AppFunctionManager`](#caller-app-execute-a-function-pattern), **in-app** actions, or any **official** integration path Google documents for AppFunctions.

If an agent never reaches CVNotes, check: CVNotes installed, **Android 16+** device, correct **package name**, indexed schema (`adb shell cmd app_function list-app-functions`), and current platform/agent eligibility.

---

## Requirements

- **Device / API**: AppFunctions are designed for **Android 16 (API 36)** and the AppFunctions system service. Older devices may not support the full pipeline.
- **CVNotes build**: Release and debug variants use different **application IDs** (see below). Callers must target the installed package.
- **Experimental APIs**: Jetpack `androidx.appfunctions` is in **alpha**; binary and metadata formats may change between releases.

---

## CVNotes application IDs

| Build | Typical `applicationId` |
|-------|-------------------------|
| Release | `pt.rvcoding.cvnotes` |
| Debug | `pt.rvcoding.cvnotes.debug` |

Use the same package name in `ExecuteAppFunctionRequest` that matches the installed APK.

---

## Functions exposed by CVNotes

Implementation: `app/src/main/java/pt/rvcoding/cvnotes/appfunctions/CVNotesAppFunctions.kt`.

All functions take a leading **`AppFunctionContext`** (injected by the platform). Parameter names below match the Kotlin API; **function identifiers** for `ExecuteAppFunctionRequest` come from **generated metadata** (see [Discovery](#discovery)).

### Navigation (UI)

These queue a destination and bring **MainActivity** to the foreground; navigation is applied when `NavController` is on the **Home** graph (see `PendingNavigationHolder`).

| Kotlin function | Parameters | Return |
|-----------------|------------|--------|
| `navigateToDashboard` | — | `AppFunctionOperationResult` |
| `navigateToSectionDetails` | `sectionId: Int` | `AppFunctionOperationResult` |
| `navigateToNewNote` | `sectionId: Int` | `AppFunctionOperationResult` |
| `navigateToEditNote` | `sectionId: Int`, `noteId: Long` | `AppFunctionOperationResult` |

### Sections & notes (data)

| Kotlin function | Parameters | Return |
|-----------------|------------|--------|
| `addSection` | `sectionName: String`, `sectionTypeId: Int` (default: Other = **7**) | `AppFunctionOperationResult` |
| `addTextNote` | `sectionId: Int`, `content: String` | `AppFunctionOperationResult` |
| `updateSectionName` | `sectionId: Int`, `newName: String` | `AppFunctionOperationResult` |
| `updateNoteContents` | `noteId: Long`, `content1: String?`, `content2: String?` | `AppFunctionOperationResult` |

### Discovery (ids for agents)

| Kotlin function | Parameters | Return |
|-----------------|------------|--------|
| `listSections` | — | `List<AppFunctionSectionSummary>?` (null if empty) |
| `listNotesForSection` | `sectionId: Int` | `List<AppFunctionNoteSummary>?` (null if empty) |

Serializable DTOs: `AppFunctionOperationResult`, `AppFunctionSectionSummary`, `AppFunctionNoteSummary` in `appfunctions/AppFunctionModels.kt`.

---

## Caller app: Gradle

Add the same Jetpack **client** library version you use elsewhere (example version shown; align with `gradle/libs.versions.toml` in CVNotes):

```kotlin
dependencies {
    implementation("androidx.appfunctions:appfunctions:1.0.0-alpha08")
}
```

You do **not** need `appfunctions-service` or `appfunctions-compiler` on the caller unless you also expose functions from that app.

---

## Caller app: manifest permission

Callers that query or execute functions in **other** packages typically need:

```xml
<uses-permission android:name="android.permission.EXECUTE_APP_FUNCTIONS" />
```

Exact requirements follow the platform and your target SDK; confirm in the [official documentation](https://developer.android.com/ai/appfunctions).

---

## Discovery: function identifiers and parameters

Do **not** hard-code opaque strings without checking metadata.

1. **On device** (documentation often cites):

   ```bash
   adb shell cmd app_function list-app-functions
   ```

2. **In code**: subscribe to metadata for CVNotes’ package:

```kotlin
val manager = AppFunctionManager.getInstance(context)
val spec = AppFunctionSearchSpec.Builder()
    .addPackageName("pt.rvcoding.cvnotes") // or .debug
    .build()

manager.observeAppFunctions(spec).collect { packages ->
    // Inspect AppFunctionPackageMetadata / function ids and parameter schemas
}
```

Use the reported **function identifiers** and **parameter layouts** when building `AppFunctionData` for `ExecuteAppFunctionRequest`.

---

## Caller app: execute a function (pattern)

1. Obtain the manager: `AppFunctionManager.getInstance(applicationContext)`.
2. Optionally: `isAppFunctionEnabled(targetPackage, functionId)` (API shape may vary slightly by library version).
3. Build **`AppFunctionData`** for the target function’s parameters (names/types must match the schema).
4. Build **`ExecuteAppFunctionRequest`** with:
   - target package name (CVNotes),
   - function identifier from discovery,
   - parameters.
5. **Suspend** call: `manager.executeAppFunction(request)` (inside `viewModelScope`, `lifecycleScope`, or `runBlocking` in tests).

### Illustrative example (placeholders)

The builder overloads for `AppFunctionData` and the exact **`functionIdentifier`** strings are generated from your KSP schema; replace placeholders after discovery.

```kotlin
import androidx.appfunctions.AppFunctionData
import androidx.appfunctions.AppFunctionManager
import androidx.appfunctions.ExecuteAppFunctionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun exampleListSections(
    callerContext: android.content.Context,
    cvNotesPackage: String,
    listSectionsFunctionId: String,
): androidx.appfunctions.ExecuteAppFunctionResponse = withContext(Dispatchers.IO) {
    val manager = AppFunctionManager.getInstance(callerContext)
    // Parameters for listSections: only AppFunctionContext (platform-injected) — often empty payload for user args:
    val params = AppFunctionData.Builder(
        "REPLACE_WITH_NAMESPACE_FROM_METADATA",
        "REPLACE_WITH_OBJECT_ID_FROM_METADATA",
    ).build()

    val request = ExecuteAppFunctionRequest(
        cvNotesPackage,
        listSectionsFunctionId,
        params,
    )
    manager.executeAppFunction(request)
}
```

Example with **arguments** (shape illustrative):

```kotlin
suspend fun exampleAddTextNote(
    callerContext: android.content.Context,
    cvNotesPackage: String,
    addTextNoteFunctionId: String,
    sectionId: Int,
    content: String,
): androidx.appfunctions.ExecuteAppFunctionResponse = withContext(Dispatchers.IO) {
    val manager = AppFunctionManager.getInstance(callerContext)
    val params = AppFunctionData.Builder(
        "REPLACE_WITH_NAMESPACE_FROM_METADATA",
        "REPLACE_WITH_OBJECT_ID_FROM_METADATA",
    )
        .setInt("sectionId", sectionId)
        .setString("content", content)
        .build()

    val request = ExecuteAppFunctionRequest(
        cvNotesPackage,
        addTextNoteFunctionId,
        params,
    )
    manager.executeAppFunction(request)
}
```

Parse **`ExecuteAppFunctionResponse`** according to the alpha API you use (response types may wrap success payloads or errors; refer to `androidx.appfunctions` Javadoc for your exact artifact version).

---

## CVNotes host wiring (reference)

For contributors maintaining the app:

- **`CVNotesApp`**: implements `AppFunctionConfiguration.Provider`, returns `AppFunctionConfiguration` mapping `CVNotesAppFunctions::class.java` to a Hilt-provided instance (`AppFunctionsEntryPoint`).
- **KSP**: `appfunctions-compiler` generates schema / inventory consumed by the system.
- **Manifest**: the `appfunctions-service` artifact merges **`PlatformAppFunctionService`** and related metadata (you normally do not declare this service by hand).
- **ProGuard**: rules in `app/proguard-rules.pro` keep `CVNotesAppFunctions` and `AppFunction*` models for release.

---

## Troubleshooting

| Issue | Suggestion |
|-------|------------|
| Caller cannot see functions | Confirm CVNotes is installed, device supports the feature, correct **package name** (debug vs release), and `EXECUTE_APP_FUNCTIONS` where required. |
| Wrong or empty function ids | Use **`observeAppFunctions`** or **`adb shell cmd app_function list-app-functions`**; do not guess identifiers. |
| Navigation does not open the right screen | Navigation runs only after the user reaches the **Home** graph; cold start may still show Splash/Auth first. |
| Experimental / EAP limits | Google may restrict which agents can call AppFunctions in production during the preview; see [FAQ](https://developer.android.com/ai/appfunctions). |
| Assistant / agent never invokes CVNotes | Confirm device and agent eligibility for AppFunctions, CVNotes installed, schema listed via `adb`, and that the user flow routes to **local** tools (not only cloud tools). |

---

## Further reading

- [Overview of AppFunctions](https://developer.android.com/ai/appfunctions)
- [Jetpack `appfunctions` release notes](https://developer.android.com/jetpack/androidx/releases/appfunctions)
