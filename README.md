# AI Language Tutor

AI Language Tutor is an Android application designed to help users learn Chinese through interactive AI-powered features. The app integrates advanced language models (via OpenRouter) and Text-to-Speech (TTS) capabilities to provide a comprehensive learning experience.

## Features

*   **AI Roleplay Chat**: Engage in natural conversations with an AI tutor in Chinese.
    *   **Translation**: Translate user messages to Chinese and AI responses to English.
    *   **Text-to-Speech**: Listen to the AI's Chinese responses with a single tap.
*   **Pronunciation Test**: Practice speaking Chinese phrases.
    *   **Speech Recognition**: Uses the device's speech recognizer to transcribe your pronunciation.
    *   **AI Feedback**: Get detailed feedback and scoring on your pronunciation from the AI.
    *   **AI Sentence Generation**: Generate example sentences using specific words, complete with Pinyin and English translation.
    *   **Listen & Learn**: Hear the correct pronunciation of phrases and generated sentences.
*   **Vocabulary Builder**: Generate custom vocabulary lists based on categories (e.g., Greetings, Food, Travel) and proficiency levels (HSK).
*   **Grammar Correction**: Input sentences to get instant grammar corrections and explanations.
*   **Progress Tracking**: (Visual placeholder) View stats like words learned, accuracy, and daily streaks.
*   **Profile & Settings**: Manage user preferences, including a Dark Mode toggle.

## Tech Stack

*   **Platform**: Android (Java)
*   **Networking**: OkHttp for API requests.
*   **AI Integration**: OpenRouter API (Mistral 7B / Google Gemma 2) for chat, feedback, and content generation.
*   **Speech Services**:
    *   `android.speech.SpeechRecognizer` for speech-to-text.
    *   `android.speech.tts.TextToSpeech` for text-to-speech.
*   **UI Components**: Material Design components, RecyclerView, ConstraintLayout.

## Setup & Installation

1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/yourusername/AILanguageTutor.git
    ```
2.  **Open in Android Studio**:
    *   Launch Android Studio.
    *   Select "Open" and navigate to the project directory.
3.  **Configure API Key**:
    *   The project uses OpenRouter for AI features.
    *   Locate the `OPENROUTER_API_KEY` constant in the following files and ensure a valid key is set:
        *   `ChatActivity.java`
        *   `PronunciationTestActivity.java`
        *   `VocabularyBuilderActivity.java`
        *   `GrammarCorrectionActivity.java`
    *   *Note: For security in a production app, use `local.properties` or a secure backend proxy.*
4.  **Permissions**:
    *   The app requires `Internet` and `Record Audio` permissions, which are declared in `AndroidManifest.xml`.
    *   On Android 11+ (API 30+), ensure the Google App is installed/enabled for speech recognition visibility.
5.  **Build and Run**:
    *   Sync Gradle files.
    *   Run the app on an Android Emulator or Physical Device.
    *   *Note: For TTS to work, ensure a Chinese language pack is installed on the device's Text-to-Speech engine.*

## Usage Guide

*   **Chat**: Tap "AI Roleplay Chat" on the home screen. Type a message to start. Tap "Translate" under messages to see translations. Tap the speaker icon to hear the AI.
*   **Pronunciation**: Tap "Pronunciation Test". Select a phrase, tap the microphone button, and speak. Wait for AI analysis. Use "Make a Sentence with AI" to expand your learning.
*   **Vocab**: Tap "Vocabulary Builder" and select a category to get a generated list of words.
*   **Grammar**: Tap "Grammar Correction", type a sentence, and hit "Check Grammar".

## Troubleshooting

*   **API Error 401**: Your API Key is missing or invalid. Update the `OPENROUTER_API_KEY` constant in the relevant activity files.
*   **API Error 404**: The selected AI model might be unavailable. Switch the `MODEL_ID` in the code to a different free model (e.g., `mistralai/mistral-7b-instruct:free`).
*   **Voice Recognition Error**: Ensure you have granted microphone permissions and that the Google App is installed on your device.
*   **No Sound**: Check your device volume and ensure a Text-to-Speech engine (like Google TTS) is installed and supports Chinese.

## License

[MIT License](LICENSE) (or your preferred license)
