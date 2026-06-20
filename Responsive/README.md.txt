# Responsive Android Application

A modern, responsive Android application built with Java that demonstrates seamless orientation handling between Portrait and Landscape modes using native Android resource qualifiers.

## Features

- **Automatic Responsive Layout**:
  - **Portrait Mode**: Displays content in a vertical stack.
  - **Landscape Mode**: Displays content side-by-side horizontally.
- **Modern UI/UX**: Designed with a sleek "Travel & Lifestyle" aesthetic.
  - Dark Navy color palette (#1A1A2E, #16213E, #0F0F1A).
  - High-quality image integration using Picasso.
  - Custom gradient overlays for text readability.
  - Interactive buttons with ripple effects and rounded corners.
- **Efficient Image Loading**: Utilizes the Picasso library to fetch and cache images from external URLs.
- **No Manual Orientation Logic**: Layout transitions are handled entirely by the Android system via `res/layout` and `res/layout-land` directories.

## Tech Stack

- **Language**: Java
- **UI Framework**: Android XML (LinearLayout, FrameLayout)
- **Library**: Picasso 2.8 (for image loading)
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)

## How to Run

1. Clone or download the project.
2. Open the project in **Android Studio**.
3. Ensure you have an internet connection (required to load images).
4. Build and run the app on an emulator or a physical device.
5. Rotate your device to see the layout adjust automatically.

## Project Structure

- `res/layout/activity_main.xml`: Defines the vertical layout for portrait mode.
- `res/layout-land/activity_main.xml`: Defines the horizontal layout for landscape mode.
- `MainActivity.java`: Contains the logic for initializing Picasso and loading images.
- `res/values/colors.xml`: Contains the custom color palette definitions.

---
© 2026 Deny Jeremia Siahaan. All rights reserved.
