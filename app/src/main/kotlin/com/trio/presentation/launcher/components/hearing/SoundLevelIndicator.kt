package com.trio.presentation.launcher.components.hearing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.core.theme.HearingSoundActive
import com.trio.core.theme.HearingSoundMuted
import com.trio.service.hearing.HearingAlertStateHolder

@Composable
fun SoundLevelIndicator(
    alertStateHolder: HearingAlertStateHolder,
    modifier: Modifier = Modifier
) {
    val soundLevel by alertStateHolder.soundLevel.collectAsState()
    val animatedLevel by animateFloatAsState(
        targetValue = soundLevel,
        animationSpec = tween(durationMillis = 120),
        label = "sound_level"
    )

    val isActive = soundLevel > 0.05f
    val barCount = 12

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.sound_visualizer_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (isActive) stringResource(R.string.sound_level_active)
                else stringResource(R.string.sound_level_quiet),
                style = MaterialTheme.typography.labelMedium,
                color = if (isActive) HearingSoundActive else HearingSoundMuted
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            repeat(barCount) { index ->
                val threshold = (index + 1).toFloat() / barCount
                val filled = animatedLevel >= threshold
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((12 + index * 3).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (filled) HearingSoundActive.copy(alpha = 0.5f + 0.5f * animatedLevel)
                            else HearingSoundMuted.copy(alpha = 0.15f)
                        )
                )
            }
        }
    }
}
