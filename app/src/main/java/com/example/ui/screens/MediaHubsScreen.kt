package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

enum class HubType(val displayName: String, val icon: ImageVector, val tag: String) {
    LIVE_TV("Live TV", Icons.Default.Tv, "live_tv"),
    MUSIC("Music", Icons.Default.MusicNote, "music"),
    PODCASTS("AudioBooks", Icons.Default.Podcasts, "podcasts"),
    MANGA("Manga & Books", Icons.Default.MenuBook, "manga"),
    GAMES("Retro Games", Icons.Default.SportsEsports, "games"),
    DEBRID_VAULT("Debrid Vault", Icons.Default.CloudDownload, "debrid")
}

data class HubItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val posterUrl: String,
    val streamUrl: String?,
    val badge: String? = null,
    val isLive: Boolean = false
)

@Composable
fun MediaHubsScreen(
    viewModel: StreamNovaViewModel,
    onPlayMedia: (streamUrl: String, title: String) -> Unit,
    onItemClick: (type: String, id: String) -> Unit = { _, _ -> }
) {
    var selectedHub by remember { mutableStateOf(HubType.LIVE_TV) }
    var searchQuery by remember { mutableStateOf("") }

    val liveTvChannels = remember {
        listOf(
            HubItem(
                id = "live_nasa",
                title = "NASA TV Live HD",
                subtitle = "Official 24/7 Space & ISS Broadcast",
                category = "Science & Space",
                posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                badge = "1080p 60fps",
                isLive = true
            ),
            HubItem(
                id = "live_bloomberg",
                title = "Global News 24/7",
                subtitle = "Financial markets, technology & breaking news",
                category = "News",
                posterUrl = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                badge = "LIVE HD",
                isLive = true
            ),
            HubItem(
                id = "live_sports",
                title = "Red Bull Action Sports",
                subtitle = "Extreme sports, mountain biking, drifting",
                category = "Sports",
                posterUrl = "https://images.unsplash.com/photo-1517649763962-0c623266ddc0?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                badge = "4K UHD",
                isLive = true
            ),
            HubItem(
                id = "live_cinema",
                title = "Retro Cinema Classic",
                subtitle = "Golden era sci-fi, noir, and cult film streams",
                category = "Cinema",
                posterUrl = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                badge = "CLASSIC",
                isLive = true
            ),
            HubItem(
                id = "live_nature",
                title = "Nature & Wildlife 4K",
                subtitle = "Ambient oceans, rainforests, and wildlife documentary",
                category = "Documentary",
                posterUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
                badge = "HDR 10+",
                isLive = true
            )
        )
    }

    val musicChannels = remember {
        listOf(
            HubItem(
                id = "music_lofi",
                title = "Lofi Hip Hop Radio",
                subtitle = "Beats to relax/study to (24/7 Audio Stream)",
                category = "Chill",
                posterUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                badge = "Lossless",
                isLive = true
            ),
            HubItem(
                id = "music_synth",
                title = "Synthwave 80s Cyber Radio",
                subtitle = "Retro outrun, chillwave & electronic drives",
                category = "Electronic",
                posterUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4",
                badge = "320 kbps",
                isLive = true
            ),
            HubItem(
                id = "music_classical",
                title = "Philharmonic Symphony Live",
                subtitle = "Orchestral masterpieces & piano concerts",
                category = "Classical",
                posterUrl = "https://images.unsplash.com/photo-1465847899084-d164df4dedc6?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                badge = "Hi-Res Audio",
                isLive = true
            )
        )
    }

    val podcastsAudiobooks = remember {
        listOf(
            HubItem(
                id = "book_dune",
                title = "Dune: Complete Chronicle",
                subtitle = "Frank Herbert • Narrated by Full Cast (18h 42m)",
                category = "Sci-Fi Audiobook",
                posterUrl = "https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                badge = "Ch. 1-28"
            ),
            HubItem(
                id = "book_cyber",
                title = "Neuromancer (Unabridged)",
                subtitle = "William Gibson • Sprawl Trilogy Book 1",
                category = "Cyberpunk",
                posterUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                badge = "Ch. 1-12"
            ),
            HubItem(
                id = "pod_lex",
                title = "Artificial Intelligence Podcast",
                subtitle = "Discussions on Deep Learning, Robotics & Space",
                category = "Tech Podcast",
                posterUrl = "https://images.unsplash.com/photo-1485827404703-89b55fcc595e?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                badge = "Ep. 412"
            )
        )
    }

    val mangaBooks = remember {
        listOf(
            HubItem(
                id = "manga_berserk",
                title = "Berserk: Deluxe Edition",
                subtitle = "Kentaro Miura • Vol 1-41 Digital Scans",
                category = "Dark Fantasy",
                posterUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600",
                streamUrl = null,
                badge = "364 Chapters"
            ),
            HubItem(
                id = "manga_solo",
                title = "Solo Leveling (Omnibus)",
                subtitle = "Chugong • Full Color Webtoon High Res",
                category = "Action / Manhwa",
                posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600",
                streamUrl = null,
                badge = "200 Chapters"
            ),
            HubItem(
                id = "manga_onepiece",
                title = "One Piece: Wano Arc",
                subtitle = "Eiichiro Oda • Shonen Jump Official",
                category = "Adventure",
                posterUrl = "https://images.unsplash.com/photo-1563089145-599997674d42?w=600",
                streamUrl = null,
                badge = "Latest Vol"
            )
        )
    }

    val gamesLauncher = remember {
        listOf(
            HubItem(
                id = "game_chrono",
                title = "Chrono Trigger (SNES)",
                subtitle = "Square • Cloud Save Synced • RetroArch Ready",
                category = "JRPG",
                posterUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=600",
                streamUrl = null,
                badge = "SNES Core"
            ),
            HubItem(
                id = "game_metroid",
                title = "Super Metroid (Zero Mission)",
                subtitle = "Nintendo • 60 FPS HD Texture Pack",
                category = "Platformer",
                posterUrl = "https://images.unsplash.com/photo-1551103782-8ab07afd45c1?w=600",
                streamUrl = null,
                badge = "GBA Core"
            ),
            HubItem(
                id = "game_castlevania",
                title = "Castlevania: Symphony of the Night",
                subtitle = "Konami • PS1 Classic • Hi-Fi Audio Pack",
                category = "Metroidvania",
                posterUrl = "https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=600",
                streamUrl = null,
                badge = "DuckStation"
            )
        )
    }

    val debridVault = remember {
        listOf(
            HubItem(
                id = "deb_oppenheimer",
                title = "Oppenheimer (2023) [Remux 2160p]",
                subtitle = "Real-Debrid Cached (78.4 GB) • Dolby Vision TrueHD 7.1",
                category = "Cached Debrid",
                posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                badge = "10 Gbps RD"
            ),
            HubItem(
                id = "deb_blade",
                title = "Blade Runner 2049 [4K HDR10+]",
                subtitle = "AllDebrid Instant Unrestrict (52.1 GB)",
                category = "Cached Debrid",
                posterUrl = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                badge = "Instant CDN"
            ),
            HubItem(
                id = "deb_interstellar",
                title = "Interstellar IMAX Remux",
                subtitle = "Premiumize Cloud Storage (64.2 GB)",
                category = "Cached Debrid",
                posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
                streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                badge = "Cloud Cache"
            )
        )
    }

    val activeList = when (selectedHub) {
        HubType.LIVE_TV -> liveTvChannels
        HubType.MUSIC -> musicChannels
        HubType.PODCASTS -> podcastsAudiobooks
        HubType.MANGA -> mangaBooks
        HubType.GAMES -> gamesLauncher
        HubType.DEBRID_VAULT -> debridVault
    }.filter {
        searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("media_hubs_screen")
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🌐 MediaHoard Hubs",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Universal Multi-Format Streaming & Content Hub",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF131525),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262A45))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                        )
                        Text(
                            text = "${activeList.size} Ready",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search filter within Hub
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hubs_search_input"),
                placeholder = { Text("Filter channels, books, games, or debrid...", color = Color(0xFF6B7280)) },
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

            Spacer(modifier = Modifier.height(12.dp))

            // Hub Category Selector Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(HubType.values()) { hub ->
                    val isSelected = selectedHub == hub
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedHub = hub }
                            .testTag("hub_tab_${hub.tag}"),
                        color = if (isSelected) NovaPrimary else Color(0xFF15182C),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) NovaPrimary else Color(0xFF232742)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = hub.icon,
                                contentDescription = hub.displayName,
                                tint = if (isSelected) Color.White else Color(0xFF8B92B2),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = hub.displayName,
                                color = if (isSelected) Color.White else Color(0xFFA5ABC8),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Content Grid
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            items(activeList) { item ->
                HubItemCard(
                    item = item,
                    onPlayClick = {
                        if (item.streamUrl != null) {
                            onPlayMedia(item.streamUrl, item.title)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun HubItemCard(
    item: HubItem,
    onPlayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onPlayClick)
            .testTag("hub_item_${item.id}"),
        shape = RoundedCornerShape(14.dp),
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
            // Poster / Thumbnail
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E2238))
            ) {
                AsyncImage(
                    model = item.posterUrl,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (item.isLive) {
                    Surface(
                        color = Color(0xFFE50914),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                if (item.streamUrl != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NovaPrimary.copy(alpha = 0.9f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = NovaAccentCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    item.badge?.let {
                        Surface(
                            color = Color(0xFF222640),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = it,
                                color = NovaRatingGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B92B2),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action Button
            IconButton(
                onClick = onPlayClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF1E223A))
            ) {
                Icon(
                    imageVector = if (item.streamUrl != null) Icons.Default.PlayArrow else Icons.Default.OpenInNew,
                    contentDescription = "Launch",
                    tint = NovaPrimary
                )
            }
        }
    }
}
