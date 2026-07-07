package com.example.android.tvleanback.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.tv.material3.Button
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CompactCard
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.android.tvleanback.R
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.LoadingIndicator
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.components.SectionHeader
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BrowseScreen(
    onVideoClick: (Video) -> Unit,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onVerticalGridClick: () -> Unit,
    onGuidedStepClick: () -> Unit,
    onErrorFragmentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var allVideos by remember { mutableStateOf<List<Video>?>(null) }
    var focusedVideo by remember { mutableStateOf<Video?>(null) }

    LaunchedEffect(Unit) {
        val videos = VideoRepository.getVideos(context)
        allVideos = videos
        if (videos.isNotEmpty()) {
            focusedVideo = videos.first()
        }
    }

    val categories = remember(allVideos) {
        allVideos?.groupBy { it.category ?: "Other" } ?: emptyMap()
    }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (allVideos == null) {
                LoadingIndicator()
            } else {
                // Hero Background Banner
                Box(modifier = Modifier.fillMaxWidth().height(350.dp)) {
                    AnimatedContent(
                        targetState = focusedVideo,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                        },
                        label = "hero_banner"
                    ) { video ->
                        if (video != null) {
                            AsyncImage(
                                model = video.bgImageUrl ?: video.cardImageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    // Gradient Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    )
                    // Hero Text
                    if (focusedVideo != null) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(horizontal = 32.dp, vertical = 16.dp)
                        ) {
                            Text(
                                text = focusedVideo?.title ?: "",
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = focusedVideo?.description ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }
                    }
                }

                // Main Content Scroll
                LazyColumn(
                    contentPadding = PaddingValues(top = 260.dp, bottom = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top Actions Row (Search & Settings)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Videos by Google",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(onClick = onSearchClick) {
                                    Text(text = "SEARCH")
                                }
                                Button(onClick = onSettingsClick) {
                                    Text(text = "SETTINGS")
                                }
                            }
                        }
                    }

                    // Video Categories
                    categories.forEach { (categoryName, videos) ->
                        item {
                            Column {
                                SectionHeader(title = categoryName, modifier = Modifier.padding(horizontal = 16.dp))
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 32.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(videos) { video ->
                                        MovieCard(
                                            video = video,
                                            onClick = onVideoClick,
                                            modifier = Modifier.onFocusChanged { focusState ->
                                                if (focusState.isFocused) {
                                                    focusedVideo = video
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // More Samples Category Row
                    item {
                        Column {
                            SectionHeader(title = "More Samples", modifier = Modifier.padding(horizontal = 16.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 32.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.grid_view),
                                        onClick = onVerticalGridClick
                                    )
                                }
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.guidedstep_first_title),
                                        onClick = onGuidedStepClick
                                    )
                                }
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.error_fragment),
                                        onClick = onErrorFragmentClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun SampleActionCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompactCard(
        onClick = onClick,
        image = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
        },
        scale = CardDefaults.scale(focusedScale = 1.1f),
        modifier = modifier.width(180.dp)
    )
}
