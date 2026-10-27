package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.data.db.LibraryEntity
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun LibraryScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit,
    onPlayDirect: (streamUrl: String, title: String) -> Unit = { _, _ -> }
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()
    val allLibrary by viewModel.libraryItems.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0: Continue, 1: Favorites, 2: All
    var showClearConfirm by remember { mutableStateOf(false) }

    val tabTitles = listOf(
        "Continue Watching (${continueWatching.size})",
        "Favorites (${favorites.size})",
        "All Items (${allLibrary.size})"
    )

    val currentItems = when (selectedTab) {
        0 -> continueWatching
        1 -> favorites
        else -> allLibrary
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear Watch History?") },
            text = { Text("This will reset playback progress on all items in your library.", color = Color(0xFFA5ABC8)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearWatchHistory()
                        showClearConfirm = false
                    }
                ) {
                    Text("Clear All", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF131526)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("library_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📚 My Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Your synchronized bookmarks and watch history",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }

                if (selectedTab == 0 && continueWatching.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirm = true },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFF1F2238))
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear History",
                            tint = Color(0xFFFF5252)
                        )
                    }
                }
            }

            // Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF0E101D),
                contentColor = Color.White,
                edgePadding = 0.dp,
                divider = {}
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) NovaAccentCyan else Color(0xFF8B92B2),
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }
        }

        // Items list / grid
        if (currentItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Default.FavoriteBorder else Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = Color(0xFF3B4060),
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = when (selectedTab) {
                            0 -> "No items currently in progress"
                            1 -> "No favorites bookmarked yet"
                            else -> "Your library is empty"
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Browse Discover or Search to start watching and collecting media.",
                        color = Color(0xFF8B92B2),
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                items(currentItems) { item ->
                    LibraryItemCard(
                        item = item,
                        onClick = { onItemClick(item.type, item.id) },
                        onRemove = { viewModel.removeFromLibrary(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryItemCard(
    item: LibraryEntity,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .testTag("library_item_${item.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .size(width = 65.dp, height = 90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1D2138))
            ) {
                AsyncImage(
                    model = item.poster,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Info & Progress
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = NovaPrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.type.uppercase(),
                            color = NovaAccentCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (item.isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.progress > 0f) {
                    val percentage = (item.progress * 100).toInt()
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Progress", fontSize = 11.sp, color = Color(0xFF8B92B2))
                            Text("$percentage%", fontSize = 11.sp, color = NovaAccentCyan, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { item.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = NovaPrimary,
                            trackColor = Color(0xFF22263C)
                        )
                    }
                } else {
                    Text(
                        text = "Saved in library",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }
            }

            // Actions
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Remove",
                    tint = Color(0xFF7E84A6),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
