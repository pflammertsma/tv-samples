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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.launch
import androidx.tv.material3.Button
import androidx.tv.material3.Card
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
    val allVideos by remember {
        VideoRepository.getVideosFlow(context)
    }.collectAsState(initial = null)

    var focusedVideo by remember { mutableStateOf<Video?>(null) }
    val focusRequester = remember { FocusRequester() }
    var initialFocusRequested by remember { mutableStateOf(false) }

    LaunchedEffect(allVideos) {
        if (!allVideos.isNullOrEmpty() && focusedVideo == null) {
            focusedVideo = allVideos?.firstOrNull()
        }
    }

    val categories = remember(allVideos) {
        allVideos?.groupBy { it.category ?: "Other" } ?: emptyMap()
    }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val topBarFocusRequester = remember { FocusRequester() }

    TvLeanbackTheme {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Full-screen backdrop image (always behind everything!)
            if (allVideos != null && focusedVideo != null) {
                AnimatedContent(
                    targetState = focusedVideo,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(600)) togetherWith fadeOut(animationSpec = tween(600))
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
                // Gradient Overlays
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Black.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )
            }

            if (allVideos != null) {
                // Main Content Scroll - Top Bar and Hero Text are ITEM 0!
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(top = 36.dp, bottom = 64.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Item 0: Top Bar (Search/Settings on Right) + Immersive Hero Text on Left!
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 48.dp, end = 48.dp, bottom = 16.dp)
                        ) {
                            // Top Bar Row (Scrolls away with hero banner when lower rows are browsed!)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Button(
                                        onClick = onSearchClick,
                                        modifier = Modifier
                                            .focusRequester(topBarFocusRequester)
                                            .focusProperties { down = focusRequester }
                                    ) {
                                        Text(text = "Search")
                                    }
                                    Button(
                                        onClick = onSettingsClick,
                                        modifier = Modifier.focusProperties { down = focusRequester }
                                    ) {
                                        Text(text = "Settings")
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Immersive Hero Text Section
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(end = 270.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                if (focusedVideo != null) {
                                    Column {
                                        if (!focusedVideo?.studio.isNullOrEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = Color.White.copy(alpha = 0.2f),
                                                        shape = MaterialTheme.shapes.small
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = focusedVideo?.studio?.uppercase() ?: "",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = Color.White
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                        }
                                        Text(
                                            text = focusedVideo?.title ?: "",
                                            style = MaterialTheme.typography.displayMedium,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = focusedVideo?.description ?: "",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White.copy(alpha = 0.85f),
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Video Categories (Items 1..N)
                    categories.entries.forEachIndexed { catIndex, (categoryName, videos) ->
                        item {
                            Column {
                                // In an Immersive List, DO NOT show a title above the first horizontal row!
                                if (catIndex > 0) {
                                    SectionHeader(title = categoryName, modifier = Modifier.padding(horizontal = 32.dp))
                                }
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 48.dp),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                                    modifier = Modifier.then(
                                        if (catIndex == 0) {
                                            Modifier.focusProperties { up = topBarFocusRequester }
                                        } else {
                                            Modifier
                                        }
                                    )
                                ) {
                                    itemsIndexed(videos) { vidIndex, video ->
                                        MovieCard(
                                            video = video,
                                            onClick = onVideoClick,
                                            modifier = Modifier
                                                .then(
                                                    if (catIndex == 0 && vidIndex == 0) {
                                                        Modifier
                                                            .focusRequester(focusRequester)
                                                            .onGloballyPositioned {
                                                                if (!initialFocusRequested) {
                                                                    initialFocusRequested = true
                                                                    focusRequester.requestFocus()
                                                                }
                                                            }
                                                    } else {
                                                        Modifier
                                                    }
                                                )
                                                .onFocusChanged { focusState ->
                                                    if (focusState.isFocused) {
                                                        focusedVideo = video
                                                        coroutineScope.launch {
                                                            if (catIndex == 0) {
                                                                listState.animateScrollToItem(0)
                                                            } else {
                                                                // Scroll so the row ABOVE sits at Y=0, placing this active row (catIndex + 1) cleanly in the vertical center!
                                                                listState.animateScrollToItem(catIndex)
                                                            }
                                                        }
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
                            SectionHeader(title = "More Samples", modifier = Modifier.padding(horizontal = 32.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 48.dp),
                                horizontalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.grid_view),
                                        onClick = onVerticalGridClick,
                                        modifier = Modifier.onFocusChanged {
                                            if (it.isFocused) {
                                                coroutineScope.launch { listState.animateScrollToItem(categories.size) }
                                            }
                                        }
                                    )
                                }
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.guidedstep_first_title),
                                        onClick = onGuidedStepClick,
                                        modifier = Modifier.onFocusChanged {
                                            if (it.isFocused) {
                                                coroutineScope.launch { listState.animateScrollToItem(categories.size) }
                                            }
                                        }
                                    )
                                }
                                item {
                                    SampleActionCard(
                                        title = stringResource(id = R.string.error_fragment),
                                        onClick = onErrorFragmentClick,
                                        modifier = Modifier.onFocusChanged {
                                            if (it.isFocused) {
                                                coroutineScope.launch { listState.animateScrollToItem(categories.size) }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                LoadingIndicator()
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
    Card(
        onClick = onClick,
        scale = CardDefaults.scale(focusedScale = 1.08f),
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
            .width(240.dp)
            .height(120.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
        }
    }
}
