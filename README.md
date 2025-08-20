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
