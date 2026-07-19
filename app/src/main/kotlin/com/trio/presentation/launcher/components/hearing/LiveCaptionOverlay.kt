package com.trio.presentation.launcher.components.hearing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trio.service.hearing.CaptionEntry
import com.trio.service.hearing.HearingAlertStateHolder

@Composable
fun LiveCaptionOverlay(
    alertStateHolder: HearingAlertStateHolder,
    modifier: Modifier = Modifier
) {
    val captions by alertStateHolder.recentCaptions.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(captions.size) {
        if (captions.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.85f))
            .padding(16.dp)
    ) {
        if (captions.isEmpty()) {
            Text(
                text = "Waiting for audio signals...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items(captions) { entry ->
                    CaptionItem(entry)
                }
            }
        }
    }
}

@Composable
private fun CaptionItem(entry: CaptionEntry) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.1f), shape = MaterialTheme.shapes.small)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = entry.source,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF80CBC4),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = entry.text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            lineHeight = 28.sp
        )
    }
}
