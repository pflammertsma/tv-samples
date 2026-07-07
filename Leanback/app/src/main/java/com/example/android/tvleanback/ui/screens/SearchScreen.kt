package com.example.android.tvleanback.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.android.tvleanback.data.VideoRepository
import com.example.android.tvleanback.model.Video
import com.example.android.tvleanback.ui.components.LoadingIndicator
import com.example.android.tvleanback.ui.components.MovieCard
import com.example.android.tvleanback.ui.theme.TvLeanbackTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SearchScreen(
    onVideoClick: (Video) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var allVideos by remember { mutableStateOf<List<Video>?>(null) }
    var query by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        allVideos = VideoRepository.getVideos(context)
    }

    val filteredVideos = remember(allVideos, query) {
        allVideos?.filter { video ->
            if (query.isBlank()) true
            else {
                (video.title?.contains(query, ignoreCase = true) == true) ||
                (video.description?.contains(query, ignoreCase = true) == true) ||
                (video.category?.contains(query, ignoreCase = true) == true)
            }
        } ?: emptyList()
    }

    TvLeanbackTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp)
        ) {
            // Search Header Box
            Surface(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.border,
                        shape = MaterialTheme.shapes.small
                    )
                    .onFocusChanged { isFocused = it.isFocused }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search: ",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box {
                                if (query.isEmpty()) {
                                    Text(
                                        text = "Type title, category, or description...",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            // Results Grid
            if (allVideos == null) {
                LoadingIndicator()
            } else if (filteredVideos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No matching videos found for \"$query\"",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Text(
                    text = "Results (${filteredVideos.size})",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredVideos) { video ->
                        MovieCard(
                            video = video,
                            onClick = onVideoClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
