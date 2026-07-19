package com.trio.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trio.R
import com.trio.data.local.db.UserProfileEntity

@Composable
fun ProfileManagementScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val profiles by viewModel.profiles.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.user_profiles),
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(onClick = { showAddDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_profile)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(profiles) { profile ->
                ProfileCard(
                    profile = profile,
                    isCurrent = profile.id == currentUser?.id,
                    onSwitch = { viewModel.switchProfile(profile.id) },
                    onTextScaleChange = { viewModel.updateTextScale(profile.id, it) },
                    onHapticIntensityChange = { viewModel.updateHapticIntensity(profile.id, it) },
                    onDelete = { viewModel.deleteProfile(profile) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddProfileDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name ->
                viewModel.addProfile(name)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun ProfileCard(
    profile: UserProfileEntity,
    isCurrent: Boolean,
    onSwitch: () -> Unit,
    onTextScaleChange: (Float) -> Unit,
    onHapticIntensityChange: (Float) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSwitch)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                    Column {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (isCurrent) {
                            Text(
                                text = stringResource(R.string.active),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (!isCurrent) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.delete_profile)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = stringResource(R.string.text_scale, profile.textScale))
            Slider(
                value = profile.textScale,
                onValueChange = onTextScaleChange,
                valueRange = 0.5f..2.0f,
                steps = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = stringResource(R.string.haptic_intensity, profile.hapticIntensity * 100))
            Slider(
                value = profile.hapticIntensity,
                onValueChange = onHapticIntensityChange,
                valueRange = 0.0f..1.0f,
                steps = 9
            )
        }
    }
}

@Composable
private fun AddProfileDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_profile_title)) },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.profile_name)) },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
