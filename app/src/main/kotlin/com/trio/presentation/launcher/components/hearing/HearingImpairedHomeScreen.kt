package com.trio.presentation.launcher.components.hearing

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.LaunchableApp
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab
import com.trio.presentation.launcher.components.shared.loadIcon
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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var apps by remember { mutableStateOf<List<LaunchableApp>>(emptyList()) }

    LaunchedEffect(Unit) {
        apps = withContext(Dispatchers.IO) {
            queryLaunchableApps(context)
        }
        hapticController.playNotificationPulse()
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLandscape) {
            LandscapeHearingLayout(
                currentMode = currentMode,
                onModeSelected = onModeSelected,
                apps = apps,
                alertStateHolder = alertStateHolder,
                hapticController = hapticController
            )
        } else {
            PortraitHearingLayout(
                currentMode = currentMode,
                onModeSelected = onModeSelected,
                apps = apps,
                alertStateHolder = alertStateHolder,
                hapticController = hapticController
            )
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

@Composable
private fun PortraitHearingLayout(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    apps: List<LaunchableApp>,
    alertStateHolder: HearingAlertStateHolder,
    hapticController: HapticFeedbackController
) {
    val context = LocalContext.current

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
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            Text(
                text = stringResource(R.string.hearing_mode_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            SoundLevelIndicator(alertStateHolder = alertStateHolder)
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            HearingTouchZone(
                label = stringResource(R.string.switch_to_standard),
                onClick = { onModeSelected(DeviceMode.STANDARD) },
                hapticController = hapticController,
                description = stringResource(R.string.switch_to_standard),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            HearingTouchZone(
                label = stringResource(R.string.settings),
                onClick = {
                    val intent = Intent("com.trio.action.OPEN_SETTINGS").apply {
                        `package` = context.packageName
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                hapticController = hapticController,
                description = stringResource(R.string.settings),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.applications),
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
                    val icon = remember(app.packageName) { app.loadIcon(context) }
                    HearingTouchZone(
                        label = app.label,
                        onClick = {
                            app.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(app.intent)
                        },
                        hapticController = hapticController,
                        description = app.label,
                        modifier = Modifier.weight(1f),
                        icon = icon
                    )
                }
                if (row.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            LiveCaptionOverlay(alertStateHolder = alertStateHolder)
        }
    }
}

@Composable
private fun LandscapeHearingLayout(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    apps: List<LaunchableApp>,
    alertStateHolder: HearingAlertStateHolder,
    hapticController: HapticFeedbackController
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VisualAlertBanner(alertStateHolder = alertStateHolder)

            Text(
                text = stringResource(R.string.hearing_mode_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            SoundLevelIndicator(alertStateHolder = alertStateHolder)

            HearingTouchZone(
                label = stringResource(R.string.switch_to_standard),
                onClick = { onModeSelected(DeviceMode.STANDARD) },
                hapticController = hapticController,
                description = stringResource(R.string.switch_to_standard),
                modifier = Modifier.fillMaxWidth()
            )

            HearingTouchZone(
                label = stringResource(R.string.settings),
                onClick = {
                    val intent = Intent("com.trio.action.OPEN_SETTINGS").apply {
                        `package` = context.packageName
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                hapticController = hapticController,
                description = stringResource(R.string.settings),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .weight(1.2f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(R.string.applications),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(apps.chunked(2)) { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { app ->
                            val icon = remember(app.packageName) { app.loadIcon(context) }
                            HearingTouchZone(
                                label = app.label,
                                onClick = {
                                    app.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(app.intent)
                                },
                                hapticController = hapticController,
                                description = app.label,
                                modifier = Modifier.weight(1f),
                                icon = icon
                            )
                        }
                        if (row.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            LiveCaptionOverlay(alertStateHolder = alertStateHolder)
        }
    }
}
