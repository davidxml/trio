package com.trio.presentation.launcher.components.standard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.core.theme.StandardFab
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.AppHeader
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab

@Composable
fun StandardHomeScreen(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            AppHeader(
                title = stringResource(R.string.app_name),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            StandardAppGrid()
        }

        ModeSwitcherFab(
            currentMode = currentMode,
            onModeSelected = onModeSelected,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = StandardFab
        )
    }
}
