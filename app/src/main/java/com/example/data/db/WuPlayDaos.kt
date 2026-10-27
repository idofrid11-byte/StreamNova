package com.example.data.db

import androidx.room.*
import com.example.data.model.CatalogConfigEntity
import com.example.data.model.WatchHistoryEntity
import com.example.data.model.WatchlistEntity
import com.example.data.model.WuPlayProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WuPlayProfileDao {
    @Query("SELECT * FROM wuplay_profiles ORDER BY name ASC")
    fun getAllProfiles(): Flow<List<WuPlayProfileEntity>>

    @Query("SELECT * FROM wuplay_profiles WHERE isCurrent = 1 LIMIT 1")
    fun getCurrentProfile(): Flow<WuPlayProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: WuPlayProfileEntity)

    @Query("UPDATE wuplay_profiles SET isCurrent = (profileId = :profileId)")
    suspend fun setCurrentProfile(profileId: String)

    @Query("DELETE FROM wuplay_profiles WHERE profileId = :profileId")
    suspend fun deleteProfile(profileId: String)
}

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM wuplay_watchlist WHERE profileId = :profileId ORDER BY addedTimestamp DESC")
    fun getWatchlist(profileId: String): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(item: WatchlistEntity)

    @Query("DELETE FROM wuplay_watchlist WHERE id = :id AND profileId = :profileId")
    suspend fun removeFromWatchlist(id: String, profileId: String)

    @Query("SELECT COUNT(*) > 0 FROM wuplay_watchlist WHERE id = :id AND profileId = :profileId")
    fun isInWatchlist(id: String, profileId: String): Flow<Boolean>
}

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM wuplay_history WHERE profileId = :profileId ORDER BY lastWatched DESC")
    fun getHistory(profileId: String): Flow<List<WatchHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHistory(item: WatchHistoryEntity)

    @Query("DELETE FROM wuplay_history WHERE profileId = :profileId")
    suspend fun clearHistory(profileId: String)
}

@Dao
interface CatalogConfigDao {
    @Query("SELECT * FROM wuplay_catalogs ORDER BY displayOrder ASC")
    fun getCatalogs(): Flow<List<CatalogConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCatalogs(catalogs: List<CatalogConfigEntity>)

    @Query("UPDATE wuplay_catalogs SET isVisible = :isVisible WHERE catalogId = :catalogId")
    suspend fun updateCatalogVisibility(catalogId: String, isVisible: Boolean)
}
