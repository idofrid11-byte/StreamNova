package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
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
import com.example.data.db.LibraryEntity
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.components.HeroBanner
import com.example.ui.components.TvCatalogCard
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@OptIn(
    androidx.compose.foundation.ExperimentalFoundationApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.tv.material3.ExperimentalTvMaterial3Api::class
)
@Composable
fun HomeScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit,
    onDirectPlayClick: (streamUrl: String, title: String) -> Unit
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val trending by viewModel.trending.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingHome.collectAsStateWithLifecycle()

    var selectedFilter by remember { mutableStateOf("all") } // all, movies, series, library
    val firstItemFocus = remember { FocusRequester() }

    val featuredItem = remember(movies, series) {
        movies.firstOrNull { it.background != null || it.poster != null }
            ?: series.firstOrNull()
    }

    LaunchedEffect(movies) {
        if (movies.isNotEmpty()) {
            try {
                firstItemFocus.requestFocus()
            } catch (e: Exception) {
                // Ignore if not yet attached
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // App Bar & Brand
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NovaPrimary,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "StreamNova TV",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Stremio Protocol Catalog • D-Pad & Touch Ready",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF8C92B5)
                    )
                }

                IconButton(
                    onClick = { viewModel.loadHomeCatalogs() },
                    modifier = Modifier.testTag("refresh_home_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = Color(0xFFA5ACD0)
                    )
                }
            }
        }

        // Category Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "all",
                    onClick = { selectedFilter = "all" },
                    label = { Text("Discover All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NovaPrimary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == "movies",
                    onClick = { selectedFilter = "movies" },
                    label = { Text("Movies") }
                )
                FilterChip(
                    selected = selectedFilter == "series",
                    onClick = { selectedFilter = "series" },
                    label = { Text("Series") }
                )
                FilterChip(
                    selected = selectedFilter == "library",
                    onClick = { selectedFilter = "library" },
                    label = { Text("My Library") }
                )
            }
        }

        // Loading indicator if fetching
        if (isLoading && movies.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NovaPrimary)
                }
            }
        }

        // Hero Banner
        if (selectedFilter != "library" && featuredItem != null) {
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    HeroBanner(
                        meta = featuredItem,
                        onPlayClick = {
                            onItemClick(featuredItem.type, featuredItem.id)
                        },
                        onDetailsClick = {
                            onItemClick(featuredItem.type, featuredItem.id)
                        }
                    )
                }
            }
        }

        // Continue Watching Section
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Continue Watching",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (continueWatching.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF232742))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = Color(0xFF1E2238),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color(0xFF7E85A8),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "No recent watch history",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Start playing any title to pick up right where you left off.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF7E85A8)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.focusRestorer(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(continueWatching, key = { it.id }) { item ->
                            ContinueWatchingCard(
                                item = item,
                                onClick = { onItemClick(item.type, item.id) }
                            )
                        }
                    }
                }
            }
        }

        // Library Section (if library tab selected)
        if (selectedFilter == "library") {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Saved to My Library (${favorites.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (favorites.isEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF141724)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = Color(0xFF636A8C),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your Library is Empty",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Tap the bookmark icon on any movie or series to save it here for instant access.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF888EA8),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    } else {
                        LazyRow(
                            modifier = Modifier.focusRestorer(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favorites) { fav ->
                                TvCatalogCard(
                                    meta = MetaItem(
                                        id = fav.id,
                                        type = fav.type,
                                        name = fav.name,
                                        poster = fav.poster,
                                        background = fav.backdrop,
                                        imdbRating = fav.rating,
                                        releaseInfo = fav.releaseYear
                                    ),
                                    onClick = { onItemClick(fav.type, fav.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Popular Movies Section
        if (selectedFilter == "all" || selectedFilter == "movies") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Movies",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Cinemeta Top",
                            style = MaterialTheme.typography.labelSmall,
                            color = NovaPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    LazyRow(
                        modifier = Modifier.focusRestorer(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        itemsIndexed(movies, key = { _, item -> item.id }) { index, movie ->
                            TvCatalogCard(
                                meta = movie,
                                modifier = if (index == 0) Modifier.focusRequester(firstItemFocus) else Modifier,
                                onClick = { onItemClick(movie.type, movie.id) }
                            )
                        }
                    }
                }
            }
        }

        // Top Rated Series Section
        if (selectedFilter == "all" || selectedFilter == "series") {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Series",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Trending Shows",
                            style = MaterialTheme.typography.labelSmall,
                            color = NovaPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    LazyRow(
                        modifier = Modifier.focusRestorer(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(series, key = { it.id }) { show ->
                            TvCatalogCard(
                                meta = show,
                                onClick = { onItemClick(show.type, show.id) }
                            )
                        }
                    }
                }
            }
        }

        // Trending Carousel Section
        if (selectedFilter == "all" && trending.isNotEmpty()) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trending Now",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    LazyRow(
                        modifier = Modifier.focusRestorer(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(trending, key = { "trend_${it.id}" }) { item ->
                            TvCatalogCard(
                                meta = item,
                                onClick = { onItemClick(item.type, item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContinueWatchingCard(
    item: LibraryEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("continue_card_${item.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161928)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.backdrop ?: item.poster,
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Bottom gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x99000000), Color(0xEE090A10))
                        )
                    )
            )

            // Play icon in center
            Surface(
                color = Color(0xAA000000),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .size(36.dp)
                    .align(Alignment.Center)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Resume",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Info and Progress bar at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { item.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = NovaPrimary,
                    trackColor = Color(0x55FFFFFF)
                )
            }
        }
    }
}
