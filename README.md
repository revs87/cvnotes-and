[![Static Badge](https://img.shields.io/badge/release%20-%20v1.1.1%20-%20%231082C3)](https://github.com/revs87/cvnotes-and/releases/tag/v1.1.1)

![Static Badge](https://img.shields.io/badge/License%20-%20Apache%202.0%20-%20%231082C3)

![image](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![image](https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black)


# [CVNotes - A snappy and customizable CV editor](https://play.google.com/store/apps/details?id=pt.rvcoding.cvnotes)
Thinking through CV writing can be challenging given its complexity and external circumstances.

With this app you can have an immediate CV Hands-on when an idea comes to mind.

**Update your CV in the most available and flexible way!**

![cvnotes_wide](https://github.com/revs87/cvnotes-and/assets/556860/0ce3dd39-3b31-4e92-b39b-827c23ec5598)

You can:
- Create and edit CV sections and notes.
- Customize your notes with different note types.
- Generate a simple Resumé exported as a PDF file.

[<img src="https://lh3.googleusercontent.com/q1k2l5CwMV31JdDXcpN4Ey7O43PxnjAuZBTmcHEwQxVuv_2wCE2gAAQMWxwNUC2FYEOnYgFPOpw6kmHJWuEGeIBLTj9CuxcOEeU8UXyzWJq4NJM3lg=s0">](https://play.google.com/store/apps/details?id=pt.rvcoding.cvnotes)


## Project Requirements
Java 17+

The latest Android Studio Hedgehog or above (for easy install use JetBrains Toolbox)

## Tech Stack
### Core
- 100% Kotlin
- 100% Jetpack Compose
- Kotlin Coroutines (structured concurrency)
- Kotlin Flow (reactivity)
- Hilt (DI)
- Nested navigation (Jetpack Compose)

### Local Persistence
- SharedPreferences (key-value storage)
- Room DB (using KSP)

### Networking
- Firebase Auth
- Firebase Crashlytics
