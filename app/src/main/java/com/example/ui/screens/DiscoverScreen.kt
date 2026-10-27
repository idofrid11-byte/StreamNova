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
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
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
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun DiscoverScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val trending by viewModel.trending.collectAsStateWithLifecycle()

    var selectedType by remember { mutableStateOf("all") } // all, movie, series
    var selectedGenre by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val genres = remember {
        listOf("All", "Action", "Sci-Fi", "Drama", "Crime", "Animation", "Comedy", "Thriller", "Adventure")
    }

    val typeOptions = remember {
        listOf(
            "all" to "🔥 All Trending",
            "movie" to "🎬 Movies",
            "series" to "📺 TV Series"
        )
    }

    val allMetas = remember(movies, series, trending, selectedType) {
        when (selectedType) {
            "movie" -> movies
            "series" -> series
            else -> (trending + movies + series).distinctBy { it.id }
        }
    }

    val filteredList = remember(allMetas, selectedGenre, searchQuery) {
        allMetas.filter { meta ->
            val matchesGenre = selectedGenre == "All" || (meta.genres?.any { it.equals(selectedGenre, ignoreCase = true) } == true)
            val matchesSearch = searchQuery.isBlank() || meta.name.contains(searchQuery, ignoreCase = true) || (meta.description?.contains(searchQuery, ignoreCase = true) == true)
            matchesGenre && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("discover_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "✨ Discover & Browse",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("discover_search_input"),
                placeholder = { Text("Filter by title, actor, or keyword...", color = Color(0xFF6B7280)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NovaPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NovaPrimary,
                    unfocusedBorderColor = Color(0xFF22263C),
                    focusedContainerColor = Color(0xFF131526),
                    unfocusedContainerColor = Color(0xFF131526),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // Type Segmented Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                typeOptions.forEach { (typeKey, label) ->
                    val isSelected = selectedType == typeKey
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedType = typeKey }
                            .testTag("discover_type_$typeKey"),
                        color = if (isSelected) NovaPrimary else Color(0xFF15182C),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NovaPrimary else Color(0xFF22263C)
                        )
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else Color(0xFF8B92B2),
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Genre Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(genres) { genre ->
                    val isSelected = selectedGenre == genre
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedGenre = genre },
                        label = { Text(genre, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NovaAccentCyan.copy(alpha = 0.2f),
                            selectedLabelColor = NovaAccentCyan,
                            containerColor = Color(0xFF131526),
                            labelColor = Color(0xFF8B92B2)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NovaAccentCyan else Color(0xFF22263C)
                        )
                    )
                }
            }
        }

        // Grid Content
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color(0xFF4A4E69),
                        modifier = Modifier.size(48.dp)
                    )
                    Text("No matching titles found", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Try adjusting your search query or genre filter.", color = Color(0xFF8B92B2), fontSize = 13.sp)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 130.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                items(filteredList) { item ->
                    CatalogCard(
                        meta = item,
                        onClick = { onItemClick(item.type, item.id) }
                    )
                }
            }
        }
    }
}
