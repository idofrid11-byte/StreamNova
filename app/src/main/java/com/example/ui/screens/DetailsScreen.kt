package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.data.model.StreamItem
import com.example.data.model.VideoEpisode
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun DetailsScreen(
    type: String,
    id: String,
    viewModel: StreamNovaViewModel,
    onBackClick: () -> Unit,
    onPlayClick: (streamUrl: String, title: String) -> Unit
) {
    var meta by remember { mutableStateOf<MetaItem?>(null) }
    var streams by remember { mutableStateOf<List<StreamItem>>(emptyList()) }
    var selectedEpisode by remember { mutableStateOf<VideoEpisode?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val isFavorite = remember(favorites, id) {
        favorites.any { it.id == id }
    }

    LaunchedEffect(id) {
        isLoading = true
        val fetchedMeta = viewModel.getMetaDetails(type, id)
        meta = fetchedMeta
        selectedEpisode = fetchedMeta?.videos?.firstOrNull()

        val fetchedStreams = viewModel.getStreamsFor(type, id, fetchedMeta?.name)
        streams = fetchedStreams
        isLoading = false
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("details_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            meta?.let { viewModel.toggleFavorite(it) }
                        },
                        modifier = Modifier.testTag("details_favorite_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) NovaPrimary else Color.White
                        )
                    }
                }
            }
        },
        containerColor = Color(0xFF090A10)
    ) { innerPadding ->
        if (isLoading && meta == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NovaPrimary)
            }
        } else {
            val item = meta ?: MetaItem(id = id, name = "Stream Details", type = type)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("details_scroll_view"),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Backdrop & Poster Header
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    ) {
                        AsyncImage(
                            model = item.background ?: item.poster,
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0x33090A10),
                                            Color(0x88090A10),
                                            Color(0xFF090A10)
                                        )
                                    )
                                )
                        )

                        // Title & Rating Overlay
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(85.dp)
                                    .height(125.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color(0xFF333857), RoundedCornerShape(8.dp)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                AsyncImage(
                                    model = item.poster ?: item.background,
                                    contentDescription = item.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (!item.imdbRating.isNullOrBlank()) {
                                        Row(
                                            modifier = Modifier
                                                .background(Color(0xDD000000), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = NovaRatingGold,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = item.imdbRating,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    if (!item.releaseInfo.isNullOrBlank()) {
                                        Text(
                                            text = item.releaseInfo,
                                            color = Color(0xFFA5ABC8),
                                            fontSize = 12.sp
                                        )
                                    }

                                    if (!item.runtime.isNullOrBlank()) {
                                        Text(
                                            text = "• ${item.runtime}",
                                            color = Color(0xFFA5ABC8),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Genre Chips
                if (!item.genres.isNullOrEmpty()) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(item.genres) { genre ->
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF191C2E)
                                ) {
                                    Text(
                                        text = genre,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFC5C9E5),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Synopsis
                if (!item.description.isNullOrBlank()) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = item.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFB4BACF),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Episodes list if TV Series with EpisodeItem
                if (!item.videos.isNullOrEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Episodes (${item.videos.size})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    items(item.videos) { ep ->
                        val isSelected = selectedEpisode?.id == ep.id
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            EpisodeItem(
                                episode = ep,
                                isSelected = isSelected,
                                onClick = {
                                    selectedEpisode = ep
                                    val streamTitle = "${item.name} - S${ep.season ?: 1}E${ep.episode ?: 1}: ${ep.title}"
                                    val defaultUrl = streams.firstOrNull()?.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                                    onPlayClick(defaultUrl, streamTitle)
                                }
                            )
                        }
                    }
                }

                // Available Streams Section
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Available Streams (${streams.size})",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Torrentio & Community Addons",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF888FAE)
                            )
                        }

                        if (streams.isEmpty()) {
                            Text(
                                text = "Scraping streams from installed addons...",
                                color = Color(0xFF888FAE),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                items(streams) { stream ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        StreamCard(
                            stream = stream,
                            itemTitle = item.name,
                            onPlayClick = {
                                val targetUrl = stream.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                                val currentTitle = if (selectedEpisode != null) {
                                    "${item.name} S${selectedEpisode?.season ?: 1}E${selectedEpisode?.episode ?: 1}: ${selectedEpisode?.title}"
                                } else {
                                    item.name
                                }
                                onPlayClick(targetUrl, currentTitle)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: VideoEpisode,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .border(
                1.dp,
                if (isSelected) NovaPrimary else Color(0xFF25293F),
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1B1F34) else Color(0xFF131525)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!episode.thumbnail.isNullOrEmpty()) {
                AsyncImage(
                    model = episode.thumbnail,
                    contentDescription = episode.title,
                    modifier = Modifier
                        .width(110.dp)
                        .height(68.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                Surface(
                    color = Color(0xFF22263D),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .width(90.dp)
                        .height(60.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "S${episode.season ?: 1} E${episode.episode ?: 1}: ${episode.title.ifEmpty { "Episode " + (episode.episode ?: 1) }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (!episode.imdbRating.isNullOrEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NovaRatingGold,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "IMDb ${episode.imdbRating}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFA5ABC8),
                            fontSize = 11.sp
                        )
                    }
                }

                if (!episode.released.isNullOrEmpty()) {
                    Text(
                        text = episode.released,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7E85A8),
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(
                onClick = onClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Episode",
                    tint = NovaPrimary
                )
            }
        }
    }
}

@Composable
fun StreamCard(
    stream: StreamItem,
    itemTitle: String,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF23273D), RoundedCornerShape(12.dp))
            .clickable(onClick = onPlayClick)
            .testTag("stream_item_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131524)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = when (stream.quality) {
                            "4K Remux", "4K UHD" -> Color(0xFF7C4DFF)
                            "1080p Ultra HD", "1080p FHD" -> Color(0xFF00B0FF)
                            else -> Color(0xFF00BFA5)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = stream.quality,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = stream.provider,
                        color = Color(0xFF8D94B8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = stream.title ?: stream.name ?: itemTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("play_stream_button")
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Play", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
