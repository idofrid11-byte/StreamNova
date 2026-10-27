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
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    val debridApiKey: String = "",
    val debridProvider: String = "Real-Debrid",
    val maxCacheSizeMb: Int = 1024,
    val subtitleSize: Int = 18,
    val subtitleColor: String = "White",
    val hardwareAcceleration: Boolean = true,
    val autoPlayNext: Boolean = true,
    val preferredQuality: String = "1080p",
    val filterAdultContent: Boolean = true
)
