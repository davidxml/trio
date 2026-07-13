package com.trio.data.state

import com.trio.domain.model.DeviceMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalModeStateHolder @Inject constructor() {

    private val _mode = MutableStateFlow(DeviceMode.Standard)

    val mode: StateFlow<DeviceMode> = _mode.asStateFlow()

    fun setMode(m: DeviceMode) {
        _mode.value = m
    }
}
