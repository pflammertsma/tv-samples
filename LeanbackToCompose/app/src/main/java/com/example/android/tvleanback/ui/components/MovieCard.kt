package com.example.android.tvleanback.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.android.tvleanback.model.Video

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(
    video: Video,
    onClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 220.dp
) {
    CompactCard(
        onClick = { onClick(video) },
        image = {
            AsyncImage(
                model = video.cardImageUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
        },
        title = {
            Text(
                text = video.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 10.dp, start = 8.dp, end = 8.dp)
            )
        },
        subtitle = {
            if (!video.studio.isNullOrEmpty()) {
                Text(
                    text = video.studio,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 6.dp)
                )
            }
        },
        scale = CardDefaults.scale(focusedScale = 1.08f),
        border = CardDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = BorderStroke(2.dp, Color.White)
            )
        ),
        modifier = modifier.width(cardWidth)
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCardPlaceholder(
    modifier: Modifier = Modifier,
    cardWidth: Dp = 220.dp
) {
    CompactCard(
        onClick = { },
        image = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
            )
        },
        title = {
            Text(
                text = " ",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(top = 10.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.10f))
            )
        },
        subtitle = {
            Text(
                text = " ",
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 4.dp, start = 8.dp, end = 8.dp, bottom = 6.dp)
                    .fillMaxWidth(0.6f)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f))
            )
        },
        scale = CardDefaults.scale(focusedScale = 1.08f),
        border = CardDefaults.border(
            focusedBorder = androidx.tv.material3.Border(
                border = BorderStroke(2.dp, Color.White)
            )
        ),
        modifier = modifier.width(cardWidth)
    )
}
