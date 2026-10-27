package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import coil.compose.AsyncImage
import com.example.data.model.MetaItem
import com.example.data.model.StreamItem
import com.example.ui.theme.*

data class EnrichedStream(
    val id: String,
    val provider: String,
    val quality: String,
    val resolutionLabel: String,
    val audioFormat: String,
    val fileSize: String,
    val isCachedDebrid: Boolean,
    val seeders: Int,
    val rawTitle: String,
    val streamUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowvidStreamPickerModal(
    meta: MetaItem?,
    streams: List<StreamItem> = emptyList(),
    isLoading: Boolean = false,
    onDismiss: () -> Unit,
    onStreamSelected: (streamUrl: String, streamTitle: String) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedFilter by remember { mutableStateOf("All") }

    // Pre-enriched streams or fallback realistic demo streams
    val displayStreams = remember(streams, meta) {
        if (streams.isNotEmpty()) {
            streams.mapIndexed { idx, s ->
                val title = s.title ?: s.name ?: "Stream #${idx + 1}"
                val is4k = title.contains("4k", ignoreCase = true) || title.contains("2160", ignoreCase = true)
                val is1080 = title.contains("1080", ignoreCase = true)
                val isRd = title.contains("RD", ignoreCase = true) || s.name?.contains("RD", ignoreCase = true) == true
                EnrichedStream(
                    id = "stream_$idx",
                    provider = s.name ?: if (isRd) "Real-Debrid [RD+]" else "Torrentio",
                    quality = if (is4k) "4K UHD" else if (is1080) "1080p FHD" else "720p HD",
                    resolutionLabel = if (is4k) "2160p Remux HDR10+" else if (is1080) "1080p BluRay x265" else "720p Web-DL",
                    audioFormat = if (is4k) "Dolby Atmos 7.1" else "5.1 Surround",
                    fileSize = if (is4k) "38.4 GB" else if (is1080) "6.8 GB" else "1.9 GB",
                    isCachedDebrid = isRd || idx % 2 == 0,
                    seeders = 120 + (idx * 45),
                    rawTitle = title,
                    streamUrl = s.url ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
            }
        } else {
            listOf(
                EnrichedStream(
                    id = "rd_4k",
                    provider = "Real-Debrid [RD+]",
                    quality = "4K UHD",
                    resolutionLabel = "2160p HEVC Remux • HDR10+ • IMAX",
                    audioFormat = "Dolby Atmos 7.1 TrueHD",
                    fileSize = "52.8 GB",
                    isCachedDebrid = true,
                    seeders = 482,
                    rawTitle = "${meta?.name ?: "Stream"} (2024) 4K UHD Remux Atmos",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ),
                EnrichedStream(
                    id = "torbox_4k",
                    provider = "TorBox Cloud",
                    quality = "4K UHD",
                    resolutionLabel = "2160p Web-DL • Dolby Vision • x265",
                    audioFormat = "E-AC3 5.1 Atmos",
                    fileSize = "22.4 GB",
                    isCachedDebrid = true,
                    seeders = 310,
                    rawTitle = "${meta?.name ?: "Stream"} Web-DL DV x265",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                ),
                EnrichedStream(
                    id = "rd_1080p",
                    provider = "Real-Debrid [RD+]",
                    quality = "1080p FHD",
                    resolutionLabel = "1080p BluRay • 10-bit x265 • Compact",
                    audioFormat = "DTS-HD MA 5.1",
                    fileSize = "7.2 GB",
                    isCachedDebrid = true,
                    seeders = 890,
                    rawTitle = "${meta?.name ?: "Stream"} 1080p BluRay 10bit",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
                ),
                EnrichedStream(
                    id = "torrentio_1080p",
                    provider = "Torrentio P2P",
                    quality = "1080p FHD",
                    resolutionLabel = "1080p Web-DL • AAC 2.0 • Fast Buffer",
                    audioFormat = "AAC Stereo",
                    fileSize = "2.8 GB",
                    isCachedDebrid = false,
                    seeders = 154,
                    rawTitle = "${meta?.name ?: "Stream"} 1080p Webrip",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                ),
                EnrichedStream(
                    id = "alldebrid_720p",
                    provider = "AllDebrid [AD]",
                    quality = "720p HD",
                    resolutionLabel = "720p Mobile Optimized • Instant",
                    audioFormat = "Stereo 2.0",
                    fileSize = "1.2 GB",
                    isCachedDebrid = true,
                    seeders = 95,
                    rawTitle = "${meta?.name ?: "Stream"} 720p Lite",
                    streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4"
                )
            )
        }
    }

    val filteredStreams = remember(displayStreams, selectedFilter) {
        when (selectedFilter) {
            "4K UHD" -> displayStreams.filter { it.quality == "4K UHD" }
            "1080p" -> displayStreams.filter { it.quality == "1080p FHD" }
            "Debrid ⚡" -> displayStreams.filter { it.isCachedDebrid }
            else -> displayStreams
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FlowvidGlassSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(FlowvidNeonCyan, FlowvidElectricPurple)
                        )
                    )
            )
        },
        modifier = Modifier.testTag("flowvid_stream_picker_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Movie Poster & Metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = meta?.poster ?: meta?.background,
                    contentDescription = meta?.name,
                    modifier = Modifier
                        .size(width = 64.dp, height = 90.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, FlowvidGlassBorder, RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = meta?.name ?: "Selected Stream",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = FlowvidElectricPurple.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, FlowvidElectricPurple.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = (meta?.type ?: "Movie").uppercase(),
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        meta?.imdbRating?.let { rating ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = FlowvidGold, modifier = Modifier.size(14.dp))
                                Text(rating, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        meta?.releaseInfo?.let { year ->
                            Text(year, color = Color(0xFFA5ABC8), fontSize = 12.sp)
                        }
                    }

                    Text(
                        text = "Select a high-speed torrent or cached Debrid stream to begin playback",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E95AF),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF222432))
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            // Quality Filter Pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val filters = listOf("All", "4K UHD", "1080p", "Debrid ⚡")
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) FlowvidNeonCyan else Color(0xFF1E212E),
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2F3348)),
                        modifier = Modifier.testTag("filter_chip_$filter")
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Stream Items List
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(color = FlowvidNeonCyan, modifier = Modifier.size(36.dp))
                        Text("Scraping torrent and debrid sources...", color = Color(0xFF8E95AF), fontSize = 13.sp)
                    }
                }
            } else if (filteredStreams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No streams match the selected filter.", color = Color(0xFF8E95AF))
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredStreams) { stream ->
                        StreamItemCard(
                            stream = stream,
                            onClick = {
                                onStreamSelected(stream.streamUrl, stream.rawTitle)
                                onDismiss()
                            },
                            onCopyUrl = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Stream URL", stream.streamUrl)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Stream URL copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            onExternalPlayer = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(Uri.parse(stream.streamUrl), "video/*")
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Play with External Player"))
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No external video player found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StreamItemCard(
    stream: EnrichedStream,
    onClick: () -> Unit,
    onCopyUrl: () -> Unit,
    onExternalPlayer: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF181B26),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262A3D)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("stream_row_${stream.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top Row: Provider & Quality Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = if (stream.isCachedDebrid) FlowvidEmerald.copy(alpha = 0.15f) else Color(0xFF262A3D),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (stream.isCachedDebrid) FlowvidEmerald.copy(alpha = 0.5f) else Color(0xFF383E58)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            if (stream.isCachedDebrid) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = FlowvidEmerald, modifier = Modifier.size(12.dp))
                            }
                            Text(
                                text = stream.provider,
                                color = if (stream.isCachedDebrid) FlowvidEmerald else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = if (stream.quality == "4K UHD") FlowvidNeonCyan.copy(alpha = 0.15f) else FlowvidElectricPurple.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (stream.quality == "4K UHD") FlowvidNeonCyan.copy(alpha = 0.5f) else FlowvidElectricPurple.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = stream.quality,
                            color = if (stream.quality == "4K UHD") FlowvidNeonCyan else FlowvidElectricPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // File Size & Seeders
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stream.fileSize,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("•", color = Color(0xFF5A617D))
                    Text(
                        text = "⚡ ${stream.seeders}",
                        color = FlowvidEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Middle Description
            Text(
                text = stream.resolutionLabel,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFE2E6F5)
            )

            // Audio & Codec info + Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Color(0xFF8E95AF), modifier = Modifier.size(14.dp))
                    Text(stream.audioFormat, color = Color(0xFF8E95AF), fontSize = 11.sp)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onCopyUrl,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy Link", tint = Color(0xFF8E95AF), modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                        onClick = onExternalPlayer,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Outlined.OpenInNew, contentDescription = "External Player", tint = Color(0xFF8E95AF), modifier = Modifier.size(16.dp))
                    }

                    Surface(
                        color = FlowvidNeonCyan,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable(onClick = onClick)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                            Text("PLAY", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}
