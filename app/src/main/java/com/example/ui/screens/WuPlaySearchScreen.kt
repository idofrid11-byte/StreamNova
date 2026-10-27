package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun WuPlaySearchScreen(
    viewModel: StreamNovaViewModel = viewModel(),
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingSearch.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("wuplay_search_screen")
    ) {
        Text(
            text = "Voice & Text Search",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("wuplay_search_input"),
            placeholder = { Text("Search movies, TV shows, anime...", color = Color(0xFF6B7280)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NovaPrimary) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
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

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator(color = NovaPrimary)
            }
        } else if (searchResults.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(
                    text = if (searchQuery.isBlank()) "Type or speak to search titles across all Stremio addons" else "No results found for \"$searchQuery\"",
                    color = Color(0xFF8B92B2),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 135.dp),
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                items(searchResults) { item ->
                    CatalogCard(
                        meta = item,
                        onClick = { onItemClick(item.type, item.id) }
                    )
                }
            }
        }
    }
}
