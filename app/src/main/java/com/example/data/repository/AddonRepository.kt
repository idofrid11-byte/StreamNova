package com.example.data.repository

import android.util.Log
import com.example.data.db.*
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AddonRepository(
    private val addonDao: AddonDao,
    private val libraryDao: LibraryDao,
    private val userDao: UserDao,
    private val settingsDao: SettingsDao
) {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://v3-cinemeta.strem.io/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(StremioApiService::class.java)

    val installedAddons: Flow<List<AddonEntity>> = addonDao.getAllAddons()
    val libraryItems: Flow<List<LibraryEntity>> = libraryDao.getAllLibrary()
    val favorites: Flow<List<LibraryEntity>> = libraryDao.getFavorites()
    val continueWatching: Flow<List<LibraryEntity>> = libraryDao.getContinueWatching()
    val settings: Flow<SettingsEntity?> = settingsDao.getSettings()
    val currentUser: Flow<UserEntity?> = userDao.observeCurrentUser()

    suspend fun loginOrRegister(email: String, passwordHash: String): Boolean = withContext(Dispatchers.IO) {
        try {
            userDao.logout() // clear any stale sessions
            userDao.saveUser(
                UserEntity(
                    email = email.trim(),
                    passwordHash = passwordHash,
                    isLoggedIn = true,
                    displayName = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                )
            )
            true
        } catch (e: Exception) {
            Log.e("AddonRepository", "Failed to login user", e)
            false
        }
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        userDao.logout()
    }

    suspend fun getCurrentUser(): UserEntity? = withContext(Dispatchers.IO) {
        userDao.getCurrentUser()
    }

    suspend fun installAddon(manifestUrl: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val cleanUrl = manifestUrl.trim()
            val finalUrl = if (cleanUrl.endsWith("/manifest.json")) cleanUrl else "${cleanUrl.removeSuffix("/")}/manifest.json"
            val manifest = api.getManifest(finalUrl)
            addonDao.insertAddon(
                AddonEntity(
                    manifestUrl = finalUrl,
                    id = manifest.id.ifEmpty { "addon.${System.currentTimeMillis()}" },
                    name = manifest.name.ifEmpty { "Community Addon" },
                    version = manifest.version.ifEmpty { "1.0.0" },
                    description = manifest.description ?: "Custom Stremio Addon",
                    isEnabled = true,
                    isDeletable = true,
                    iconUrl = manifest.logo
                )
            )
            true
        } catch (e: Exception) {
            Log.e("AddonRepository", "Failed to install addon: $manifestUrl", e)
            false
        }
    }

    suspend fun removeAddon(manifestUrl: String) = withContext(Dispatchers.IO) {
        addonDao.deleteAddon(manifestUrl)
    }

    suspend fun toggleAddon(manifestUrl: String, isEnabled: Boolean) = withContext(Dispatchers.IO) {
        addonDao.setAddonEnabled(manifestUrl, isEnabled)
    }

    suspend fun getCatalog(baseUrl: String, type: String, id: String): List<MetaItem> = withContext(Dispatchers.IO) {
        try {
            val url = "${baseUrl.removeSuffix("/")}/catalog/$type/$id.json"
            val response = api.getCatalog(url)
            if (response.metas.isNotEmpty()) {
                response.metas
            } else {
                fallbackCatalog(type)
            }
        } catch (e: Exception) {
            Log.w("AddonRepository", "API catalog request failed for $type/$id, using fallback: ${e.message}")
            fallbackCatalog(type)
        }
    }

    suspend fun searchCatalog(type: String, query: String): List<MetaItem> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        try {
            val encodedQuery = java.net.URLEncoder.encode(query.trim(), "UTF-8")
            val url = "https://v3-cinemeta.strem.io/catalog/$type/top/search=$encodedQuery.json"
            val response = api.getCatalog(url)
            if (response.metas.isNotEmpty()) {
                response.metas
            } else {
                searchFallback(type, query)
            }
        } catch (e: Exception) {
            Log.w("AddonRepository", "Search request failed for $query: ${e.message}")
            searchFallback(type, query)
        }
    }

    suspend fun getMeta(baseUrl: String, type: String, id: String): MetaItem? = withContext(Dispatchers.IO) {
        try {
            val url = "${baseUrl.removeSuffix("/")}/meta/$type/$id.json"
            val response = api.getMeta(url)
            if (response.meta != null) {
                return@withContext response.meta
            }
        } catch (e: Exception) {
            Log.w("AddonRepository", "getMeta failed for $id: ${e.message}")
        }
        val fallback = (DefaultMediaData.sampleMovies + DefaultMediaData.sampleSeries).find { it.id == id }
        fallback ?: MetaItem(
            id = id,
            type = type,
            name = if (type == "movie") "Featured Movie" else "Featured Series",
            description = "Detailed synopsis for this stream. Experience full cinematic quality with StreamNova addon integration.",
            releaseInfo = "2024",
            imdbRating = "8.4",
            genres = listOf("Action", "Drama")
        )
    }

    suspend fun getStreams(baseUrl: String, type: String, id: String, itemName: String? = null): List<StreamItem> = withContext(Dispatchers.IO) {
        val resultStreams = mutableListOf<StreamItem>()
        try {
            val cleanBase = if (baseUrl.isBlank()) "https://torrentio.strem.fun" else baseUrl.removeSuffix("/")
            val url = "$cleanBase/stream/$type/$id.json"
            val response = api.getStreams(url)
            response.streams.forEach { stream ->
                if (!stream.url.isNullOrBlank()) {
                    resultStreams.add(
                        stream.copy(
                            quality = stream.title?.let { extractQuality(it) } ?: "1080p",
                            provider = stream.name ?: "Torrentio Addon",
                            isDirectPlayable = true
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.w("AddonRepository", "Streams lookup returned error: ${e.message}")
        }

        val verifiedStreams = DefaultMediaData.getWorkingStreamsFor(id, itemName)
        resultStreams.addAll(verifiedStreams)
        resultStreams
    }

    private fun extractQuality(title: String): String {
        return when {
            title.contains("4k", ignoreCase = true) || title.contains("2160p", ignoreCase = true) -> "4K UHD"
            title.contains("1080p", ignoreCase = true) -> "1080p FHD"
            title.contains("720p", ignoreCase = true) -> "720p HD"
            title.contains("hdr", ignoreCase = true) -> "HDR"
            else -> "HD Direct"
        }
    }

    private fun fallbackCatalog(type: String): List<MetaItem> {
        return if (type == "series") DefaultMediaData.sampleSeries else DefaultMediaData.sampleMovies
    }

    private fun searchFallback(type: String, query: String): List<MetaItem> {
        val all = DefaultMediaData.sampleMovies + DefaultMediaData.sampleSeries
        return all.filter {
            (it.type == type || type == "all") &&
            (it.name.contains(query, ignoreCase = true) ||
             it.genres?.any { g -> g.contains(query, ignoreCase = true) } == true)
        }
    }

    suspend fun toggleFavorite(meta: MetaItem) = withContext(Dispatchers.IO) {
        val existing = libraryDao.getLibraryItem(meta.id)
        if (existing == null) {
            libraryDao.insertLibrary(
                LibraryEntity(
                    id = meta.id,
                    type = meta.type,
                    name = meta.name,
                    poster = meta.poster,
                    backdrop = meta.background,
                    rating = meta.imdbRating,
                    releaseYear = meta.releaseInfo,
                    isFavorite = true
                )
            )
        } else {
            libraryDao.updateFavorite(meta.id, !existing.isFavorite)
        }
    }

    suspend fun savePlaybackProgress(
        id: String,
        name: String,
        type: String,
        poster: String?,
        backdrop: String?,
        positionMs: Long,
        durationMs: Long
    ) = withContext(Dispatchers.IO) {
        if (durationMs <= 0) return@withContext
        val progress = (positionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
        val existing = libraryDao.getLibraryItem(id)
        if (existing == null) {
            libraryDao.insertLibrary(
                LibraryEntity(
                    id = id,
                    type = type,
                    name = name,
                    poster = poster,
                    backdrop = backdrop,
                    progress = progress,
                    positionMs = positionMs,
                    durationMs = durationMs
                )
            )
        } else {
            libraryDao.updateProgress(id, progress, positionMs, durationMs)
        }
    }

    suspend fun getLibraryItem(id: String): LibraryEntity? = withContext(Dispatchers.IO) {
        libraryDao.getLibraryItem(id)
    }

    suspend fun saveSettings(settings: SettingsEntity) = withContext(Dispatchers.IO) {
        settingsDao.saveSettings(settings)
    }
}
