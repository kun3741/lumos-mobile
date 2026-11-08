package ua.lumos.mobile

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.lumos.mobile.data.SettingsRepository

class RegionCatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsRepository = SettingsRepository(application)
    private var _uiState by mutableStateOf(RegionCatalogUiState())

    val uiState: RegionCatalogUiState
        get() = _uiState

    init {
        viewModelScope.launch {
            settingsRepository.sortAlphabetically.collect { sortAlphabetically ->
                _uiState = _uiState.copy(sortAlphabetically = sortAlphabetically)
            }
        }
    }

    fun onFilterSelected(filter: RegionFilter) {
        _uiState = _uiState.copy(selectedFilter = filter)
    }
}
