package com.trio.presentation.launcher.components.standard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private data class AppTile(val name: String, val color: Color)

private val dummyApps = listOf(
    AppTile("Phone", Color(0xFF4CAF50)),
    AppTile("Messages", Color(0xFF2196F3)),
    AppTile("Camera", Color(0xFF9C27B0)),
    AppTile("Photos", Color(0xFFFF9800)),
    AppTile("Chrome", Color(0xFFF44336)),
    AppTile("Maps", Color(0xFF009688)),
    AppTile("YouTube", Color(0xFFE91E63)),
    AppTile("Gmail", Color(0xFF3F51B5)),
    AppTile("Calendar", Color(0xFF00BCD4)),
    AppTile("Clock", Color(0xFF795548)),
    AppTile("Settings", Color(0xFF607D8B)),
    AppTile("Files", Color(0xFFCDDC39))
)

@Composable
fun StandardAppGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(dummyApps) { app ->
            AppTileItem(app)
        }
    }
}

@Composable
private fun AppTileItem(app: AppTile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(app.color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = app.name.first().toString(),
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = app.name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}
