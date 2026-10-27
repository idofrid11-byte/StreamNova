package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ApiKeysEntity
import com.example.data.model.CloudSyncConfigEntity
import com.example.data.model.ProfileEntity
import com.example.data.model.ThemeConfigEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AddonEntity::class,
        LibraryEntity::class,
        UserEntity::class,
        SettingsEntity::class,
        ProfileEntity::class,
        ThemeConfigEntity::class,
        CloudSyncConfigEntity::class,
        ApiKeysEntity::class,
        com.example.data.model.WuPlayProfileEntity::class,
        com.example.data.model.CatalogConfigEntity::class,
        com.example.data.model.WatchlistEntity::class,
        com.example.data.model.WatchHistoryEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addonDao(): AddonDao
    abstract fun libraryDao(): LibraryDao
    abstract fun userDao(): UserDao
    abstract fun settingsDao(): SettingsDao
    abstract fun profileDao(): ProfileDao
    abstract fun themeDao(): ThemeDao
    abstract fun cloudSyncDao(): CloudSyncDao
    abstract fun apiKeysDao(): ApiKeysDao
    abstract fun wuplayProfileDao(): WuPlayProfileDao
    abstract fun watchlistDao(): WatchlistDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun catalogConfigDao(): CatalogConfigDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "streamnova_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getDatabase(context)
                            database.settingsDao().saveSettings(SettingsEntity())
                            database.profileDao().saveProfile(
                                ProfileEntity(
                                    profileId = "profile_main",
                                    name = "Main Hoarder Profile",
                                    isCurrent = true
                                )
                            )
                            database.themeDao().saveThemeConfig(ThemeConfigEntity())
                            database.cloudSyncDao().saveCloudSyncConfig(CloudSyncConfigEntity())
                            database.apiKeysDao().saveApiKeys(ApiKeysEntity())
                            database.wuplayProfileDao().saveProfile(
                                com.example.data.model.WuPlayProfileEntity(
                                    profileId = "profile_living_room",
                                    name = "Living Room TV",
                                    isCurrent = true
                                )
                            )
                            database.wuplayProfileDao().saveProfile(
                                com.example.data.model.WuPlayProfileEntity(
                                    profileId = "profile_master_bed",
                                    name = "Master Bedroom",
                                    isCurrent = false
                                )
                            )
                            database.wuplayProfileDao().saveProfile(
                                com.example.data.model.WuPlayProfileEntity(
                                    profileId = "profile_kids",
                                    name = "Kids Zone",
                                    isCurrent = false
                                )
                            )
                            database.catalogConfigDao().saveCatalogs(
                                listOf(
                                    com.example.data.model.CatalogConfigEntity("cat_top_movies", "Popular Movies", "movie", true, 0),
                                    com.example.data.model.CatalogConfigEntity("cat_top_series", "Trending TV Shows", "series", true, 1),
                                    com.example.data.model.CatalogConfigEntity("cat_anime", "Top Anime", "series", true, 2)
                                )
                            )
                            database.addonDao().insertAddons(
                                listOf(
                                    AddonEntity(
                                        manifestUrl = "https://v3-cinemeta.strem.io/manifest.json",
                                        id = "community.cinemeta",
                                        name = "AIOMetadata Official",
                                        version = "3.0.12",
                                        description = "All-In-One Unified Metadata Catalog for IMDb, TMDB, TVDB & AniList titles.",
                                        isEnabled = true,
                                        isDeletable = false
                                    ),
                                    AddonEntity(
                                        manifestUrl = "https://torrentio.strem.fun/manifest.json",
                                        id = "com.stremio.torrentio.addon",
                                        name = "AIOStreams Multi-Scraper",
                                        version = "1.0.14",
                                        description = "Unified stream aggregator across Real-Debrid, TorBox, Premiumize, and P2P caches.",
                                        isEnabled = true,
                                        isDeletable = true
                                    ),
                                    AddonEntity(
                                        manifestUrl = "https://opensubtitles-v3.strem.io/manifest.json",
                                        id = "org.stremio.opensubtitlesv3",
                                        name = "OpenSubtitles v3",
                                        version = "1.0.0",
                                        description = "Official subtitle addon powered by OpenSubtitles with multi-language captions.",
                                        isEnabled = true,
                                        isDeletable = true
                                    ),
                                    AddonEntity(
                                        manifestUrl = "https://watchhub.strem.io/manifest.json",
                                        id = "community.watchhub",
                                        name = "WatchHub Streams",
                                        version = "1.0.3",
                                        description = "Provider links for Netflix, Prime, HBO, Disney+, and free open streams.",
                                        isEnabled = true,
                                        isDeletable = true
                                    )
                                )
                            )
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
