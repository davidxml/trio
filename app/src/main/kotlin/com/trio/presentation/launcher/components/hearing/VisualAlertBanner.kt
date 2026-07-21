package com.trio.presentation.launcher.components.hearing

import android.app.NotificationManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.core.theme.HearingAlertCritical
import com.trio.core.theme.HearingAlertWarning
import com.trio.service.hearing.AlertEvent
import com.trio.service.hearing.HearingAlertStateHolder
import kotlinx.coroutines.delay

@Composable
fun VisualAlertBanner(
    alertStateHolder: HearingAlertStateHolder,
    modifier: Modifier = Modifier,
    onDismiss: (Int) -> Unit = { alertStateHolder.dismissAlert(it) }
) {
    val alerts by alertStateHolder.pendingAlerts.collectAsState()
    var visible by remember { mutableStateOf(false) }

    val latestAlert = alerts.firstOrNull()

    LaunchedEffect(latestAlert?.title) {
        if (latestAlert != null) {
            visible = true
        }
    }

    AnimatedVisibility(
        visible = visible && latestAlert != null,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = fadeOut()
    ) {
        latestAlert?.let { alert ->
            val isCritical = alert.importance >= NotificationManager.IMPORTANCE_HIGH
            val borderColor = if (isCritical) HearingAlertCritical else HearingAlertWarning
            val dismissLabel = stringResource(R.string.dismiss_alert)

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 3.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(borderColor.copy(alpha = 0.12f))
                    .clickable {
                        visible = false
                        onDismiss(0)
                    }
                    .semantics {
                        contentDescription = dismissLabel
                    }
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = borderColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = alert.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = borderColor,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = alert.body,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }

    if (alerts.isEmpty()) {
        visible = false
    }
}
