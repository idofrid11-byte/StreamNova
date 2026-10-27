package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.AddonEntity
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun AddonsScreen(viewModel: StreamNovaViewModel) {
    val addons by viewModel.installedAddons.collectAsStateWithLifecycle()
    var addonUrlInput by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isInstalling by remember { mutableStateOf(false) }

    val featuredCommunityAddons = listOf(
        CommunityAddon(
            name = "CyberFlix Catalog",
            url = "https://cyberflix.kamyroll.net/manifest.json",
            desc = "Catalogs from Netflix, Disney+, Apple TV+, Prime Video, HBO Max & Hulu."
        ),
        CommunityAddon(
            name = "Anime Kitsu",
            url = "https://anime-kitsu.strem.fun/manifest.json",
            desc = "Anime catalog synced with Kitsu.io anime database."
        ),
        CommunityAddon(
            name = "OpenSubtitles v3",
            url = "https://opensubtitles-v3.strem.io/manifest.json",
            desc = "Official OpenSubtitles v3 scraper for multi-language SRT subtitles."
        ),
        CommunityAddon(
            name = "Torrentio Streams",
            url = "https://torrentio.strem.fun/manifest.json",
            desc = "Scrapes streams from YTS, EZTV, RARBG, 1337x, and Debrid providers."
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("addons_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Addon Manager",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Extend StreamNova with Stremio Addon Protocol endpoints for catalogs, streams, and subtitles.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B92B2)
                )
            }
        }

        // Install Addon by URL
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131627)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262A44))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Install Custom Addon",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    OutlinedTextField(
                        value = addonUrlInput,
                        onValueChange = { addonUrlInput = it },
                        placeholder = { Text("https://example-addon.strem.fun/manifest.json", color = Color(0xFF6B7294), fontSize = 13.sp) },
                        label = { Text("Addon Manifest URL") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("addon_url_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NovaPrimary,
                            unfocusedBorderColor = Color(0xFF2C3252),
                            focusedContainerColor = Color(0xFF171A30),
                            unfocusedContainerColor = Color(0xFF171A30),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (statusMessage != null) {
                            Text(
                                text = statusMessage ?: "",
                                color = if (statusMessage?.contains("Success", ignoreCase = true) == true) NovaAccentCyan else Color(0xFFFF5252),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Button(
                            onClick = {
                                if (addonUrlInput.isNotBlank()) {
                                    isInstalling = true
                                    viewModel.installAddon(addonUrlInput) { success ->
                                        isInstalling = false
                                        if (success) {
                                            statusMessage = "Addon installed successfully!"
                                            addonUrlInput = ""
                                        } else {
                                            statusMessage = "Failed to load manifest. Check URL."
                                        }
                                    }
                                }
                            },
                            enabled = !isInstalling && addonUrlInput.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("install_addon_button")
                        ) {
                            if (isInstalling) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Install", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Featured Community Addons
        item {
            Text(
                text = "Featured Community Addons",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(featuredCommunityAddons) { item ->
            val isAlreadyInstalled = addons.any { it.manifestUrl.contains(item.url.substringBefore("/manifest.json")) }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131627)),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263D))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF8E95BA),
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (isAlreadyInstalled) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF18382B)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Installed",
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text("Installed", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                viewModel.installAddon(item.url) { }
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text("Install", fontSize = 12.sp, color = NovaPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Installed Addons Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Installed Addons (${addons.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        items(addons, key = { it.manifestUrl }) { addon ->
            AddonItemCard(
                addon = addon,
                onToggle = { enabled -> viewModel.toggleAddon(addon.manifestUrl, enabled) },
                onDelete = { viewModel.removeAddon(addon.manifestUrl) }
            )
        }
    }
}

@Composable
fun AddonItemCard(
    addon: AddonEntity,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("addon_card_${addon.name.replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF141728)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262A42))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = null,
                        tint = NovaPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = addon.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Surface(
                        color = Color(0xFF232844),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "v${addon.version}",
                            color = Color(0xFFA1A8CC),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Switch(
                    checked = addon.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = NovaPrimary,
                        uncheckedTrackColor = Color(0xFF2B304C)
                    ),
                    modifier = Modifier.testTag("addon_switch_${addon.name.replace(" ", "_")}")
                )
            }

            if (!addon.description.isNullOrBlank()) {
                Text(
                    text = addon.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B92B6),
                    lineHeight = 16.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = addon.manifestUrl,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF5D6488),
                    fontSize = 10.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                if (addon.isDeletable) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(28.dp)
                            .testTag("delete_addon_${addon.name.replace(" ", "_")}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = Color(0xFFE57373),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

data class CommunityAddon(
    val name: String,
    val url: String,
    val desc: String
)
