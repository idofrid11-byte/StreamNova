package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installed_addons")
data class AddonEntity(
    @PrimaryKey val manifestUrl: String,
    val id: String = "",
    val name: String,
    val version: String,
    val description: String?,
    val isEnabled: Boolean = true,
    val isDeletable: Boolean = true,
    val iconUrl: String? = null
)

@Entity(tableName = "library")
data class LibraryEntity(
    @PrimaryKey val id: String,
    val type: String,
    val name: String,
    val poster: String?,
    val backdrop: String? = null,
    val rating: String? = null,
    val releaseYear: String? = null,
    val progress: Float = 0f,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val isFavorite: Boolean = false,
    val lastWatchedEpisode: String? = null,
    val lastWatchedTimestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_account")
data class UserEntity(
    @PrimaryKey val email: String,
    val passwordHash: String,
    val isLoggedIn: Boolean = true,
    val displayName: String = "StreamNova Member",
    val memberSince: Long = System.currentTimeMillis()
)

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,

    // --- 1. GENERAL & STARTUP ---
    val appLanguage: String = "English",
    val startupScreen: String = "Discover / Home",
    val launchOnSystemBoot: Boolean = false,
    val backgroundSeeding: Boolean = false,
    val analyticsEnabled: Boolean = true,

    // --- 2. ACCOUNT & PROFILES ---
    val activeProfile: String = "Default User",
    val parentalControlPin: String = "",
    val debridApiKey: String = "",
    val debridProvider: String = "Real-Debrid",

    // --- 3. LIBRARY & CLOUD SYNC ---
    val autoRemoveFinished: Boolean = true,
    val cloudSyncInterval: String = "Daily",
    val defaultSortOrder: String = "Title",
    val posterViewStyle: String = "Grid Cards",

    // --- 4. ADDON MANAGEMENT ---
    val autoUpdateAddons: Boolean = true,
    val verifyAddonSsl: Boolean = true,
    val allowCommunityAddons: Boolean = true,

    // --- 5. PLAYBACK & VIDEO DECORATION ---
    val defaultDecoder: String = "Hardware (HW+)",
    val deinterlaceMode: String = "Auto",
    val hdrToneMapping: Boolean = true,
    val nightModeFilter: Boolean = false,
    val touchGesturesBrightnessVolume: Boolean = true,
    val doubleTapSeekSeconds: Int = 10,
    val completionAction: String = "Return to Details",
    val defaultPlaybackSpeed: Float = 1.0f,
    val autoplayNextEpisode: Boolean = true,
    val statsForNerds: Boolean = false,
    val hardwareAcceleration: Boolean = true,
    val preferredQuality: String = "1080p",

    // --- 6. AUDIO & PASSTHROUGH ---
    val audioLanguage: String = "English",
    val audioOutputMode: String = "Stereo",
    val volumeBoostLevel: Int = 100,
    val audioDelayMs: Int = 0,
    val passthroughDolbyDts: Boolean = false,

    // --- 7. SUBTITLES & OPENSUBTITLES ---
    val autoLoadSubtitles: String = "For Foreign Audio Only",
    val primarySubtitleLang: String = "English",
    val secondarySubtitleLang: String = "None",
    val subtitleFontFamily: String = "Sans-Serif",
    val subtitleSize: Int = 18,
    val subtitleTextColor: String = "White",
    val subtitleTextOpacity: Int = 100,
    val subtitleBgColor: String = "Black",
    val subtitleBgOpacity: Int = 50,
    val subtitlePosition: String = "Bottom",
    val subtitleOffsetMs: Int = 0,
    val openSubtitlesApiKey: String = "",

    // --- 8. STREAMING SERVER & TORRENT P2P ---
    val streamingServerPort: Int = 11470,
    val prebufferSizeMb: Int = 50,
    val maxDownloadSpeedKbps: Int = 0, // 0 = Unlimited
    val maxUploadSpeedKbps: Int = 100,
    val torrentProfile: String = "Default / Fast",
    val torrentTimeoutSec: Int = 30,
    val maxPeersPerTorrent: Int = 50,
    val upnpPortForwarding: Boolean = true,
    val dhtEnabled: Boolean = true,
    val encryptionMode: String = "Preferred",
    val proxyType: String = "None",
    val proxyHost: String = "",
    val proxyPort: Int = 1080,

    // --- 9. STORAGE & CACHE MANAGEMENT ---
    val cacheStorageLocation: String = "Internal Storage",
    val streamingCacheSizeMb: Int = 2048,
    val maxCacheAgeDays: Int = 7,
    val clearMetaCacheOnExit: Boolean = false,

    // --- 10. ACCESSIBILITY & ANDROID TV ---
    val highContrastText: Boolean = false,
    val tvFocusScaleMultiplier: Float = 1.1f,
    val talkBackEnhancements: Boolean = false
)
