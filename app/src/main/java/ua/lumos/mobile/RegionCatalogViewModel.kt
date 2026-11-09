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
import ua.lumos.mobile.data.RegionRepository
import ua.lumos.mobile.data.SettingsRepository

class RegionCatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository = SettingsRepository(application)
    private val regionRepository = RegionRepository()
    private var loadRegionsJob: Job? = null
    private var _uiState by mutableStateOf(RegionCatalogUiState())

    val uiState: RegionCatalogUiState
        get() = _uiState

    init {
        viewModelScope.launch {
            settingsRepository.sortAlphabetically.collect { sortAlphabetically ->
                _uiState = _uiState.copy(sortAlphabetically = sortAlphabetically)
            }
        }
        loadRegions()
    }

    fun onFilterSelected(filter: RegionFilter) {
        _uiState = _uiState.copy(selectedFilter = filter)
        loadRegions()
    }

    private fun loadRegions() {
        loadRegionsJob?.cancel()
        loadRegionsJob = viewModelScope.launch {
            _uiState = _uiState.copy(isLoading = true)
            try {
                val regions = regionRepository.getRegions()
                _uiState = _uiState.copy(
                    regions = regions,
                    isLoading = false
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState = _uiState.copy(isLoading = false)
            }
        }
    }
}
