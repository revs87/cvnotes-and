[![Static Badge](https://img.shields.io/badge/release%20-%20v1.1.1%20-%20%231082C3)](https://github.com/revs87/cvnotes-and/releases/tag/v1.1.1)

![Static Badge](https://img.shields.io/badge/License%20-%20Apache%202.0%20-%20%231082C3)

![image](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![image](https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black)


# [CVNotes - A snappy and customizable CV editor](https://play.google.com/store/apps/details?id=pt.rvcoding.cvnotes)
A snappy and customizable CV editor

[<img src="https://lh3.googleusercontent.com/q1k2l5CwMV31JdDXcpN4Ey7O43PxnjAuZBTmcHEwQxVuv_2wCE2gAAQMWxwNUC2FYEOnYgFPOpw6kmHJWuEGeIBLTj9CuxcOEeU8UXyzWJq4NJM3lg=s0">](https://play.google.com/store/apps/details?id=pt.rvcoding.cvnotes)


## 📜 Summary

Thinking through CV writing can be challenging given its complexity and external circumstances.

With this app you can have an immediate CV Hands-on when an idea comes to mind or when requesting AI to write everything for you.

**Update your CV in the most available and flexible way!**

![cvnotes_wide](https://github.com/revs87/cvnotes-and/assets/556860/0ce3dd39-3b31-4e92-b39b-827c23ec5598)


## 📌 Features

- Create and edit CV sections and notes.
- Customize your notes with different note types.
- Generate a simple Resumé exported as a PDF file.


### 🚀 Technical Highlights

- Android Native
- 100% Kotlin
- 100% Jetpack Compose
- Kotlin Coroutines and Flows
- Nested navigation (Jetpack Compose)
- Firebase Auth
- Generative AI (Gemini LLM)


### 🏛️ Architecture & Design Patterns

- Single module architecture
- Single Activity architecture
- Clean architecture with Presentation, UseCase, Domain and Data layers
- MVVM presentation pattern with StateFlows visible to the UI layer
- MVI unidirectional data flow from Actions to Data and to State
- Dependency Injection applied with Hilt
- Dependency Inversion Principle (DIP) from data to presentation layers
- Single Source of Truth in a Room database


### 📲 UI

- Light mode only
- Jetpack Compose No-type Args navigation
- Updates and Errors are shown in a Snackbar
- Generative AI dedicated button with animation


### 🗄️ Local Persistence

- Room DB (on KSP)
- SharedPreferences (key-value storage)


### 📸 Screenshots

| Splash                                                                                      | Role Selection                                                                             | Sections List                                                                                  |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| ![1-intro](https://github.com/user-attachments/assets/b6a34432-b5c7-43d3-a72e-0b757179ddac) | ![2-role](https://github.com/user-attachments/assets/98995dfb-bb28-4eef-9e62-c5f34327a8e0) | ![3-sections](https://github.com/user-attachments/assets/1f010083-7af5-4719-83b6-ac73d0438f22) |

| Notes List                                                                                  | Note Edit                                                                                  | Document Generation                                                                            |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| ![4-notes](https://github.com/user-attachments/assets/b587602e-81b5-4378-b106-daee41dbce38) | ![5-edit](https://github.com/user-attachments/assets/1106a5a7-d3fd-4514-bc58-f75308b32c34) | ![6-document](https://github.com/user-attachments/assets/6fe22afb-c615-4f22-a371-db729c74953b) |


### 🌐 Networking

- Firebase Auth
- Firebase Crashlytics
- Generative UI (Gemini LLM)


### ☑ Project Requirements

- Java 21+
- The latest Android Studio Hedgehog or above (for easy install use JetBrains Toolbox)
- Minimum SDK: 28 (Android 9)
- Target SDK: 36 (Android 16)
- Kotlin 2.2.10


### ⚙️ Configuration

Run ./gradlew build


## 🧾 License

Check LICENSE from the project.
