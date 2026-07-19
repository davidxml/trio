package com.trio.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.trio.data.state.UserProfileStateHolder
import com.trio.domain.model.DeviceMode

@Composable
fun TrioTheme(
    mode: DeviceMode = DeviceMode.STANDARD,
    userProfileState: UserProfileStateHolder? = null,
    content: @Composable () -> Unit
) {
    val textScale = if (userProfileState != null) {
        val scale by userProfileState.textScale.collectAsState()
        scale
    } else {
        1.0f
    }

    ModeThemeProvider(mode = mode, textScale = textScale, content = content)
}
