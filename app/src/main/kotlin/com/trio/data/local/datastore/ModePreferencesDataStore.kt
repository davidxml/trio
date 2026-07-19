package com.trio.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.trio.core.util.Constants
import com.trio.domain.model.DeviceMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.modeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = Constants.DATASTORE_NAME
)

@Singleton
class ModePreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.modeDataStore

    private val modeKey = stringPreferencesKey(Constants.KEY_DEVICE_MODE)

    val modeFlow: Flow<DeviceMode> = dataStore.data.map { preferences ->
        val name = preferences[modeKey]
        if (name != null) {
            runCatching { DeviceMode.valueOf(name) }.getOrDefault(DeviceMode.STANDARD)
        } else {
            DeviceMode.STANDARD
        }
    }

    suspend fun setMode(mode: DeviceMode) {
        dataStore.edit { preferences ->
            preferences[modeKey] = mode.name
        }
    }
}
