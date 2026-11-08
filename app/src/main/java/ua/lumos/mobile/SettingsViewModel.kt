package ua.lumos.mobile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.lumos.mobile.data.SettingsRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    val sortAlphabetically: StateFlow<Boolean> = repository.sortAlphabetically.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsRepository.DefaultSortAlphabetically
    )

    fun onSortAlphabeticallyChange(enabled: Boolean) {
        viewModelScope.launch {
            repository.setSortAlphabetically(enabled)
        }
    }
}
