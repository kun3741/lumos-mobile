package ua.lumos.mobile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.lumos.mobile.data.FavoritesRepository
import ua.lumos.mobile.data.RegionRepository

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val favoritesRepository = FavoritesRepository(application)
    private val regionRepository = RegionRepository()
    private var loadJob: Job? = null
    private var _uiState by mutableStateOf(FavoriteRegionsUiState())

    val uiState: FavoriteRegionsUiState
        get() = _uiState

    init {
        viewModelScope.launch {
            favoritesRepository.favoriteIds.collect { favoriteIds ->
                val loadedIds = _uiState.regions.mapTo(mutableSetOf()) { it.id }
                when {
                    favoriteIds.isEmpty() -> {
                        loadJob?.cancel()
                        _uiState = FavoriteRegionsUiState(isLoading = false)
                    }
                    favoriteIds.any { it !in loadedIds } -> loadFavorites(favoriteIds)
                    else -> {
                        _uiState = _uiState.copy(
                            favoriteIds = favoriteIds,
                            regions = _uiState.regions.filter { it.id in favoriteIds },
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadFavorites(_uiState.favoriteIds)
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(id)
        }
    }

    private fun loadFavorites(favoriteIds: Set<String>) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState = _uiState.copy(
                favoriteIds = favoriteIds,
                isLoading = true,
                error = null
            )
            try {
                val freshRegions = regionRepository.getRegions()
                    .filter { region -> region.id in favoriteIds }
                _uiState = _uiState.copy(
                    favoriteIds = favoriteIds,
                    regions = freshRegions,
                    isLoading = false,
                    error = null
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState = _uiState.copy(
                    regions = emptyList(),
                    isLoading = false,
                    error = exception.toRegionLoadError()
                )
            }
        }
    }
}
