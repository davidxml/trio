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
import com.trio.domain.model.DeviceMode

/**
 * Owned by Nexus (Feature 1). Shared UI contract — Dev B's screens (hearing/, vision/) render
 * this Fab, but they don't own it. Don't change the public signature below without flagging
 * Dev B first; the Dialog it opens is a separate file (ModeSwitcherDialog.kt).
 */
@Composable
fun ModeSwitcherFab(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(onClick = { showDialog = true }) {
        Icon(imageVector = Icons.Filled.SwapHoriz, contentDescription = "Switch mode")
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