package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold

@Composable
fun WuPlaySettingsScreen() {
    var audioLang by remember { mutableStateOf("English") }
    var subLang by remember { mutableStateOf("English") }
    var autoplay by remember { mutableStateOf(true) }
    var hwAcceleration by remember { mutableStateOf(true) }
    var syncCode by remember { mutableStateOf("WUP-8492") }

    val languages = listOf("English", "Spanish", "French", "German", "Japanese", "Portuguese", "Italian")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .testTag("wuplay_settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        item {
            Text(
                text = "WuPlay Settings",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "Android TV 10-Foot Experience & Web Configurator Sync",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF8B92B2)
            )
        }

        // Web Configurator Sync Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, tint = NovaPrimary)
                        Text(
                            text = "Web Configurator Sync Code: $syncCode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NovaAccentCyan
                        )
                    }

                    Text(
                        text = "Manage your addons, accounts, and catalogs from any phone or PC browser by visiting:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFC5CAE9)
                    )

                    Surface(
                        color = Color(0xFF1B1E34),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "https://wuplay.app/configure  •  Pairing Code: $syncCode",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        // Per-Profile Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Per-Profile Preferences",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Audio language selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Preferred Audio Track: $audioLang", color = Color(0xFF8B92B2), fontSize = 13.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(languages) { lang ->
                                FilterChip(
                                    selected = audioLang == lang,
                                    onClick = { audioLang = lang },
                                    label = { Text(lang) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NovaPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = Color(0xFF1C2035),
                                        labelColor = Color(0xFF8B92B2)
                                    )
                                )
                            }
                        }
                    }

                    // Subtitle language selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Preferred Subtitles: $subLang", color = Color(0xFF8B92B2), fontSize = 13.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(languages) { lang ->
                                FilterChip(
                                    selected = subLang == lang,
                                    onClick = { subLang = lang },
                                    label = { Text(lang) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NovaAccentCyan.copy(alpha = 0.25f),
                                        selectedLabelColor = NovaAccentCyan,
                                        containerColor = Color(0xFF1C2035),
                                        labelColor = Color(0xFF8B92B2)
                                    )
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFF22263C))

                    // Autoplay switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Autoplay First Stream / Next Episode", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("Automatically starts best rated stream", color = Color(0xFF8B92B2), fontSize = 12.sp)
                        }
                        Switch(
                            checked = autoplay,
                            onCheckedChange = { autoplay = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = NovaPrimary)
                        )
                    }

                    // HW Decoder switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Hardware Acceleration (ExoPlayer HW+)", color = Color.White, fontWeight = FontWeight.SemiBold)
                            Text("Low latency 4K 60FPS playback on Android TV", color = Color(0xFF8B92B2), fontSize = 12.sp)
                        }
                        Switch(
                            checked = hwAcceleration,
                            onCheckedChange = { hwAcceleration = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = NovaPrimary)
                        )
                    }
                }
            }
        }
    }
}
