package com.trio.presentation.launcher.components.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.trio.R
import com.trio.domain.model.DeviceMode

/**
 * Owned by Nexus (Feature 1). Paired with ModeSwitcherFab.kt — this is the actual
 * mode-selection UI. Dev B calls into ModeSwitcherFab, not this file directly.
 */
@Composable
fun ModeSwitcherDialog(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.switch_mode)) },
        text = {
            Column {
                DeviceMode.entries.forEach { mode ->
                    TextButton(
                        onClick = { onModeSelected(mode) },
                        enabled = mode != currentMode
                    ) {
                        Text(mode.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}