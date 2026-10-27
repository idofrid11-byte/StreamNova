package com.example.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults as TvCardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.components.HeroBanner
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WuPlayHomeScreen(
    viewModel: StreamNovaViewModel = viewModel(),
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()

    val firstItemFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            firstItemFocus.requestFocus()
        } catch (_: Exception) {}
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .testTag("wuplay_home_screen"),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // Spotlight / Hero
        movies.firstOrNull()?.let { featured ->
            item {
                HeroBanner(
                    meta = featured,
                    onPlayClick = { onItemClick(featured.type, featured.id) },
                    onDetailsClick = { onItemClick(featured.type, featured.id) }
                )
            }
        }

        // Continue Watching Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Continue Watching",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (continueWatching.isEmpty()) {
                    Card(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .testTag("wuplay_empty_history_card"),
                        scale = TvCardDefaults.scale(focusedScale = 1.02f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF131526))
                                .border(1.dp, Color(0xFF22263C), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF1F2338)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color(0xFF7E84A6)
                                    )
                                }
                                Text(
                                    text = "No watch history yet • Start streaming any title below",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF8B92B2)
                                )
                            }
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.focusGroup()
                    ) {
                        items(continueWatching) { item ->
                            Card(
                                onClick = { onItemClick(item.type, item.id) },
                                modifier = Modifier
                                    .width(220.dp)
                                    .height(130.dp),
                                scale = TvCardDefaults.scale(focusedScale = 1.06f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF131526))
                                        .border(1.dp, Color(0xFF22263C), RoundedCornerShape(12.dp))
                                ) {
                                    AsyncImage(
                                        model = item.poster,
                                        contentDescription = item.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    listOf(Color.Transparent, Color(0xCC000000), Color(0xFF090A10))
                                                )
                                            )
                                    )
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        val pct = (item.progress * 100).toInt()
                                        Text(
                                            text = "$pct% completed",
                                            fontSize = 11.sp,
                                            color = NovaAccentCyan,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Trending Movies Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Movies",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text("Cinemeta / TMDB", color = NovaAccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                LazyRow(
                    modifier = Modifier.focusRequester(firstItemFocus).focusGroup(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(movies) { movie ->
                        CatalogCard(
                            meta = movie,
                            onClick = { onItemClick(movie.type, movie.id) }
                        )
                    }
                }
            }
        }

        // Trending Series Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Popular TV Shows",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyRow(
                    modifier = Modifier.focusGroup(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(series) { show ->
                        CatalogCard(
                            meta = show,
                            onClick = { onItemClick(show.type, show.id) }
                        )
                    }
                }
            }
        }
    }
}
