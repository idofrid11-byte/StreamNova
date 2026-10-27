package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults as TvCardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.data.model.StreamItem
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

data class CastMember(
    val id: String,
    val name: String,
    val role: String,
    val photoUrl: String
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WuPlayDetailsScreen(
    type: String,
    id: String,
    viewModel: StreamNovaViewModel? = null,
    onPlayClick: (String) -> Unit,
    onPersonClick: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var meta by remember { mutableStateOf<MetaItem?>(null) }
    var streams by remember { mutableStateOf<List<StreamItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val castList = remember {
        listOf(
            CastMember("cast_1", "Cillian Murphy", "J. Robert Oppenheimer", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400"),
            CastMember("cast_2", "Emily Blunt", "Katherine Oppenheimer", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400"),
            CastMember("cast_3", "Matt Damon", "Leslie Groves", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400"),
            CastMember("cast_4", "Robert Downey Jr.", "Lewis Strauss", "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=400"),
            CastMember("cast_5", "Florence Pugh", "Jean Tatlock", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400")
        )
    }

    val similarTitles = remember {
        listOf(
            MetaItem(
                id = "tt0816692",
                type = "movie",
                name = "Interstellar",
                poster = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
                background = null,
                logo = null,
                description = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
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
                description = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea.",
                releaseInfo = "2010",
                imdbRating = "8.8",
                genres = listOf("Action", "Sci-Fi"),
                videos = null
            ),
            MetaItem(
                id = "tt0468569",
                type = "movie",
                name = "The Dark Knight",
                poster = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600",
                background = null,
                logo = null,
                description = "When the menace known as the Joker wreaks havoc on Gotham, Batman must accept one of the greatest psychological tests.",
                releaseInfo = "2008",
                imdbRating = "9.0",
                genres = listOf("Action", "Crime", "Drama"),
                videos = null
            )
        )
    }

    LaunchedEffect(id) {
        isLoading = true
        if (viewModel != null) {
            val fetchedMeta = viewModel.getMetaDetails(type, id)
            meta = fetchedMeta ?: MetaItem(
                id = id,
                type = type,
                name = "Media Details ($id)",
                poster = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600",
                background = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200",
                logo = null,
                description = "Masterpiece cinematic production featuring ultra high definition streams, dynamic audio passthrough, and full subtitle synchronization.",
                releaseInfo = "2024",
                imdbRating = "8.8",
                genres = listOf("Sci-Fi", "Drama", "Thriller"),
                videos = null
            )
            streams = viewModel.getStreamsFor(type, id, meta?.name)
        } else {
            meta = MetaItem(
                id = id,
                type = type,
                name = "Featured Title",
                poster = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600",
                background = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200",
                logo = null,
                description = "An extraordinary cinematic journey through deep space, featuring stellar sound design and award-winning cinematography.",
                releaseInfo = "2024",
                imdbRating = "8.9",
                genres = listOf("Action", "Sci-Fi"),
                videos = null
            )
            streams = listOf(
                StreamItem(
                    name = "Torrentio [RD+]",
                    title = "4K Remux HEVC 2160p HDR10+ • Atmos 7.1 • 54.2 GB",
                    url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    infoHash = null,
                    behaviorHints = null
                ),
                StreamItem(
                    name = "AIOStreams [1080p]",
                    title = "1080p Web-DL 5.1ch Surround • 8.4 GB",
                    url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                    infoHash = null,
                    behaviorHints = null
                )
            )
        }
        isLoading = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("wuplay_details_screen"),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF22263C), RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = meta?.background ?: meta?.poster,
                    contentDescription = meta?.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xAA090A10), Color(0xFF090A10))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = NovaPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = type.uppercase(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        meta?.imdbRating?.let { rating ->
                            Surface(
                                color = Color(0xCC000000),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = NovaRatingGold, modifier = Modifier.size(14.dp))
                                    Text(rating, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        meta?.releaseInfo?.let {
                            Text(it, color = Color(0xFFA5ABC8), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    Text(
                        text = meta?.name ?: "Loading...",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = meta?.description ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFC0C5DC),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Action Buttons Row
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        val firstStream = streams.firstOrNull()?.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                        onPlayClick(firstStream)
                    },
                    modifier = Modifier.height(48.dp).testTag("wuplay_play_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play Stream", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val movieQuery = Uri.encode("${meta?.name ?: "movie"} trailer")
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$movieQuery"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.height(48.dp).testTag("wuplay_trailer_button"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333857)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Watch Trailer")
                }
            }
        }

        // Available Streams Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Available Streams (${streams.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (streams.isEmpty()) {
                    Text("Resolving streams from active addons...", color = Color(0xFF8B92B2), fontSize = 13.sp)
                } else {
                    streams.forEach { stream ->
                        Card(
                            onClick = { stream.url?.let { onPlayClick(it) } },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("stream_card_${stream.name}"),
                            scale = TvCardDefaults.scale(focusedScale = 1.02f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF131526))
                                    .border(1.dp, Color(0xFF22263C), RoundedCornerShape(12.dp))
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = NovaPrimary.copy(alpha = 0.2f),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NovaAccentCyan, modifier = Modifier.size(20.dp))
                                        }
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                        Text(
                                            text = stream.name ?: "Direct Stream",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = stream.title ?: "1080p Multi-Audio",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF8B92B2),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Surface(
                                    color = Color(0xFF1E223A),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "STREAM",
                                        color = NovaAccentCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Cast & Crew Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Cast & Crew",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(castList) { person ->
                        Card(
                            onClick = { onPersonClick(person.id) },
                            modifier = Modifier
                                .width(120.dp)
                                .height(170.dp)
                                .testTag("cast_card_${person.id}"),
                            scale = TvCardDefaults.scale(focusedScale = 1.08f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF131526))
                                    .border(1.dp, Color(0xFF22263C), RoundedCornerShape(10.dp)),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = person.photoUrl,
                                    contentDescription = person.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = person.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                Text(
                                    text = person.role,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF8B92B2),
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Similar Titles Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Similar Titles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    items(similarTitles) { similar ->
                        Card(
                            onClick = { onPlayClick("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4") },
                            modifier = Modifier
                                .width(130.dp)
                                .height(200.dp),
                            scale = TvCardDefaults.scale(focusedScale = 1.08f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF131526))
                                    .border(1.dp, Color(0xFF22263C), RoundedCornerShape(10.dp))
                            ) {
                                AsyncImage(
                                    model = similar.poster,
                                    contentDescription = similar.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color.Transparent, Color(0xDD000000))
                                            )
                                        )
                                )
                                Text(
                                    text = similar.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
