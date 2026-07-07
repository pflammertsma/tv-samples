package com.example.android.tvleanback.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.components.SectionHeader
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun VideoDetailsScreen(
    video: Video,
    onWatchTrailerClick: (Video) -> Unit,
    onRelatedVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var allVideos by remember { mutableStateOf<List<Video>?>(null) }

    LaunchedEffect(Unit) {
        allVideos = VideoRepository.getVideos(context)
    }

    val relatedVideos = remember(allVideos, video) {
        allVideos?.filter { it.category == video.category && it.id != video.id } ?: emptyList()
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Background Image with Gradient Scrim
            AsyncImage(
                model = video.bgImageUrl ?: video.cardImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp, vertical = 36.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Details Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Poster Image
                    AsyncImage(
                        model = video.cardImageUrl,
                        contentDescription = video.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(220.dp)
                            .height(330.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                    Spacer(modifier = Modifier.width(40.dp))

                    // Info & Actions
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = video.studio ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = video.title ?: "",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = video.description ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 4
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        // Action Buttons Row
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(onClick = { onWatchTrailerClick(video) }) {
                                Text(text = "WATCH TRAILER")
                            }
                            Button(onClick = {
                                Toast.makeText(context, "Rent $3.99", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "RENT $3.99")
                            }
                            Button(onClick = {
                                Toast.makeText(context, "Buy $9.99", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "BUY $9.99")
                            }
                        }
                    }
                }

                // Related Videos Row
                if (relatedVideos.isNotEmpty()) {
                    Column {
                        SectionHeader(title = "Related Videos")
                        LazyRow(
                            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(relatedVideos) { relatedVideo ->
                                MovieCard(
                                    video = relatedVideo,
                                    onClick = onRelatedVideoClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
