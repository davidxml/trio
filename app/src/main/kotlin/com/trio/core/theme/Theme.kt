package com.trio.core.theme

import androidx.compose.runtime.Composable
import com.trio.domain.model.DeviceMode

@Composable
fun TrioTheme(
    mode: DeviceMode = DeviceMode.STANDARD,
    content: @Composable () -> Unit
) {
    ModeThemeProvider(mode = mode, content = content)
}
