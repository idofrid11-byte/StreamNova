# StreamNova Android APK

This directory contains the compiled Android Application Package (APK) for **StreamNova**:

- **File**: `StreamNova-v1.0.apk` / `app-debug.apk`
- **Application ID**: `com.aistudio.streamnova.kxvpmz`
- **Target SDK**: Android 16 (API 36 / 37)
- **Minimum SDK**: Android 7.0 (API 24)
- **Architecture**: Universal (arm64-v8a, armeabi-v7a, x86_64)

### How to Install on Device / Emulator

1. **Direct Install (Android Device)**:
   - Transfer `StreamNova-v1.0.apk` to your phone via USB, Google Drive, or local download.
   - Tap the APK file and select "Install" (enable *Install unknown apps* if prompted).

2. **Via ADB (Command Line)**:
   ```bash
   adb install -r apk/StreamNova-v1.0.apk
   ```

3. **Rebuilding the APK**:
   To regenerate or update the APK anytime after code changes:
   ```bash
   gradle assembleDebug
   cp app/build/outputs/apk/debug/app-debug.apk apk/StreamNova-v1.0.apk
   ```
