package ua.lumos.mobile.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class FavoritesRepository(context: Context) {
    private val dataStore = context.applicationContext.lumosPreferencesDataStore

    val favoriteIds: Flow<Set<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[FavoriteIdsKey]?.toSet().orEmpty()
        }

    suspend fun toggleFavorite(id: String) {
        dataStore.edit { preferences ->
            val currentIds = preferences[FavoriteIdsKey]?.toMutableSet() ?: mutableSetOf()
            if (!currentIds.add(id)) currentIds.remove(id)
            preferences[FavoriteIdsKey] = currentIds
        }
    }

    companion object {
        private val FavoriteIdsKey = stringSetPreferencesKey("favorite_region_ids")
    }
}
