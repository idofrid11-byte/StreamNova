package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mediahoard_profiles")
data class ProfileEntity(
    @PrimaryKey val profileId: String,
    val name: String,
    val avatarUrl: String? = null,
    val pinHash: String? = null,
    val isCurrent: Boolean = false
)

@Entity(tableName = "mediahoard_themes")
data class ThemeConfigEntity(
    @PrimaryKey val id: Int = 1,
    val primaryColorHex: String = "#7C4DFF",
    val backgroundColorHex: String = "#090A10",
    val surfaceColorHex: String = "#131526",
    val fontFamily: String = "Inter",
    val customLogoUrl: String? = null
)

@Entity(tableName = "mediahoard_supabase_sync")
data class CloudSyncConfigEntity(
    @PrimaryKey val id: Int = 1,
    val supabaseUrl: String = "",
    val supabaseAnonKey: String = "",
    val isSyncEnabled: Boolean = false,
    val lastSyncTimestamp: Long = 0L
)

@Entity(tableName = "mediahoard_api_keys")
data class ApiKeysEntity(
    @PrimaryKey val id: Int = 1,
    val tmdbApiKey: String = "",
    val tvdbApiKey: String = "",
    val rpdbApiKey: String = "",
    val fanartApiKey: String = "",
    val mdblistApiKey: String = "",
    val geminiApiKey: String = "",
    val debridService: String = "Real-Debrid",
    val debridApiKey: String = ""
)
