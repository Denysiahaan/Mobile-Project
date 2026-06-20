# Audio Recorder App

A simple and functional Android application for recording audio, managing recordings, and converting text to speech.

## Features

### 🎙️ Audio Recording
*   **High-Quality Recording**: Record audio from your microphone and save it as high-quality MPEG_4/AAC files.
*   **Real-time Visualizer**: See your voice amplitude in real-time with a built-in wave visualizer during recording.
*   **Instant Playback**: Listen to your last recording immediately after stopping.

### 🗣️ Text-to-Speech (TTS)
*   Convert typed text into spoken words.
*   Supports multiple languages (Defaulting to Indonesian and US English).
*   Clean UI for easy text input and playback.

### ⚙️ Settings & History
*   **Dark Mode**: Switch between light and dark themes for a comfortable viewing experience.
*   **Recording History**: View a list of your previous recordings.
*   **Play History**: Play back old recordings directly from the history list.
*   **Clear Data**: Easily delete all recording history with a single tap.

## Tech Stack
*   **Language**: Java
*   **UI Framework**: Android XML with Material Components
*   **Libraries**:
    *   `MediaRecorder` for audio capture
    *   `MediaPlayer` for audio playback
    *   `TextToSpeech` for voice synthesis
    *   `RecyclerView` for history management
    *   `SharedPreferences` for user settings

## Permissions
This app requires the following permissions:
*   `RECORD_AUDIO`: To capture sound through the microphone.

## How to Run
1.  Clone this repository.
2.  Open the project in **Android Studio**.
3.  Build and run the app on an Android device or emulator (API Level 21+).

## License
This project is open-source and available for educational purposes.
© 2026 Deny Jeremia Siahaan. All rights reserved.
