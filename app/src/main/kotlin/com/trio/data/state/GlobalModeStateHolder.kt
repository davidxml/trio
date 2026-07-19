package com.trio.data.state

import com.trio.core.di.AppScope
import com.trio.data.local.datastore.ModePreferencesDataStore
import com.trio.domain.model.DeviceMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalModeStateHolder @Inject constructor(
    private val dataStore: ModePreferencesDataStore,
    @AppScope private val scope: CoroutineScope
) {
    private val _mode = MutableStateFlow(DeviceMode.STANDARD)

    val mode: StateFlow<DeviceMode> = _mode.asStateFlow()

    init {
        scope.launch {
            dataStore.modeFlow.collect { _mode.value = it }
        }
    }

    suspend fun setMode(mode: DeviceMode) {
        dataStore.setMode(mode)
    }
}
