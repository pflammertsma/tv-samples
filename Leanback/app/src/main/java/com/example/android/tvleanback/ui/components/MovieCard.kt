package com.example.android.tvleanback.ui.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.android.tvleanback.model.Video

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun MovieCard(
    video: Video,
    onClick: (Video) -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 180.dp
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
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        subtitle = {
            Text(
                text = video.studio ?: "",
                maxLines = 1
            )
        },
        scale = CardDefaults.scale(focusedScale = 1.1f),
        modifier = modifier.width(cardWidth)
    )
}
