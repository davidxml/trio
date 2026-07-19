package com.trio.presentation.launcher

import com.trio.domain.model.DeviceMode

data class LauncherUiState(
    val currentMode: DeviceMode = DeviceMode.STANDARD
)
