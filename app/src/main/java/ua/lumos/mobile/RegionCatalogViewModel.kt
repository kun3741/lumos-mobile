package ua.lumos.mobile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegionCatalogViewModel : ViewModel() {
    private var _uiState by mutableStateOf(RegionCatalogUiState())

    val uiState: RegionCatalogUiState
        get() = _uiState

    fun onFilterSelected(filter: RegionFilter) {
        _uiState = _uiState.copy(selectedFilter = filter)
    }
}
