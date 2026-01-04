# Lingua Ai

## Project Overview

Lingua Ai is an advanced Android application engineered to provide an immersive and interactive experience for users learning the Chinese language. The application leverages AI-powered services to deliver a suite of tools that target various aspects of language acquisition, including conversation, pronunciation, vocabulary, and grammar.

---

## Core Features

-   **AI-Powered Conversational Practice**: Engage in dynamic role-playing chats with an AI tutor. This feature includes on-demand translation for both user and AI messages and text-to-speech functionality to hear correct pronunciation.

-   **Pronunciation Analysis**: Users can select from an extensive list of phrases to practice their speaking skills. The application provides AI-driven feedback, scoring, and detailed analysis of the user's pronunciation.

-   **Dynamic Sentence Generation**: Generate contextual example sentences from a selected word, complete with Pinyin and an English translation, to better understand its usage.

-   **Vocabulary Expansion**: Build custom vocabulary lists based on HSK proficiency levels and specific categories such as greetings, dining, and travel.

-   **Grammar Correction Engine**: Submit Chinese sentences to receive immediate, AI-powered grammatical analysis, including error identification and correction.

-   **User Customization**: A user profile section allows for application settings management, including a theme toggle for dark mode.

---

## Technical Architecture

-   **Platform**: Android
-   **Language**: Java
-   **Core Libraries**:
    -   `androidx.appcompat`, `androidx.activity`, `androidx.constraintlayout`
    -   `com.google.android.material` for UI components.
    -   `com.squareup.okhttp3:okhttp` for robust and efficient network operations.
-   **AI and Machine Learning Integration**:
    -   Utilizes the **OpenRouter API** to access various large language models for generating chat responses, translations, and grammatical feedback.
-   **Speech and Language Processing**:
    -   `android.speech.SpeechRecognizer` for converting spoken user input into text.
    -   `android.speech.tts.TextToSpeech` for synthesizing and playing back Chinese text.

---

## Setup and Installation Guide

To build and run this project locally, follow these steps:

1.  **Clone the Repository**:
    ```bash
    git clone <your-repository-url>
    ```

2.  **Open in Android Studio**:
    -   Launch Android Studio.
    -   Select "Open" from the welcome screen and navigate to the cloned project directory.

3.  **Configure API Credentials**:
    -   This project requires a valid API key from OpenRouter to function. Obtain a key from [openrouter.ai](https://openrouter.ai/).
    -   The API key is defined as a `String` constant named `OPENROUTER_API_KEY` within the following files. You must replace the placeholder value with your actual key:
        -   `app/src/main/java/com/example/ailanguagetutor/ChatActivity.java`
        -   `app/src/main/java/com/example/ailanguagetutor/ChatAdapter.java`
        -   `app/src/main/java/com/example/ailanguagetutor/PronunciationTestActivity.java`
        -   `app/src/main/java/com/example/ailanguagetutor/VocabularyBuilderActivity.java`
        -   `app/src/main/java/com/example/ailanguagetutor/GrammarCorrectionActivity.java`

    *Note: For production environments, it is strongly recommended to store API keys securely using Gradle properties, a backend service, or the Android Keystore system rather than hardcoding them.* 

4.  **Build and Run**:
    -   Allow Gradle to sync and download the required dependencies.
    -   Select a target device (emulator or physical device) and run the application.
    -   Ensure the target device has a Text-to-Speech engine that supports Chinese (e.g., Google TTS).

---

## Troubleshooting

-   **401 Unauthorized Error**: This indicates an invalid or expired API key. Please verify that the `OPENROUTER_API_KEY` constants are set correctly in all relevant files.

-   **404 Not Found Error**: The specified AI model may be unavailable. The model is defined by the `MODEL_ID` constant in the activity files. Consider switching to a different free-tier model available on OpenRouter.

-   **Rate Limit Exceeded**: This error occurs when the number of API requests exceeds the limit for your key. Please wait for the rate limit to reset or generate a new key.

-   **Speech Recognition Issues**: Ensure the application has been granted microphone permissions. On emulators, verify that the Google App is installed and that the host microphone is properly configured.

-   **Text-to-Speech Failure**: Check device media volume and confirm that a Chinese language pack is installed and enabled in the device's TTS settings.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
