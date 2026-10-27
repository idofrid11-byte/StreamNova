package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.AddonEntity
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

data class ManagerCommunityAddon(
    val id: String,
    val name: String,
    val version: String,
    val description: String,
    val manifestUrl: String,
    val category: String,
    val isVerified: Boolean = true
)

@Composable
fun AddonManagerScreen(
    viewModel: StreamNovaViewModel
) {
    val installedAddons by viewModel.installedAddons.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf(0) } // 0: Installed, 1: Community Catalog, 2: Custom URL
    var customManifestUrl by remember { mutableStateOf("") }
    var installStatusMessage by remember { mutableStateOf<String?>(null) }
    var isInstalling by remember { mutableStateOf(false) }

    val communityCatalog = remember {
        listOf(
            ManagerCommunityAddon(
                id = "community.cinemeta",
                name = "AIOMetadata Official",
                version = "3.0.12",
                description = "All-In-One Unified Metadata Catalog for IMDb, TMDB, TVDB & AniList titles.",
                manifestUrl = "https://v3-cinemeta.strem.io/manifest.json",
                category = "Metadata"
            ),
            ManagerCommunityAddon(
                id = "com.stremio.torrentio.addon",
                name = "AIOStreams Multi-Scraper",
                version = "1.0.14",
                description = "Unified stream aggregator across Real-Debrid, TorBox, Premiumize, and P2P caches.",
                manifestUrl = "https://torrentio.strem.fun/manifest.json",
                category = "Streams"
            ),
            ManagerCommunityAddon(
                id = "org.stremio.opensubtitlesv3",
                name = "OpenSubtitles v3",
                version = "1.0.0",
                description = "Official subtitle addon powered by OpenSubtitles with multi-language captions.",
                manifestUrl = "https://opensubtitles-v3.strem.io/manifest.json",
                category = "Subtitles"
            ),
            ManagerCommunityAddon(
                id = "community.watchhub",
                name = "WatchHub Streams",
                version = "1.0.3",
                description = "Provider links for Netflix, Prime, HBO, Disney+, and free open streams.",
                manifestUrl = "https://watchhub.strem.io/manifest.json",
                category = "Official Providers"
            ),
            ManagerCommunityAddon(
                id = "community.animekitsu",
                name = "AnimeKitsu Hub",
                version = "2.1.0",
                description = "Kitsu.io anime database with direct episode mapping, sub/dub options, and fillers.",
                manifestUrl = "https://anime-kitsu.strem.fun/manifest.json",
                category = "Anime"
            ),
            ManagerCommunityAddon(
                id = "community.cyberflix",
                name = "CyberFlix Catalog",
                version = "1.4.2",
                description = "Streaming services rows: Netflix, Apple TV+, HBO Max, Disney+, Hulu, and Paramount+.",
                manifestUrl = "https://cyberflix.elfhosted.com/manifest.json",
                category = "Catalogs"
            ),
            ManagerCommunityAddon(
                id = "community.knightcrawler",
                name = "KnightCrawler Scraper",
                version = "2.0.5",
                description = "Alternative high-performance scraper for cached 4K Remux torrents & debrid links.",
                manifestUrl = "https://knightcrawler.elfhosted.com/manifest.json",
                category = "Streams"
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("addon_manager_screen")
    ) {
        // Top Header
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
                        text = "🧩 Addon Manager",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Expand catalogs, streams, and subtitle providers via Stremio protocol",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }

                Surface(
                    color = NovaPrimary.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${installedAddons.size} Active",
                        color = NovaAccentCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Tab bar
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF0E101D),
                contentColor = Color.White,
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Installed (${installedAddons.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Catalog (${communityCatalog.size})", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Install URL", fontWeight = FontWeight.Bold) }
                )
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Installed Addons
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 36.dp)
                ) {
                    items(installedAddons) { addon ->
                        InstalledAddonCard(
                            addon = addon,
                            onToggle = { isChecked ->
                                viewModel.toggleAddon(addon.manifestUrl, isChecked)
                            },
                            onDelete = {
                                viewModel.removeAddon(addon.manifestUrl)
                            }
                        )
                    }
                }
            }

            1 -> {
                // Community Catalog
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 36.dp)
                ) {
                    items(communityCatalog) { communityAddon ->
                        val isInstalled = installedAddons.any { it.manifestUrl == communityAddon.manifestUrl }
                        CommunityAddonCard(
                            addon = communityAddon,
                            isInstalled = isInstalled,
                            onInstallClick = {
                                viewModel.installAddon(communityAddon.manifestUrl) {
                                    // installed
                                }
                            },
                            onUninstallClick = {
                                viewModel.removeAddon(communityAddon.manifestUrl)
                            }
                        )
                    }
                }
            }

            2 -> {
                // Custom Manifest URL Installer
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Install Stremio Addon from URL",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "Paste any valid Stremio v3 protocol manifest URL ending with /manifest.json (e.g. Torrentio, Comet, MediaFusion, Jackett, etc.).",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF8B92B2)
                            )

                            OutlinedTextField(
                                value = customManifestUrl,
                                onValueChange = {
                                    customManifestUrl = it
                                    installStatusMessage = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_addon_url_input"),
                                label = { Text("Manifest URL") },
                                placeholder = { Text("https://my-addon.example.com/manifest.json") },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NovaPrimary,
                                    unfocusedBorderColor = Color(0xFF22263C),
                                    focusedContainerColor = Color(0xFF090A10),
                                    unfocusedContainerColor = Color(0xFF090A10),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Button(
                                onClick = {
                                    if (customManifestUrl.isBlank()) {
                                        installStatusMessage = "Please enter a valid URL"
                                        return@Button
                                    }
                                    isInstalling = true
                                    installStatusMessage = null
                                    viewModel.installAddon(customManifestUrl.trim()) { success ->
                                        isInstalling = false
                                        if (success) {
                                            installStatusMessage = "✅ Addon successfully installed and registered!"
                                            customManifestUrl = ""
                                        } else {
                                            installStatusMessage = "❌ Failed to fetch manifest. Verify the URL is accessible."
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_install_addon_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !isInstalling
                            ) {
                                if (isInstalling) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Validating Manifest...")
                                } else {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Test & Install Addon", fontWeight = FontWeight.Bold)
                                }
                            }

                            installStatusMessage?.let { msg ->
                                Text(
                                    text = msg,
                                    color = if (msg.startsWith("✅")) Color(0xFF00E676) else Color(0xFFFF5252),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InstalledAddonCard(
    addon: AddonEntity,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("addon_item_${addon.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (addon.isEnabled) NovaPrimary.copy(alpha = 0.2f) else Color(0xFF22263C),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = null,
                        tint = if (addon.isEnabled) NovaAccentCyan else Color(0xFF7E84A6),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = addon.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        color = Color(0xFF222640),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "v${addon.version}",
                            color = NovaRatingGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = addon.description ?: "Community Stremio addon provider",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B92B2),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Switch(
                    checked = addon.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = NovaPrimary,
                        uncheckedThumbColor = Color(0xFF7E84A6),
                        uncheckedTrackColor = Color(0xFF22263C)
                    )
                )

                if (addon.isDeletable) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Uninstall Addon",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommunityAddonCard(
    addon: ManagerCommunityAddon,
    isInstalled: Boolean,
    onInstallClick: () -> Unit,
    onUninstallClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .testTag("community_addon_${addon.id}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = NovaAccentCyan.copy(alpha = 0.15f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CloudQueue,
                        contentDescription = null,
                        tint = NovaAccentCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = addon.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        color = Color(0xFF222640),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = addon.category,
                            color = NovaAccentCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = addon.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B92B2),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isInstalled) {
                OutlinedButton(
                    onClick = onUninstallClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f))
                ) {
                    Text("Installed", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onInstallClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary)
                ) {
                    Text("Install", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
