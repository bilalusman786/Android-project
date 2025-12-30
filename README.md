# AI Language Tutor

An Android application designed to help users learn Chinese (Mandarin) using AI-powered features. This app leverages Google's Gemini AI to provide grammar correction, vocabulary building, and more.

## Features

*   **Grammar Correction**: 
    *   Input sentences in Chinese to check for grammar errors.
    *   Receive detailed explanations and corrections powered by the Gemini AI model.
*   **Vocabulary Builder**: (Feature description to be added as it's developed)
*   **Pronunciation Test**: (Feature description to be added as it's developed)
*   **Progress Tracking**: Monitor your learning journey.

## Screenshots

*(Add screenshots of your app here)*

## Tech Stack

*   **Language**: Java
*   **Minimum SDK**: 24
*   **Compile SDK**: 36
*   **AI Integration**: Google Gemini API (`generativeai` SDK)
*   **Networking**: OkHttp
*   **UI Components**: Material Design, ConstraintLayout, CardView

## Setup & Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/AILanguageTutor.git
    ```
2.  **Open in Android Studio:**
    *   Open Android Studio and select "Open an existing Android Studio project".
    *   Navigate to the cloned directory.
3.  **Configure API Key:**
    *   Obtain a Gemini API key from [Google AI Studio](https://aistudio.google.com/).
    *   **Note:** For security, it is recommended to use `local.properties` or a secure build config. Currently, for testing purposes, the key is located in `GrammarCorrectionActivity.java`.
    *   Open `app/src/main/java/com/example/ailanguagetutor/GrammarCorrectionActivity.java`.
    *   Replace `API_KEY` with your actual key.
4.  **Build and Run:**
    *   Sync Gradle files.
    *   Connect an Android device or start an emulator.
    *   Click "Run".

## Troubleshooting

*   **Installation Failed (User Restricted)**: On Xiaomi/Redmi devices, enable "Install via USB" in Developer Options.
*   **404 Model Not Found**: Ensure your API Key is valid, unrestricted (or correctly restricted), and that the "Generative Language API" is enabled in your Google Cloud Console project.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](https://choosealicense.com/licenses/mit/)
