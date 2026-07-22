package com.trio.presentation.launcher.components.speech

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.core.theme.SpeechFab
import com.trio.core.theme.SpeechPrimaryAction
import com.trio.domain.model.DeviceMode
import com.trio.presentation.launcher.components.shared.LaunchableApp
import com.trio.presentation.launcher.components.shared.ModeSwitcherFab
import com.trio.presentation.launcher.components.shared.loadIcon
import com.trio.presentation.launcher.components.shared.queryLaunchableApps
import com.trio.presentation.launcher.components.vision.rememberTtsAnnouncer
import com.trio.service.haptics.HapticFeedbackController
import com.trio.service.tts.TtsQueueManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.ceil
import kotlin.math.max

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SpeechModeEntryPoint {
    fun hapticController(): HapticFeedbackController
    fun ttsManager(): TtsQueueManager
}

@Composable
fun SpeechImpairedHomeScreen(
    currentMode: DeviceMode,
    onModeSelected: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val entryPoint = remember {
        EntryPointAccessors.fromApplication<SpeechModeEntryPoint>(
            context.applicationContext
        )
    }
    val hapticController = entryPoint.hapticController()
    val ttsManager = entryPoint.ttsManager()
    val ttsAnnouncer = rememberTtsAnnouncer(ttsManager)
    val presetPhrases = context.resources.getStringArray(R.array.speech_preset_phrases).toList()

    var apps by remember { mutableStateOf<List<LaunchableApp>>(emptyList()) }
    var bufferText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        apps = withContext(Dispatchers.IO) {
            queryLaunchableApps(context)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { bufferText = "" },
                        modifier = Modifier
                    ) {
                        Text(text = stringResource(R.string.speech_clear))
                    }

                    Button(
                        onClick = {
                            if (bufferText.isNotBlank()) {
                                hapticController.playConfirmation()
                                ttsAnnouncer.announce(bufferText)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SpeechPrimaryAction
                        )
                    ) {
                        Text(text = stringResource(R.string.speech_speak))
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = bufferText,
                    onValueChange = { bufferText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = { Text(text = stringResource(R.string.speech_buffer_placeholder)) },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }

            item {
                LazyRow(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(presetPhrases) { phrase ->
                        AacChip(
                            label = phrase,
                            onClick = {
                                bufferText = if (bufferText.isBlank()) {
                                    phrase
                                } else {
                                    "$bufferText $phrase"
                                }
                                hapticController.playTick()
                            }
                        )
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.applications),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            item {
                BoxWithConstraints {
                    val minTileWidth = 120.dp
                    val columns = max(1, (this.maxWidth.value / minTileWidth.value).toInt())
                    val rows = ceil(apps.size.toFloat() / columns).toInt()
                    val tileHeight = 72.dp
                    val spacing = 8.dp
                    val gridHeight = if (apps.isEmpty()) 0.dp
                    else tileHeight * rows + spacing * max(0, rows - 1)

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = minTileWidth),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(gridHeight),
                        userScrollEnabled = false
                    ) {
                        items(apps) { app ->
                            val icon = remember(app.packageName) { app.loadIcon(context) }
                            SpeechTouchZone(
                                label = app.label,
                                onClick = {
                                    app.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    context.startActivity(app.intent)
                                },
                                hapticController = hapticController,
                                description = app.label,
                                icon = icon,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        ModeSwitcherFab(
            currentMode = currentMode,
            onModeSelected = onModeSelected,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = SpeechFab
        )
    }
}
