package ua.lumos.mobile.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.lumosPreferencesDataStore by preferencesDataStore(name = "lumos_settings")
