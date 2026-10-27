package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun WuPlayWatchlistScreen(
    viewModel: StreamNovaViewModel = viewModel(),
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("wuplay_watchlist_screen")
    ) {
        Text(
            text = "My Watchlist",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "${favorites.size} titles saved to profile",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF8B92B2)
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("Your watchlist is empty. Add movies and shows from Home or Details.", color = Color(0xFF8B92B2))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 135.dp),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                items(favorites) { fav ->
                    val meta = MetaItem(
                        id = fav.id,
                        type = fav.type,
                        name = fav.name,
                        poster = fav.poster,
                        background = null,
                        logo = null,
                        description = null,
                        releaseInfo = null,
                        imdbRating = null,
                        genres = null,
                        videos = null
                    )
                    CatalogCard(
                        meta = meta,
                        onClick = { onItemClick(fav.type, fav.id) }
                    )
                }
            }
        }
    }
}
