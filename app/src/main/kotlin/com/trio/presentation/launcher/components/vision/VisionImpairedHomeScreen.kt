package com.trio.presentation.launcher.components.vision

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.tts.TtsQueueManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface VisionModeEntryPoint {
    fun hapticController(): HapticFeedbackController
    fun ttsManager(): TtsQueueManager
}

private data class VisionAppTile(
    val label: String,
    val ttsDescription: String
)

private val visionApps = listOf(
    VisionAppTile("Phone", "Phone. Double tap to open."),
    VisionAppTile("Messages", "Messages. Double tap to open."),
    VisionAppTile("Camera", "Camera. Double tap to open."),
    VisionAppTile("Photos", "Photos. Double tap to open."),
    VisionAppTile("Chrome", "Chrome browser. Double tap to open."),
    VisionAppTile("Maps", "Maps. Double tap to open."),
    VisionAppTile("YouTube", "YouTube. Double tap to open."),
    VisionAppTile("Gmail", "Gmail. Double tap to open."),
    VisionAppTile("Calendar", "Calendar. Double tap to open."),
    VisionAppTile("Clock", "Clock. Double tap to open."),
    VisionAppTile("Files", "Files. Double tap to open."),
    VisionAppTile("Settings", "Settings. Double tap to open.")
)

@Composable
fun VisionImpairedHomeScreen(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication<VisionModeEntryPoint>(
            context.applicationContext
        )
    }
    val hapticController = entryPoint.hapticController()
    val ttsManager = entryPoint.ttsManager()
    val ttsAnnouncer = rememberTtsAnnouncer(ttsManager)

    LaunchedEffect(Unit) {
        ttsAnnouncer.announce("Vision Impaired Mode activated. Swipe to explore items.")
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Trio",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                HighContrastTouchZone(
                    label = "Switch to Standard Mode",
                    onClick = { onModeSelected(DeviceMode.STANDARD) },
                    hapticController = hapticController,
                    ttsAnnouncer = ttsAnnouncer,
                    description = "Switch to Standard Mode. Double tap to activate.",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                HighContrastTouchZone(
                    label = "Settings",
                    onClick = { /* TODO: navigate to settings */ },
                    hapticController = hapticController,
                    ttsAnnouncer = ttsAnnouncer,
                    description = "Settings. Double tap to open.",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Applications",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(visionApps.chunked(2)) { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { app ->
                        HighContrastTouchZone(
                            label = app.label,
                            onClick = { /* TODO: launch app */ },
                            hapticController = hapticController,
                            ttsAnnouncer = ttsAnnouncer,
                            description = app.ttsDescription,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        ModeSwitcherFab(
            currentMode = currentMode,
            onModeSelected = onModeSelected,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        )
    }
}
