package com.trio.presentation.onboarding

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.service.accessibility.TrioAccessibilityService
import com.trio.service.notification.TrioNotificationListenerService

@Composable
fun AccessibilityPermissionScreen(
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var step by remember { mutableIntStateOf(0) }

    val accessibilityEnabled = remember { isAccessibilityServiceEnabled(context) }
    val notificationListenerEnabled = remember { isNotificationListenerEnabled(context) }

    AnimatedContent(targetState = step, label = "onboarding_step") { currentStep ->
        when (currentStep) {
            0 -> PermissionStep(
                title = stringResource(R.string.onboarding_welcome),
                description = stringResource(R.string.onboarding_accessibility_desc),
                icon = Icons.Filled.Accessibility,
                isGranted = accessibilityEnabled,
                grantLabel = stringResource(R.string.open_accessibility_settings),
                onGrant = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                onNext = { step = 1 }
            )

            1 -> PermissionStep(
                title = stringResource(R.string.onboarding_notification_title),
                description = stringResource(R.string.onboarding_notification_desc),
                icon = Icons.Filled.Notifications,
                isGranted = notificationListenerEnabled,
                grantLabel = stringResource(R.string.open_notification_settings),
                onGrant = {
                    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                },
                onNext = { step = 2 }
            )

            2 -> CompletionStep(onComplete = onCompleted)
        }
    }
}

@Composable
private fun PermissionStep(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    grantLabel: String,
    onGrant: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isGranted) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.permission_granted),
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.access_granted),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = stringResource(R.string.permission_not_granted),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = stringResource(R.string.access_required),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!isGranted) {
            Button(
                onClick = onGrant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = grantLabel)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedButton(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isGranted) stringResource(R.string.next) else stringResource(R.string.skip_for_now))
        }
    }
}

@Composable
private fun CompletionStep(onComplete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = Color(0xFF4CAF50)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.onboarding_complete_title),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.onboarding_complete_desc),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.start_using_trio))
        }
    }
}

private fun isAccessibilityServiceEnabled(context: Context): Boolean {
    val serviceComponent = ComponentName(context, TrioAccessibilityService::class.java)
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabledServices.contains(serviceComponent.flattenToString())
}

private fun isNotificationListenerEnabled(context: Context): Boolean {
    val flat = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_NOTIFICATION_LISTENERS
    ) ?: return false
    val componentName = ComponentName(context, TrioNotificationListenerService::class.java)
    return flat.contains(componentName.flattenToString())
}
