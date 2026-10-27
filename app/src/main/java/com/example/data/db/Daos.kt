package com.example.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AddonDao {
    @Query("SELECT * FROM installed_addons ORDER BY name ASC")
    fun getAllAddons(): Flow<List<AddonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddon(addon: AddonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddons(addons: List<AddonEntity>)

    @Query("DELETE FROM installed_addons WHERE manifestUrl = :url")
    suspend fun deleteAddon(url: String)

    @Query("UPDATE installed_addons SET isEnabled = :isEnabled WHERE manifestUrl = :url")
    suspend fun setAddonEnabled(url: String, isEnabled: Boolean)

    @Query("SELECT COUNT(*) FROM installed_addons")
    suspend fun getAddonCount(): Int
}

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library ORDER BY updatedAt DESC")
    fun getAllLibrary(): Flow<List<LibraryEntity>>

    @Query("SELECT * FROM library WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavorites(): Flow<List<LibraryEntity>>

    @Query("SELECT * FROM library WHERE (positionMs > 0 AND positionMs < durationMs) OR (progress > 0.03 AND progress < 0.95) ORDER BY lastWatchedTimestamp DESC")
    fun getContinueWatching(): Flow<List<LibraryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibrary(item: LibraryEntity)

    @Query("SELECT * FROM library WHERE id = :id")
    suspend fun getLibraryItem(id: String): LibraryEntity?

    @Query("SELECT * FROM library WHERE id = :id")
    fun observeLibraryItem(id: String): Flow<LibraryEntity?>

    @Query("DELETE FROM library WHERE id = :id")
    suspend fun deleteLibrary(id: String)

    @Query("UPDATE library SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE library SET progress = :progress, positionMs = :positionMs, durationMs = :durationMs, lastWatchedTimestamp = :timestamp, updatedAt = :timestamp WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Float, positionMs: Long, durationMs: Long, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user_account WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT * FROM user_account WHERE isLoggedIn = 1 LIMIT 1")
    fun observeCurrentUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("DELETE FROM user_account")
    suspend fun logout()
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SettingsEntity)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettingsSnapshot(): SettingsEntity?
}
