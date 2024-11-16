# Contents
- [Download APK and Codes](#download-apk-and-codes)
- [Versions Used](#versions-used)
- [App Installation Instructions](#app-installation-instructions)
  - [Steps](#steps)
  - [Note](#note)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Screenshots](#screenshots)
  - [App Navigation](#app-navigation)
  - [App Features and Screenshots](#app-features-and-screenshots)

---

## Download APK and Codes

- Please click the link to download the APK and Codes:  
  [Download APK and Codes](https://drive.google.com/drive/folders/1OEKVJLAREm1qEaUsnwEupeFpiY1pWtn8?usp=sharing)

---

## Versions Used
- **Android SDK** - Ladybug  
- **JDK** - JetBrains Runtime 21.0.3  
- **Gradle Version** - 8.9  
- **Android Gradle Plugin (AGP) Version** - 8.7.0  
- **Google Console Web Client ID** - Follow the [official docs](https://developer.android.com/identity/sign-in/credential-manager-siwg) to create it.  
  > **Note:** For testing, the Web Client ID has been exposed in the code, which is not a recommended practice.

---

## App Installation Instructions

### Steps
1. Click the download link above to download the APK file.  
2. Once downloaded, open the APK file on your device.  
3. If prompted, allow the installation from unknown sources.  
4. Follow the on-screen instructions to complete the installation.

### Note
You can trust this app and install it without scanning.

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/03f283a8-6fe8-47db-96a8-59e7cf1781af" width="250">
    </td>
    <td></td>
    <td></td>
  </tr>
</table>

---

## Features
- **Single Activity App**: Runs with just one activity and uses fragments.  
- **Google Sign-In using Credential Manager**: Easily log in with your Google account.  
- **Notes Management**: View, add, edit, and update your notes quickly.  
- **Network Monitoring**: Checks your internet connection for Google Sign-In.  
- **Account Switching**: Switch between logged-in accounts to see saved notes.  
- **Smooth Animations**: Nice transitions between screens for a better experience.

---

## Tech Stack
- **Kotlin, XML**: Languages used for development and layout.  
- **Clean Architecture with MVVM and Repository**: Structured approach with Model-View-ViewModel for better code management.  
- **Room DB**: Stores notes locally on the device.  
- **Shared Preferences**: Tracks the login status of the user.  
- **Staggered Grid RecyclerView**: Displays the notes in a visually appealing staggered layout.  
- **Koin**: Dependency injection library for managing app components.  
- **Coroutines and Flows**: For managing asynchronous tasks and data streams.  
- **Glide**: Efficient image loading and display.  
- **Shimmer**: Shows a loading animation while data is being fetched.  
- **Lottie Animation**: Provides smooth animations for better user experience.  
- **Navigation and Lifecycle Components**: Helps manage app navigation and lifecycle events.

---

## Screenshots

### App Navigation
![app_navigation](https://github.com/user-attachments/assets/b8ce010c-1902-430f-bd54-71b7ade344d7)

### App Features and Screenshots

<table>
  <tr>
    <td align="center">
      <b>Splash Screen</b><br>
      <img src="https://github.com/user-attachments/assets/a48ad14a-6186-44a3-be1e-4ffd6488fe60" width="250">
    </td>
    <td align="center">
      <b>No Internet (Sign In Fragment)</b><br>
      <img src="https://github.com/user-attachments/assets/1d8612c3-1d44-4779-a63f-4c4403aa386d" width="250">
    </td>
    <td align="center">
      <b>Sign In Button when Internet is Available</b><br>
      <img src="https://github.com/user-attachments/assets/ce637216-5c45-45d8-b00d-230426d49919" width="250">
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Dialog While Signing In</b><br>
      <img src="https://github.com/user-attachments/assets/7f4e4932-1084-49a8-8e69-246e5cdfbc97" width="250">
    </td>
    <td align="center">
      <b>Credential Manager</b><br>
      <img src="https://github.com/user-attachments/assets/8b4f5c17-a2be-44c6-b91f-bc8e0fb462f3" width="250">
    </td>
    <td align="center">
      <b>Signing In with Selected Email</b><br>
      <img src="https://github.com/user-attachments/assets/4a95ee5f-8baf-453e-a0f7-e4a5997423f5" width="250">
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Success Dialog After Signing In</b><br>
      <img src="https://github.com/user-attachments/assets/c2bd32ae-88bf-4fc7-8967-cfc56d8700b7" width="250">
    </td>
    <td align="center">
      <b>HomeFragment (User Notes)</b><br>
      <img src="https://github.com/user-attachments/assets/f286e1f6-7f2d-4cd9-8db5-73dce8f739a6" width="250">
    </td>
    <td align="center">
      <b>Viewing Logged-in Users</b><br>
      <img src="https://github.com/user-attachments/assets/727f1106-0503-44d4-aadf-9a403d97e2ee" width="250">
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Switching Users to View Notes</b><br>
      <img src="https://github.com/user-attachments/assets/ff299796-f293-47fb-88cf-3fb40d7d3f8f" width="250">
    </td>
    <td align="center">
      <b>Add Note (AddOrEdit Fragment)</b><br>
      <img src="https://github.com/user-attachments/assets/55a9bfd2-1585-41ba-a4ce-cd6ef017ca30" width="250">
    </td>
    <td align="center">
      <b>Viewing Added Notes</b><br>
      <img src="https://github.com/user-attachments/assets/e5e9ee81-b0c0-4036-9ac7-3d1ae4de47ba" width="250">
    </td>
  </tr>
  <tr>
    <td align="center">
      <b>Edit Note (AddOrEdit Fragment)</b><br>
      <img src="https://github.com/user-attachments/assets/dc30c170-0a27-42b4-a6b7-ac2a1273502a" width="250">
    </td>
    <td align="center">
      <b>Delete Note Confirmation Dialog</b><br>
      <img src="https://github.com/user-attachments/assets/ab8802e5-63e7-4b18-9211-be568d0fd4fe" width="250">
    </td>
    <td align="center">
      <b>Logout Confirmation Dialog</b><br>
      <img src="https://github.com/user-attachments/assets/6fa871fd-d8b7-4f9b-b78d-4b46c8405953" width="250">
    </td>
  </tr>
  <tr>
    <td align="center" colspan="1">
      <b>Staggered Recycler View of Notes</b><br>
      <img src="https://github.com/user-attachments/assets/fbc8841a-722d-42f3-97d5-8077268f8928" width="250">
    </td>
    <td></td>
    <td></td>
  </tr>
</table>


---
# Thank You  😊
