package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.CatalogCard
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun SearchScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit
) {
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filter by viewModel.searchFilter.collectAsStateWithLifecycle()
    val results by viewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingSearch.collectAsStateWithLifecycle()

    val popularGenres = listOf("Action", "Sci-Fi", "Comedy", "Drama", "Horror", "Animation", "Crime", "Thriller")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("search_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Search & Explore",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = { Text("Search movies, TV series, or actors...", color = Color(0xFF757C9E)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NovaPrimary
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = Color(0xFFA0A5C2)
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_text_field"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NovaPrimary,
                unfocusedBorderColor = Color(0xFF282D46),
                focusedContainerColor = Color(0xFF131627),
                unfocusedContainerColor = Color(0xFF131627),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        // Filter Type Buttons (Movies vs TV Series)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filter == "movie",
                onClick = { viewModel.onSearchFilterChanged("movie") },
                label = { Text("Movies") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NovaPrimary,
                    selectedLabelColor = Color.White
                )
            )
            FilterChip(
                selected = filter == "series",
                onClick = { viewModel.onSearchFilterChanged("series") },
                label = { Text("TV Series") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = NovaPrimary,
                    selectedLabelColor = Color.White
                )
            )
        }

        // Quick Genre Discovery Pills
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Discover by Genre",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF8B92B2),
                fontWeight = FontWeight.SemiBold
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(popularGenres) { genre ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { viewModel.onSearchQueryChanged(genre) },
                        color = Color(0xFF181B2E),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = genre,
                            color = Color(0xFFD0D4EA),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Loading or Results State
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = NovaPrimary)
            }
        } else if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF383E63),
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = if (query.isEmpty()) "Search Across Cinemeta Catalogs" else "No matching titles found",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (query.isEmpty()) "Type a title, genre, or select a genre above to start exploring." else "Try another keyword, genre, or check your installed addons.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7B81A0)
                    )
                }
            }
        } else {
            // Results Grid
            Text(
                text = "${results.size} Results",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF9EA5C8),
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(110.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(results, key = { "search_${it.id}" }) { item ->
                    CatalogCard(
                        meta = item,
                        onClick = { onItemClick(item.type, item.id) },
                        width = 110,
                        height = 180
                    )
                }
            }
        }
    }
}
