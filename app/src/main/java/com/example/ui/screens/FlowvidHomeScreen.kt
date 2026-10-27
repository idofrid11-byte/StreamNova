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
import androidx.compose.material.icons.outlined.*
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
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.ui.theme.*
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun FlowvidHomeScreen(
    viewModel: StreamNovaViewModel,
    onItemClick: (type: String, id: String) -> Unit,
    onPlayDirect: (streamUrl: String, title: String) -> Unit = { _, _ -> },
    onSwitchToTvMode: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val movies by viewModel.movies.collectAsStateWithLifecycle()
    val series by viewModel.series.collectAsStateWithLifecycle()
    val trending by viewModel.trending.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoadingHome.collectAsStateWithLifecycle()

    var selectedGenre by remember { mutableStateOf("All") }
    var streamPickerMeta by remember { mutableStateOf<MetaItem?>(null) }
    var isStreamPickerVisible by remember { mutableStateOf(false) }

    // Fallback curated hero item if trending is empty
    val heroItem = remember(trending, movies) {
        trending.firstOrNull() ?: movies.firstOrNull() ?: MetaItem(
            id = "tt15239678",
            type = "movie",
            name = "Dune: Part Two",
            poster = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800",
            background = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=1200",
            description = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
            releaseInfo = "2024",
            imdbRating = "8.6",
            genres = listOf("Sci-Fi", "Adventure", "Action")
        )
    }

    // Curated high-bitrate 4K Debrid shelf
    val debrid4kPicks = remember {
        listOf(
            MetaItem(
                id = "tt15398776",
                type = "movie",
                name = "Oppenheimer",
                poster = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600",
                description = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.",
                releaseInfo = "2023",
                imdbRating = "8.9",
                genres = listOf("Biography", "Drama", "History")
            ),
            MetaItem(
                id = "tt1160419",
                type = "movie",
                name = "Dune",
                poster = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600",
                description = "A noble family becomes embroiled in a war for control over the galaxy's most valuable asset.",
                releaseInfo = "2021",
                imdbRating = "8.0",
                genres = listOf("Action", "Adventure", "Sci-Fi")
            ),
            MetaItem(
                id = "tt0816692",
                type = "movie",
                name = "Interstellar",
                poster = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
                description = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot is tasked to pilot a spacecraft.",
                releaseInfo = "2014",
                imdbRating = "8.7",
                genres = listOf("Sci-Fi", "Drama")
            ),
            MetaItem(
                id = "tt1375666",
                type = "movie",
                name = "Inception",
                poster = "https://images.unsplash.com/photo-1506466010722-395aa2bef877?w=600",
                description = "A thief who steals corporate secrets through dream-sharing technology is given the inverse task.",
                releaseInfo = "2010",
                imdbRating = "8.8",
                genres = listOf("Action", "Sci-Fi")
            )
        )
    }

    // Anime collection
    val animePicks = remember {
        listOf(
            MetaItem(
                id = "tt1234567",
                type = "series",
                name = "Attack on Titan",
                poster = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600",
                description = "After his hometown is destroyed, young Eren Jaeger vows to cleanse the earth of the giant humanoid Titans.",
                releaseInfo = "2013-2023",
                imdbRating = "9.1",
                genres = listOf("Animation", "Action", "Fantasy")
            ),
            MetaItem(
                id = "tt2345678",
                type = "series",
                name = "Jujutsu Kaisen",
                poster = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600",
                description = "A boy swallows a cursed talisman - the finger of a demon - and becomes cursed himself.",
                releaseInfo = "2020-",
                imdbRating = "8.6",
                genres = listOf("Animation", "Action")
            ),
            MetaItem(
                id = "tt3456789",
                type = "series",
                name = "Demon Slayer",
                poster = "https://images.unsplash.com/photo-1618042164219-62c820f10723?w=600",
                description = "A youth begins a quest to fight demons and save his transformed sister.",
                releaseInfo = "2019-",
                imdbRating = "8.7",
                genres = listOf("Animation", "Action", "Supernatural")
            )
        )
    }

    val genres = listOf("All", "Trending", "Action", "Sci-Fi", "Drama", "Anime", "Horror", "Comedy", "4K Remux")

    val filteredMovies = remember(movies, selectedGenre) {
        if (selectedGenre == "All" || selectedGenre == "Trending") movies
        else if (selectedGenre == "4K Remux") debrid4kPicks
        else if (selectedGenre == "Anime") animePicks
        else movies.filter { it.genres?.any { g -> g.contains(selectedGenre, ignoreCase = true) } == true }
            .ifEmpty { movies }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FlowvidDeepOled)
            .testTag("flowvid_home_screen")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // 1. Top App Bar / Branding
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = FlowvidNeonCyan.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FlowvidNeonCyan),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = FlowvidNeonCyan, modifier = Modifier.size(16.dp))
                            }
                        }

                        Text(
                            text = "FLOWVID",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = Color.White
                        )

                        Surface(
                            color = FlowvidElectricPurple,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "PRO",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // TV Mode Switcher
                        Surface(
                            onClick = onSwitchToTvMode,
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF1E212D),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FlowvidGlassBorder),
                            modifier = Modifier.testTag("flowvid_tv_mode_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(Icons.Default.Tv, contentDescription = null, tint = FlowvidNeonCyan, modifier = Modifier.size(14.dp))
                                Text("10-Foot TV", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF181A25))
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // 2. Hero Showcase Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(340.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, FlowvidGlassBorder, RoundedCornerShape(20.dp))
                        .testTag("flowvid_hero_banner")
                ) {
                    AsyncImage(
                        model = heroItem.background ?: heroItem.poster,
                        contentDescription = heroItem.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Atmospheric gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x33000000),
                                        Color(0xAA0A0A0C),
                                        Color(0xFC0A0A0C)
                                    )
                                )
                            )
                    )

                    // Hero Content
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Badges Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = FlowvidNeonCyan,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "FEATURED 4K",
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }

                            heroItem.imdbRating?.let { rating ->
                                Surface(
                                    color = Color(0x99000000),
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = FlowvidGold, modifier = Modifier.size(13.dp))
                                        Text(rating, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Surface(
                                color = Color(0x881E212E),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Dolby Atmos",
                                    color = Color(0xFFC4CADF),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Title
                        Text(
                            text = heroItem.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Description
                        Text(
                            text = heroItem.description ?: "Experience ultra high-definition cinematic streaming powered by high-speed Debrid caching.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFADB3CB),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Action Buttons
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    streamPickerMeta = heroItem
                                    isStreamPickerVisible = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FlowvidNeonCyan),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("flowvid_hero_play_btn")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Stream 4K", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                            }

                            OutlinedButton(
                                onClick = { onItemClick(heroItem.type, heroItem.id) },
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B4058)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Icon(Icons.Outlined.Info, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Details")
                            }

                            IconButton(
                                onClick = {
                                    val trailerQuery = Uri.encode("${heroItem.name} official trailer")
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$trailerQuery"))
                                    context.startActivity(intent)
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF1E212E))
                                    .border(1.dp, Color(0xFF33384D), RoundedCornerShape(12.dp))
                            ) {
                                Icon(Icons.Default.Movie, contentDescription = "Trailer", tint = FlowvidHotPink, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // 3. Genre Pills Row
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(genres) { genre ->
                            val isSelected = selectedGenre == genre
                            Surface(
                                onClick = { selectedGenre = genre },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) FlowvidNeonCyan else Color(0xFF141622),
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF282C40)),
                                modifier = Modifier.testTag("genre_pill_$genre")
                            ) {
                                Text(
                                    text = genre,
                                    color = if (isSelected) Color.Black else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 4. Trending Now Carousel
            item {
                FlowvidSectionRow(
                    title = "Trending Now",
                    tag = "HOT",
                    items = if (trending.isNotEmpty()) trending else filteredMovies,
                    onItemClick = onItemClick,
                    onInstantStream = { meta ->
                        streamPickerMeta = meta
                        isStreamPickerVisible = true
                    }
                )
            }

            // 5. 4K Debrid Ultra HD Shelf
            item {
                FlowvidSectionRow(
                    title = "4K Debrid Remux & HDR",
                    tag = "2160P",
                    tagColor = FlowvidElectricPurple,
                    items = debrid4kPicks,
                    onItemClick = onItemClick,
                    onInstantStream = { meta ->
                        streamPickerMeta = meta
                        isStreamPickerVisible = true
                    }
                )
            }

            // 6. Popular Movies
            item {
                FlowvidSectionRow(
                    title = "Top Blockbuster Movies",
                    tag = "CINEMA",
                    items = filteredMovies,
                    onItemClick = onItemClick,
                    onInstantStream = { meta ->
                        streamPickerMeta = meta
                        isStreamPickerVisible = true
                    }
                )
            }

            // 7. Popular Series
            item {
                FlowvidSectionRow(
                    title = "Binge-Worthy TV Series",
                    tag = "SERIES",
                    items = if (series.isNotEmpty()) series else animePicks,
                    onItemClick = onItemClick,
                    onInstantStream = { meta ->
                        streamPickerMeta = meta
                        isStreamPickerVisible = true
                    }
                )
            }

            // 8. Anime & Animation
            item {
                FlowvidSectionRow(
                    title = "Anime & Animation",
                    tag = "ANIME",
                    tagColor = FlowvidHotPink,
                    items = animePicks,
                    onItemClick = onItemClick,
                    onInstantStream = { meta ->
                        streamPickerMeta = meta
                        isStreamPickerVisible = true
                    }
                )
            }
        }

        // Stream Picker Modal
        if (isStreamPickerVisible && streamPickerMeta != null) {
            FlowvidStreamPickerModal(
                meta = streamPickerMeta,
                onDismiss = {
                    isStreamPickerVisible = false
                    streamPickerMeta = null
                },
                onStreamSelected = { streamUrl, streamTitle ->
                    onPlayDirect(streamUrl, streamTitle)
                }
            )
        }
    }
}

@Composable
fun FlowvidSectionRow(
    title: String,
    tag: String? = null,
    tagColor: Color = FlowvidNeonCyan,
    items: List<MetaItem>,
    onItemClick: (type: String, id: String) -> Unit,
    onInstantStream: (MetaItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                tag?.let {
                    Surface(
                        color = tagColor.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, tagColor.copy(alpha = 0.6f))
                    ) {
                        Text(
                            text = it,
                            color = tagColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "View All",
                color = FlowvidNeonCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* expand */ }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                FlowvidCard(
                    meta = item,
                    onClick = { onItemClick(item.type, item.id) },
                    onStreamClick = { onInstantStream(item) }
                )
            }
        }
    }
}

@Composable
fun FlowvidCard(
    meta: MetaItem,
    onClick: () -> Unit,
    onStreamClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = FlowvidGlassSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF242738)),
        modifier = Modifier
            .width(140.dp)
            .height(245.dp)
            .testTag("flowvid_card_${meta.id}")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = meta.poster ?: meta.background,
                    contentDescription = meta.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Subtle top and bottom gradients
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x77000000),
                                    Color.Transparent,
                                    Color(0xDD141419)
                                )
                            )
                        )
                )

                // Top Rating Badge
                meta.imdbRating?.let { rating ->
                    Surface(
                        color = Color(0xCC0A0A0C),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = FlowvidGold, modifier = Modifier.size(11.dp))
                            Text(rating, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Quick Stream Play Icon in bottom corner
                IconButton(
                    onClick = onStreamClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(FlowvidNeonCyan)
                        .testTag("quick_stream_btn_${meta.id}")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Stream", tint = Color.Black, modifier = Modifier.size(18.dp))
                }
            }

            // Info under image
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = meta.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meta.releaseInfo ?: meta.type.replaceFirstChar { it.uppercase() },
                        color = Color(0xFF888EA8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "4K HDR",
                        color = FlowvidNeonCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
