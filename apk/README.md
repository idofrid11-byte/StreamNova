# StreamNova / WuPlay & MediaHoard Android APK

This directory contains the compiled Android Application Package (APK) for **StreamNova**:

- **File**: `StreamNova-v1.0.apk` / `app-debug.apk`
- **Application ID**: `com.aistudio.streamnova.kxvpmz`
- **Target SDK**: Android 16 (API 36 / 37)
- **Minimum SDK**: Android 7.0 (API 24)
- **Architecture**: Universal (arm64-v8a, armeabi-v7a, x86_64)

### What's Included in This Release:
1. **Flowvid Next-Gen UI (Hero Banner & Glass Cards)**:
   - `FlowvidTheme`: Deep OLED black (`0xFF0A0A0C`), Neon Cyan (`0xFF00E5FF`), Electric Purple (`0xFF7C4DFF`), and Glass Card Surface (`0xFF141419`)
   - `FlowvidHomeScreen`: Hero showcase banner with IMDb badge, Dolby Atmos tag, genre filter pills (All, Trending, Action, Sci-Fi, Anime, 4K Remux), and `FlowvidCard` glass carousels
   - `FlowvidStreamPickerModal`: Bottom sheet modal with 4K UHD Remux, 1080p BluRay, Debrid [RD+] cache indicators, seeder counts, audio codecs, and instant 1-tap playback
2. **WuPlay Android TV Mode**:
   - `WhoIsWatchingScreen` profile selection with focused TV card scaling
   - 10-foot Android TV UI navigation with `focusRestorer()` and D-pad support
   - Cast & Crew browser, movie trailer launcher via YouTube intent, and similar titles
   - Web Configurator pairing code (`WUP-8492`) with per-profile audio/subtitle language preferences
2. **MediaHoard Multi-Format Hubs**:
   - Live TV & IPTV stream channels (.m3u playback with 24/7 channels)
   - Music & Audio streams (Lossless, Chill Lo-Fi, Synthwave, Classical)
   - Narrated AudioBooks & Podcasts
   - eBooks & Manga reading hub
   - Retro Games & Emulators launcher
   - Cloud Debrid Vault (Cached 4K Remux torrents via Real-Debrid / AllDebrid)
3. **Core Stremio & Media3 Engine**:
   - AIOMetadata, AIOStreams multi-scraper, OpenSubtitles v3, and WatchHub
   - Hardware-accelerated ExoPlayer with custom playback scrubbers and stats
   - Supabase Cloud Database sync & API Keys Vault

### How to Install on Device, Android TV, or Emulator

1. **Android TV & Fire TV (Sideload)**:
   - Copy `StreamNova-v1.0.apk` onto a USB drive or send via *Send Files to TV* app.
   - Install and launch from the TV home screen or apps drawer.

2. **Mobile Device (Direct Install)**:
   - Tap the `StreamNova-v1.0.apk` file and select "Install" (allow *Install unknown apps* if prompted).

3. **Via ADB (Command Line)**:
   ```bash
   adb install -r apk/StreamNova-v1.0.apk
   ```

4. **Rebuilding the APK**:
   ```bash
   gradle assembleDebug
   cp app/build/outputs/apk/debug/app-debug.apk apk/StreamNova-v1.0.apk
   ```
