# AI Content Summarizer (Gemini API Integration)

This project features an AI-powered Content Summarization tool built with Kotlin Multiplatform and Gemini 1.5 Flash.

## Features
- **Smart Summarization**: Uses Gemini API to condense long text into concise summaries.
- **Responsive UI**: Built with Compose Multiplatform, featuring loading states and error handling.
- **Robust Networking**: Uses Ktor for multiplatform API requests.
- **MVVM Architecture**: Clean separation of concerns with ViewModels and UI States.

## Gemini API Integration
The integration uses the following components:
- `GeminiService`: Handles communication with the Google Generative Language API.
- `SummarizationViewModel`: Manages the state of the summarization process (Idle, Loading, Success, Error).
- `System Prompt`: A well-designed prompt ensures the AI acts as a professional summarizer.

## Setup
To use this feature, ensure you have an active internet connection. The API key is already configured in `GeminiService.kt` (for demonstration purposes).

---

## Original Project Info
This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.

### Build and Run Android Application
- Windows: `.\gradlew.bat :composeApp:assembleDebug`

### Build and Run Desktop (JVM) Application
- Windows: `.\gradlew.bat :composeApp:run`

### Build and Run iOS Application
Open the [/iosApp](./iosApp) directory in Xcode and run it.
