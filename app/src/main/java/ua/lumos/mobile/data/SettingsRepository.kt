package ua.lumos.mobile.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsRepository(context: Context) {
    private val dataStore = context.applicationContext.lumosPreferencesDataStore

    val sortAlphabetically: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SortAlphabeticallyKey] ?: DefaultSortAlphabetically
        }

    suspend fun setSortAlphabetically(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SortAlphabeticallyKey] = enabled
        }
    }

    companion object {
        const val DefaultSortAlphabetically = false
        private val SortAlphabeticallyKey = booleanPreferencesKey("sort_alphabetically")
    }
}
