# AI Language Tutor

Welcome to AI Language Tutor, a modern Android application designed to help users learn Chinese (Mandarin) through a suite of powerful, AI-driven tools. This app leverages various AI models via the OpenRouter API to provide an interactive and effective learning experience.

## ✨ Features

*   **AI Grammar Correction**: Input any Chinese sentence and receive instant feedback on grammatical errors, including a detailed explanation and the corrected sentence.
*   **AI Pronunciation Test**: Practice your speaking skills! Select a phrase, record your voice, and get a comprehensive analysis of your pronunciation from an AI coach, including a score out of 100, word-by-word feedback, and actionable tips.
*   **AI Vocabulary Builder**: Generate dynamic word lists for different categories and HSK levels. Each word comes with its Hanzi, Pinyin, English meaning, and an example sentence.
*   **Text-to-Speech (TTS)**: Hear the correct pronunciation of any AI-generated text or correction by simply tapping on it.
*   **Dark Mode**: A fully functional, persistent dark mode for comfortable learning in any lighting condition.
*   **Modern UI**: A clean, professional, and responsive user interface built with Material Design 3 principles.

## 🛠️ Tech Stack & Architecture

*   **Language**: Java
*   **Platform**: Android (Min SDK 24, Compile SDK 36)
*   **AI Integration**: [OpenRouter API](https://openrouter.ai/) for access to various language models (e.g., Mistral, Gemini).
*   **Networking**: OkHttp for robust and efficient API communication.
*   **Speech Recognition**: Android's native `SpeechRecognizer` for capturing user voice input.
*   **Text-to-Speech**: Android's native `TextToSpeech` engine for audio playback.
*   **UI**: Android Jetpack (ConstraintLayout, CardView), Material Design 3 Components.
*   **Persistence**: SharedPreferences for saving user preferences like the app theme.

## 🚀 Setup & Installation

To get the project up and running, follow these steps:

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/ai-language-tutor.git
    ```

2.  **Open in Android Studio**
    *   Launch Android Studio.
    *   Select **File > Open** and navigate to the cloned project directory.

3.  **Add Your OpenRouter API Key**
    *   Sign up on [OpenRouter.ai](https://openrouter.ai/) to get your free API key.
    *   You need to add this key in three places in the project:
        *   `app/src/main/java/com/example/ailanguagetutor/GrammarCorrectionActivity.java`
        *   `app/src/main/java/com/example/ailanguagetutor/VocabularyBuilderActivity.java`
        *   `app/src/main/java/com/example/ailanguagetutor/PronunciationTestActivity.java`
    *   In each file, find the line `private static final String OPENROUTER_API_KEY = "sk-or-...";` and replace the key with your own.

4.  **Sync & Run**
    *   Android Studio should prompt you to sync the Gradle project. If not, go to **File > Sync Project with Gradle Files**.
    *   Connect an Android device or start an emulator.
    *   Click the **Run** button (▶️) to build and install the app.

## ⚠️ Permissions

The app requires the following permissions, which are already declared in the `AndroidManifest.xml`:
*   `android.permission.INTERNET`: To communicate with the OpenRouter API.
*   `android.permission.RECORD_AUDIO`: For the Pronunciation Test feature.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/your-username/ai-language-tutor/issues).

## 📝 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
