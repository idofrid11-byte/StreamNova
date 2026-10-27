package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [AddonEntity::class, LibraryEntity::class, UserEntity::class, SettingsEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun addonDao(): AddonDao
    abstract fun libraryDao(): LibraryDao
    abstract fun userDao(): UserDao
    abstract fun settingsDao(): SettingsDao

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
                            database.addonDao().insertAddons(
                                listOf(
                                    AddonEntity(
                                        manifestUrl = "https://v3-cinemeta.strem.io/manifest.json",
                                        id = "community.cinemeta",
                                        name = "Cinemeta Official",
                                        version = "3.0.12",
                                        description = "Official Cinemeta Catalog for IMDb movies, series, trending, and top rated content.",
                                        isEnabled = true,
                                        isDeletable = false
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
                                    ),
                                    AddonEntity(
                                        manifestUrl = "https://torrentio.strem.fun/manifest.json",
                                        id = "com.stremio.torrentio.addon",
                                        name = "Torrentio Streams",
                                        version = "1.0.14",
                                        description = "Scrapes torrent & debrid streams with multiple quality resolutions and audio tracks.",
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
