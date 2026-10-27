package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.SettingsEntity
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun SettingsScreen(viewModel: StreamNovaViewModel) {
    val currentSettings by viewModel.settings.collectAsStateWithLifecycle()
    var debridKey by remember(currentSettings) { mutableStateOf(currentSettings.debridApiKey) }
    var selectedProvider by remember(currentSettings) { mutableStateOf(currentSettings.debridProvider) }
    var cacheSize by remember(currentSettings) { mutableIntStateOf(currentSettings.maxCacheSizeMb) }
    var hwAcceleration by remember(currentSettings) { mutableStateOf(currentSettings.hardwareAcceleration) }
    var autoPlayNext by remember(currentSettings) { mutableStateOf(currentSettings.autoPlayNext) }
    var preferredQuality by remember(currentSettings) { mutableStateOf(currentSettings.preferredQuality) }
    var subtitleSize by remember(currentSettings) { mutableIntStateOf(currentSettings.subtitleSize) }
    var showApiKey by remember { mutableStateOf(false) }
    var saveFeedback by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text(
                text = "Preferences & Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }

        // Debrid Provider & API Key
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.CloudQueue,
                            contentDescription = null,
                            tint = NovaAccentCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Debrid Streaming Service",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "Connect your Real-Debrid or AllDebrid account for lightning-fast, high-speed 4K/1080p cached streams.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )

                    // Provider Options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Real-Debrid", "AllDebrid", "Premiumize").forEach { provider ->
                            FilterChip(
                                selected = selectedProvider == provider,
                                onClick = { selectedProvider = provider },
                                label = { Text(provider, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NovaPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = debridKey,
                        onValueChange = { debridKey = it },
                        label = { Text("$selectedProvider API Token") },
                        placeholder = { Text("Paste your API token here", color = Color(0xFF6B7294)) },
                        visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showApiKey = !showApiKey }) {
                                Icon(
                                    imageVector = if (showApiKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Visibility",
                                    tint = Color(0xFFA0A5C2)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("debrid_api_key_input"),
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
                }
            }
        }

        // Playback & Video Engine
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131627)),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF262A44))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = NovaPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Playback & Video Engine",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Hardware Acceleration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hardware Acceleration",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Hardware video decoding for 4K 60FPS playback with low battery consumption.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF888FA8)
                            )
                        }
                        Switch(
                            checked = hwAcceleration,
                            onCheckedChange = { hwAcceleration = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = NovaPrimary),
                            modifier = Modifier.testTag("hw_accel_switch")
                        )
                    }

                    Divider(color = Color(0xFF22263C))

                    // Auto Play Next
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Auto-play Next Episode",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Automatically play the following episode in series queues.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF888FA8)
                            )
                        }
                        Switch(
                            checked = autoPlayNext,
                            onCheckedChange = { autoPlayNext = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = NovaPrimary),
                            modifier = Modifier.testTag("auto_play_next_switch")
                        )
                    }

                    Divider(color = Color(0xFF22263C))

                    // Preferred Quality
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Preferred Streaming Quality",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("4K Remux", "1080p FHD", "720p HD").forEach { q ->
                                FilterChip(
                                    selected = preferredQuality == q,
                                    onClick = { preferredQuality = q },
                                    label = { Text(q, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NovaPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Cache & Storage
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
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = Color(0xFFFFB74D),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Streaming Cache Limit",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "ExoPlayer buffer size: ${cacheSize} MB. Larger caches reduce buffering on slow connections.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888FA8)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(512, 1024, 2048, 4096).forEach { size ->
                            FilterChip(
                                selected = cacheSize == size,
                                onClick = { cacheSize = size },
                                label = { Text("${size}MB", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NovaPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Save Button
        item {
            Button(
                onClick = {
                    viewModel.saveSettings(
                        currentSettings.copy(
                            debridApiKey = debridKey,
                            debridProvider = selectedProvider,
                            maxCacheSizeMb = cacheSize,
                            hardwareAcceleration = hwAcceleration,
                            autoPlayNext = autoPlayNext,
                            preferredQuality = preferredQuality,
                            subtitleSize = subtitleSize
                        )
                    )
                    saveFeedback = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_settings_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (saveFeedback) "Preferences Saved!" else "Save Preferences",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        // App Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F111E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "StreamNova Mobile v1.0.0",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Compatible with official Stremio Addon Protocol v3 specifications. Powered by ExoPlayer Media3.",
                        color = Color(0xFF6B7293),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
