package com.trio.presentation.launcher.components.hearing

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trio.service.hearing.AlertEvent
import com.trio.service.hearing.HearingAlertStateHolder

@Composable
fun VisualAlertBanner(
    alertStateHolder: HearingAlertStateHolder,
    modifier: Modifier = Modifier,
    onDismiss: (Int) -> Unit = {}
) {
    val alerts by alertStateHolder.pendingAlerts.collectAsState()

    if (alerts.isEmpty()) return

    val latestAlert = alerts.first()

    val infiniteTransition = rememberInfiniteTransition(label = "alert_flash")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 4.dp,
                color = Color.Red.copy(alpha = borderAlpha),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onDismiss(0) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${latestAlert.title}: ${latestAlert.body}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(borderAlpha)
        )
    }
}
