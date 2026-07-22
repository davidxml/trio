package com.trio.presentation.launcher.components.standard

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.trio.presentation.launcher.components.shared.AppGridTile
import com.trio.presentation.launcher.components.shared.LaunchableApp
import com.trio.presentation.launcher.components.shared.loadIcon
import com.trio.presentation.launcher.components.shared.queryLaunchableApps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun StandardAppGrid(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<LaunchableApp>>(emptyList()) }

    LaunchedEffect(Unit) {
        apps = withContext(Dispatchers.IO) {
            queryLaunchableApps(context)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(apps) { app ->
            val icon = remember(app.packageName) { app.loadIcon(context) }
            AppGridTile(
                label = app.label,
                onClick = {
                    app.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(app.intent)
                },
                icon = icon
            )
        }
    }
}
