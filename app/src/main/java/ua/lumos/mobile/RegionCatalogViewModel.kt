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
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import ua.lumos.mobile.data.RegionRepository
import ua.lumos.mobile.data.SettingsRepository
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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

    fun retry() {
        loadRegions()
    }

    private fun loadRegions() {
        loadRegionsJob?.cancel()
        loadRegionsJob = viewModelScope.launch {
            _uiState = _uiState.copy(
                regions = emptyList(),
                isLoading = true,
                error = null
            )
            try {
                val regions = regionRepository.getRegions()
                _uiState = _uiState.copy(
                    regions = regions,
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

private fun Throwable.toRegionLoadError(): RegionLoadError = when (this) {
    is HttpException -> RegionLoadError.SERVER
    is SerializationException -> RegionLoadError.INVALID_RESPONSE
    is UnknownHostException,
    is ConnectException,
    is NoRouteToHostException,
    is SocketTimeoutException,
    is IOException -> RegionLoadError.NO_NETWORK
    else -> RegionLoadError.INVALID_RESPONSE
}
