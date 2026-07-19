package com.trio.presentation.launcher.components.hearing

import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.LaunchableApp
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab
import com.trio.presentation.launcher.components.shared.queryLaunchableApps
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.hearing.HearingAlertStateHolder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HearingModeEntryPoint {
    fun hapticController(): HapticFeedbackController
    fun alertStateHolder(): HearingAlertStateHolder
}

@Composable
fun HearingImpairedHomeScreen(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication<HearingModeEntryPoint>(
            context.applicationContext
        )
    }
    val hapticController = entryPoint.hapticController()
    val alertStateHolder = entryPoint.alertStateHolder()

    var apps by remember { mutableStateOf<List<LaunchableApp>>(emptyList()) }

    LaunchedEffect(Unit) {
        apps = withContext(Dispatchers.IO) {
            queryLaunchableApps(context)
        }
        hapticController.playNotificationPulse()
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                VisualAlertBanner(alertStateHolder = alertStateHolder)
            }

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
                HearingTouchZone(
                    label = "Switch to Standard Mode",
                    onClick = { onModeSelected(DeviceMode.STANDARD) },
                    hapticController = hapticController,
                    description = "Switch to Standard Mode",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                HearingTouchZone(
                    label = "Settings",
                    onClick = {
                        val intent = Intent("com.trio.action.OPEN_SETTINGS").apply {
                            `package` = context.packageName
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    },
                    hapticController = hapticController,
                    description = "Settings",
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

            items(apps.chunked(2)) { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    row.forEach { app ->
                        HearingTouchZone(
                            label = app.label,
                            onClick = {
                                app.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                context.startActivity(app.intent)
                            },
                            hapticController = hapticController,
                            description = app.label,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            item {
                LiveCaptionOverlay(alertStateHolder = alertStateHolder)
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
