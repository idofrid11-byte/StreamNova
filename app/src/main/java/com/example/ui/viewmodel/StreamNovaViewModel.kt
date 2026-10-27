package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.*
import com.example.data.model.MetaItem
import com.example.data.model.StreamItem
import com.example.data.repository.AddonRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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

    val favorites: StateFlow<List<LibraryEntity>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val continueWatching: StateFlow<List<LibraryEntity>> = repository.continueWatching
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<SettingsEntity> = repository.settings
        .map { it ?: SettingsEntity() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsEntity())

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
                // repository already returns fallback on exception
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
}
