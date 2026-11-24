package ua.lumos.mobile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import ua.lumos.mobile.data.RegionRepository
import ua.lumos.mobile.data.SettingsRepository
import ua.lumos.mobile.data.FavoritesRepository
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RegionCatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository = SettingsRepository(application)
    private val favoritesRepository = FavoritesRepository(application)
    private val regionRepository = RegionRepository()
    private var loadRegionsJob: Job? = null
    private var searchJob: Job? = null
    private var _uiState by mutableStateOf(RegionCatalogUiState())

    val uiState: RegionCatalogUiState
        get() = _uiState

    init {
        viewModelScope.launch {
            settingsRepository.sortAlphabetically.collect { sortAlphabetically ->
                _uiState = _uiState.copy(sortAlphabetically = sortAlphabetically)
            }
        }
        viewModelScope.launch {
            favoritesRepository.favoriteIds.collect { favoriteIds ->
                _uiState = _uiState.copy(favoriteIds = favoriteIds)
            }
        }
        loadRegions()
    }

    fun onFilterSelected(filter: RegionFilter) {
        _uiState = _uiState.copy(selectedFilter = filter)
        loadRegions()
    }

    fun onSearchQueryChange(query: String) {
        searchJob?.cancel()
        _uiState = _uiState.copy(searchQuery = query)

        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) {
            _uiState = _uiState.copy(appliedSearchQuery = "")
            return
        }

        // The catalog API has no search endpoint, so debounce local filtering
        // without issuing a network request for each typed character.
        searchJob = viewModelScope.launch {
            delay(SearchDebounceMillis)
            _uiState = _uiState.copy(appliedSearchQuery = normalizedQuery)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _uiState = _uiState.copy(
            searchQuery = "",
            appliedSearchQuery = ""
        )
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(id)
        }
    }

    fun retry() {
        loadRegions()
    }

    fun refresh() {
        loadRegions(isRefresh = true)
    }

    private fun loadRegions(isRefresh: Boolean = false) {
        loadRegionsJob?.cancel()
        loadRegionsJob = viewModelScope.launch {
            _uiState = _uiState.copy(
                regions = if (isRefresh) _uiState.regions else emptyList(),
                isLoading = !isRefresh,
                isRefreshing = isRefresh,
                error = null
            )
            try {
                val regions = regionRepository.getRegions()
                _uiState = _uiState.copy(
                    regions = regions,
                    isLoading = false,
                    isRefreshing = false,
                    error = null
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState = _uiState.copy(
                    regions = if (isRefresh) _uiState.regions else emptyList(),
                    isLoading = false,
                    isRefreshing = false,
                    error = exception.toRegionLoadError()
                )
            }
        }
    }
}

private const val SearchDebounceMillis = 350L

internal fun Throwable.toRegionLoadError(): RegionLoadError = when (this) {
    is HttpException -> RegionLoadError.SERVER
    is SerializationException -> RegionLoadError.INVALID_RESPONSE
    is UnknownHostException,
    is ConnectException,
    is NoRouteToHostException,
    is SocketTimeoutException,
    is IOException -> RegionLoadError.NO_NETWORK
    else -> RegionLoadError.INVALID_RESPONSE
}
