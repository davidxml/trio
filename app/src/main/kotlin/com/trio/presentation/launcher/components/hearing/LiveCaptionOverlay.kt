package com.trio.presentation.launcher.components.hearing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trio.R
import com.trio.core.theme.HearingCaptionBackground
import com.trio.core.theme.HearingCaptionHeader
import com.trio.core.theme.HearingCaptionBody
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

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(HearingCaptionBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.live_captions_label),
                style = MaterialTheme.typography.titleMedium,
                color = HearingCaptionHeader
            )
            if (captions.isNotEmpty()) {
                Text(
                    text = "${captions.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = HearingCaptionHeader.copy(alpha = 0.6f)
                )
            }
        }

        if (captions.isEmpty()) {
            Text(
                text = stringResource(R.string.caption_empty_state),
                style = MaterialTheme.typography.bodyMedium,
                color = HearingCaptionBody.copy(alpha = 0.4f),
                modifier = Modifier.padding(vertical = 32.dp)
            )
        } else {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 320.dp)
                    .padding(top = 8.dp)
            ) {
                items(captions, key = { "${it.timestamp}-${it.textHash}" }) { entry ->
                    CaptionItem(entry)
                }
            }
        }
    }
}

@Composable
private fun CaptionItem(entry: CaptionEntry) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(initialAlpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(HearingCaptionBody.copy(alpha = 0.08f))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = entry.source,
                style = MaterialTheme.typography.labelSmall,
                color = HearingCaptionHeader,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = entry.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = HearingCaptionBody,
                lineHeight = 26.sp
            )
        }
    }
}
