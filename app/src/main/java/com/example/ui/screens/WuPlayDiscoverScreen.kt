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
import com.example.ui.components.CatalogCard
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun WuPlayDiscoverScreen(
    viewModel: StreamNovaViewModel = viewModel(),
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val allItems = remember(movies, series) { (movies + series).distinctBy { it.id } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("wuplay_discover_screen")
    ) {
        Text(
            text = "Discover Catalogs",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 135.dp),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            items(allItems) { item ->
                CatalogCard(
                    meta = item,
                    onClick = { onItemClick(item.type, item.id) }
                )
            }
        }
    }
}
