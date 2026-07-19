package com.trio.data.state

import com.trio.data.local.datastore.ModePreferencesDataStore
import com.trio.domain.model.DeviceMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalModeStateHolder @Inject constructor(
    private val dataStore: ModePreferencesDataStore
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _mode = MutableStateFlow(
        runBlocking(Dispatchers.IO) {
            dataStore.modeFlow.first()
        }
    )

    val mode: StateFlow<DeviceMode> = _mode.asStateFlow()

    init {
        dataStore.modeFlow
            .onEach { _mode.value = it }
            .launchIn(scope)
    }
}
