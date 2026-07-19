package com.trio.presentation.launcher.components.hearing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab

@Composable
fun HearingImpairedHomeScreen(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Deaf Mode",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        ModeSwitcherFab(
            currentMode = currentMode,
            onModeSelected = onModeSelected,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
