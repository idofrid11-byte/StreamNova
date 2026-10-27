package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.ui.components.CatalogCard
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary

@Composable
fun PersonDetailScreen(
    personId: String,
    onBackClick: () -> Unit,
    onItemClick: (type: String, id: String) -> Unit
) {
    val knownFor = remember {
        listOf(
            MetaItem(
                id = "tt15398776",
                type = "movie",
                name = "Oppenheimer",
                poster = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600",
                background = null,
                logo = null,
                description = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                releaseInfo = "2023",
                imdbRating = "8.9",
                genres = listOf("Biography", "Drama", "History"),
                videos = null
            ),
            MetaItem(
                id = "tt0816692",
                type = "movie",
                name = "Interstellar",
                poster = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
                background = null,
                logo = null,
                description = "Explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                releaseInfo = "2014",
                imdbRating = "8.7",
                genres = listOf("Adventure", "Drama", "Sci-Fi"),
                videos = null
            ),
            MetaItem(
                id = "tt1375666",
                type = "movie",
                name = "Inception",
                poster = "https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=600",
                background = null,
                logo = null,
                description = "A thief steals corporate secrets through dream-sharing technology.",
                releaseInfo = "2010",
                imdbRating = "8.8",
                genres = listOf("Action", "Sci-Fi"),
                videos = null
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(24.dp)
            .testTag("person_detail_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E223A))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(110.dp)
                        .border(2.dp, NovaPrimary, CircleShape),
                    color = Color(0xFF131526)
                ) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
                        contentDescription = "Person",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Lead Actor & Producer",
                        style = MaterialTheme.typography.labelSmall,
                        color = NovaAccentCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Cillian Murphy",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Born May 25, 1976 • Douglas, Ireland",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Biography",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Irish actor who has received numerous accolades, including an Academy Award, a Golden Globe Award, and a BAFTA Award. Known for versatile performances across film and television.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFC0C5DC)
                )
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Known For",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(knownFor) { item ->
                        CatalogCard(
                            meta = item,
                            onClick = { onItemClick(item.type, item.id) }
                        )
                    }
                }
            }
        }
    }
}
