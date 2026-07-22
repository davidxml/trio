package com.trio.presentation.launcher.components.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.trio.R
import com.trio.domain.model.DeviceMode

/**
 * Owned by Nexus (Feature 1). Shared UI contract — Dev B's screens (hearing/, vision/) render
 * this Fab, but they don't own it. Don't change the public signature below without flagging
 * Dev B first; the Dialog it opens is a separate file (ModeSwitcherDialog.kt).
 */
@Composable
fun ModeSwitcherFab(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified
) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showDialog = true },
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = stringResource(R.string.switch_mode))
    }

    if (showDialog) {
        ModeSwitcherDialog(
            currentMode = currentMode,
            onModeSelected = {
                onModeSelected(it)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}
