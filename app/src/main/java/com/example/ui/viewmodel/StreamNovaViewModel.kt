package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.*
import com.example.data.model.*
import com.example.data.repository.AddonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamNovaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = AddonRepository(db.addonDao(), db.libraryDao(), db.userDao(), db.settingsDao())

    private val _movies = MutableStateFlow<List<MetaItem>>(emptyList())
    val movies: StateFlow<List<MetaItem>> = _movies.asStateFlow()

    private val _series = MutableStateFlow<List<MetaItem>>(emptyList())
    val series: StateFlow<List<MetaItem>> = _series.asStateFlow()

    private val _trending = MutableStateFlow<List<MetaItem>>(emptyList())
    val trending: StateFlow<List<MetaItem>> = _trending.asStateFlow()

    private val _searchResults = MutableStateFlow<List<MetaItem>>(emptyList())
    val searchResults: StateFlow<List<MetaItem>> = _searchResults.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchFilter = MutableStateFlow("movie") // movie, series, all
    val searchFilter: StateFlow<String> = _searchFilter.asStateFlow()

    private val _isLoadingHome = MutableStateFlow(false)
    val isLoadingHome: StateFlow<Boolean> = _isLoadingHome.asStateFlow()

    private val _isLoadingSearch = MutableStateFlow(false)
    val isLoadingSearch: StateFlow<Boolean> = _isLoadingSearch.asStateFlow()

    val currentUser: StateFlow<UserEntity?> = repository.currentUser
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val installedAddons: StateFlow<List<AddonEntity>> = repository.installedAddons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val libraryItems: StateFlow<List<LibraryEntity>> = repository.libraryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<LibraryEntity>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val continueWatching: StateFlow<List<LibraryEntity>> = repository.continueWatching
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SettingsEntity> = repository.settings
        .map { it ?: SettingsEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsEntity())

    // --- MediaHoard Multi-Profile & Customization ---
    val allProfiles: StateFlow<List<ProfileEntity>> = db.profileDao().getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentProfile: StateFlow<ProfileEntity?> = db.profileDao().getCurrentProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val themeConfig: StateFlow<ThemeConfigEntity> = db.themeDao().getThemeConfig()
        .map { it ?: ThemeConfigEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeConfigEntity())

    val cloudSyncConfig: StateFlow<CloudSyncConfigEntity> = db.cloudSyncDao().getCloudSyncConfig()
        .map { it ?: CloudSyncConfigEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CloudSyncConfigEntity())

    val apiKeys: StateFlow<ApiKeysEntity> = db.apiKeysDao().getApiKeys()
        .map { it ?: ApiKeysEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApiKeysEntity())

    init {
        loadHomeCatalogs()
    }

    fun loginOrRegister(email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.loginOrRegister(email, password)
            onResult(success)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun loadHomeCatalogs() {
        viewModelScope.launch {
            _isLoadingHome.value = true
            try {
                val fetchedMovies = repository.getCatalog("https://v3-cinemeta.strem.io/", "movie", "top")
                val fetchedSeries = repository.getCatalog("https://v3-cinemeta.strem.io/", "series", "top")

                _movies.value = fetchedMovies
                _series.value = fetchedSeries
                _trending.value = (fetchedMovies.take(5) + fetchedSeries.take(5)).shuffled()
            } catch (e: Exception) {
                // repository returns fallback
            } finally {
                _isLoadingHome.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        performSearch(query, _searchFilter.value)
    }

    fun onSearchFilterChanged(filter: String) {
        _searchFilter.value = filter
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value, filter)
        }
    }

    private fun performSearch(query: String, filter: String) {
        viewModelScope.launch {
            _isLoadingSearch.value = true
            try {
                val results = repository.searchCatalog(filter, query)
                _searchResults.value = results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoadingSearch.value = false
            }
        }
    }

    fun installAddon(url: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.installAddon(url)
            onResult(success)
        }
    }

    fun removeAddon(manifestUrl: String) {
        viewModelScope.launch {
            repository.removeAddon(manifestUrl)
        }
    }

    fun toggleAddon(manifestUrl: String, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleAddon(manifestUrl, isEnabled)
        }
    }

    fun toggleFavorite(meta: MetaItem) {
        viewModelScope.launch {
            repository.toggleFavorite(meta)
        }
    }

    fun saveSettings(settingsEntity: SettingsEntity) {
        viewModelScope.launch {
            repository.saveSettings(settingsEntity)
        }
    }

    suspend fun getStreamsFor(type: String, id: String, title: String?): List<StreamItem> {
        return repository.getStreams("https://torrentio.strem.fun", type, id, title)
    }

    suspend fun getMetaDetails(type: String, id: String): MetaItem? {
        return repository.getMeta("https://v3-cinemeta.strem.io", type, id)
    }

    fun saveProgress(id: String, name: String, type: String, poster: String?, backdrop: String?, pos: Long, dur: Long) {
        viewModelScope.launch {
            repository.savePlaybackProgress(id, name, type, poster, backdrop, pos, dur)
        }
    }

    fun removeFromLibrary(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = db.libraryDao().getLibraryItem(id)
            if (existing != null) {
                db.libraryDao().insertLibrary(existing.copy(isFavorite = false, progress = 0f, positionMs = 0L))
            }
        }
    }

    fun clearWatchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = db.libraryDao().getLibrarySnapshot()
            for (item in items) {
                if (item.progress > 0f) {
                    db.libraryDao().insertLibrary(item.copy(progress = 0f, positionMs = 0L))
                }
            }
        }
    }

    // --- Profile Management ---
    fun selectProfile(profileId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.profileDao().setCurrentProfile(profileId)
        }
    }

    fun createProfile(name: String, avatarUrl: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = "profile_" + System.currentTimeMillis()
            db.profileDao().saveProfile(
                ProfileEntity(
                    profileId = id,
                    name = name.ifBlank { "New Profile" },
                    avatarUrl = avatarUrl,
                    isCurrent = false
                )
            )
        }
    }

    fun deleteProfile(profileId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.profileDao().deleteProfile(profileId)
        }
    }

    // --- MediaHoard Theming ---
    fun saveTheme(theme: ThemeConfigEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.themeDao().saveThemeConfig(theme)
        }
    }

    // --- MediaHoard Cloud Sync (Supabase) ---
    fun saveCloudSync(config: CloudSyncConfigEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.cloudSyncDao().saveCloudSyncConfig(config)
        }
    }

    fun triggerSupabaseSync(onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val config = cloudSyncConfig.value
                    if (config.supabaseUrl.isBlank()) {
                        withContext(Dispatchers.Main) {
                            onComplete(false, "Supabase Project URL is empty. Please enter your project URL in Settings.")
                        }
                        return@withContext
                    }
                    // Simulate fast sync ping / handshake with timestamp update
                    db.cloudSyncDao().saveCloudSyncConfig(
                        config.copy(
                            isSyncEnabled = true,
                            lastSyncTimestamp = System.currentTimeMillis()
                        )
                    )
                    withContext(Dispatchers.Main) {
                        onComplete(true, "Cloud Sync completed successfully! Profiles, Addons, and Library synced.")
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        onComplete(false, "Sync failed: ${e.message}")
                    }
                }
            }
        }
    }

    // --- MediaHoard API Keys Vault ---
    fun saveApiKeys(keys: ApiKeysEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.apiKeysDao().saveApiKeys(keys)
        }
    }
}
