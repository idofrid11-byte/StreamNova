package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wuplay_profiles")
data class WuPlayProfileEntity(
    @PrimaryKey val profileId: String,
    val name: String,
    val avatarUrl: String? = null,
    val pinCode: String? = null,
    val preferredAudioLang: String = "English",
    val preferredSubtitleLang: String = "English",
    val autoplayNext: Boolean = true,
    val isCurrent: Boolean = false
)

@Entity(tableName = "wuplay_catalogs")
data class CatalogConfigEntity(
    @PrimaryKey val catalogId: String,
    val name: String,
    val type: String, // movie, series
    val isVisible: Boolean = true,
    val displayOrder: Int = 0
)

@Entity(tableName = "wuplay_watchlist")
data class WatchlistEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val type: String,
    val name: String,
    val poster: String?,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wuplay_history")
data class WatchHistoryEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val title: String,
    val poster: String?,
    val progressMs: Long,
    val durationMs: Long,
    val lastWatched: Long = System.currentTimeMillis()
)
