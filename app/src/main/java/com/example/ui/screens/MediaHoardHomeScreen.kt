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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.components.HeroBanner
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun MediaHoardHomeScreen(
    viewModel: StreamNovaViewModel = viewModel(),
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()

    val featuredMovie = movies.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("mediahoard_home_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Welcome back, Hoarder",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "AIOMetadata Multi-Hub Ecosystem Connected",
                    style = MaterialTheme.typography.labelSmall,
                    color = NovaPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Hero Spotlight
        if (featuredMovie != null) {
            item {
                HeroBanner(
                    meta = featuredMovie,
                    onPlayClick = { onItemClick(featuredMovie.type, featuredMovie.id) },
                    onDetailsClick = { onItemClick(featuredMovie.type, featuredMovie.id) }
                )
            }
        }

        // Continue Watching
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            .height(90.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Surface(
                                    color = Color(0xFF1F2338),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF7E84A6))
                                    }
                                }
                                Text("No media currently in progress.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF8B92B2))
                            }
                        }
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(continueWatching) { item ->
                            ContinueWatchingCard(item = item, onClick = { onItemClick(item.type, item.id) })
                        }
                    }
                }
            }
        }

        // AIOMetadata Trending Movies
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AIOMetadata Trending Movies",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("TMDB / IMDb", color = NovaPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(movies) { movie ->
                        CatalogCard(meta = movie, onClick = { onItemClick(movie.type, movie.id) })
                    }
                }
            }
        }

        // Top Rated Series Hub
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "AIOStreams Popular TV Shows",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(series) { show ->
                        CatalogCard(meta = show, onClick = { onItemClick(show.type, show.id) })
                    }
                }
            }
        }
    }
}
