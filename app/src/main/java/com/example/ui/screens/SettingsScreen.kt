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
fun SettingsScreen(
    viewModel: StreamNovaViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val currentSettings by viewModel.settings.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var selectedCategory by remember { mutableStateOf("General") }
    val categories = listOf(
        "General", "Account", "Library", "Addons",
        "Playback", "Audio", "Subtitles", "Streaming", "Storage", "Accessibility"
    )

    // Local mutable copies of settings for live control
    var settingsState by remember(currentSettings) { mutableStateOf(currentSettings) }
    var saveFeedback by remember { mutableStateOf(false) }
    var showApiKey by remember { mutableStateOf(false) }

    fun updateSettings(modify: (SettingsEntity) -> SettingsEntity) {
        val updated = modify(settingsState)
        settingsState = updated
        viewModel.saveSettings(updated)
        saveFeedback = true
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Settings Center",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Configure playback, streaming server, subtitles, and TV optimization.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF888FA8)
                )
            }
        }

        // Category Tabs
        item {
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                containerColor = Color(0xFF131526),
                contentColor = Color.White,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF252940), RoundedCornerShape(12.dp))
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        text = {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NovaPrimary else Color(0xFF9EA4C4)
                            )
                        }
                    )
                }
            }
        }

        // Selected Category Content
        when (selectedCategory) {
            "General" -> {
                item {
                    SettingsSectionCard(title = "General & Startup", icon = Icons.Default.Settings) {
                        // App Language
                        Text("App Language: ${settingsState.appLanguage}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("English", "Spanish", "French", "German").forEach { lang ->
                                FilterChip(
                                    selected = settingsState.appLanguage == lang,
                                    onClick = { updateSettings { it.copy(appLanguage = lang) } },
                                    label = { Text(lang, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        // Startup Screen
                        Text("Startup Screen: ${settingsState.startupScreen}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Discover / Home", "Search", "My Library").forEach { screen ->
                                FilterChip(
                                    selected = settingsState.startupScreen == screen,
                                    onClick = { updateSettings { it.copy(startupScreen = screen) } },
                                    label = { Text(screen, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Launch on System Boot (Android TV)",
                            subtitle = "Automatically open StreamNova when TV device turns on",
                            checked = settingsState.launchOnSystemBoot,
                            onCheckedChange = { chk -> updateSettings { it.copy(launchOnSystemBoot = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Background Torrent Seeding",
                            subtitle = "Keep sharing completed blocks when in background",
                            checked = settingsState.backgroundSeeding,
                            onCheckedChange = { chk -> updateSettings { it.copy(backgroundSeeding = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Anonymous App Analytics",
                            subtitle = "Help improve catalog scraping and player compatibility",
                            checked = settingsState.analyticsEnabled,
                            onCheckedChange = { chk -> updateSettings { it.copy(analyticsEnabled = chk) } }
                        )
                    }
                }
            }

            "Account" -> {
                item {
                    SettingsSectionCard(title = "Account & Profiles", icon = Icons.Default.Person) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = currentUser?.displayName ?: "StreamNova Member",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = currentUser?.email ?: "user@streamnova.tv",
                                    color = Color(0xFF888FA8),
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.logout()
                                    onLogoutClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Active Profile: ${settingsState.activeProfile}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Default User", "Kids", "Family", "Cinema Room").forEach { prof ->
                                FilterChip(
                                    selected = settingsState.activeProfile == prof,
                                    onClick = { updateSettings { it.copy(activeProfile = prof) } },
                                    label = { Text(prof, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        // Debrid Configuration
                        Text("Debrid Fast-Streaming Provider", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Real-Debrid", "AllDebrid", "Premiumize").forEach { prov ->
                                FilterChip(
                                    selected = settingsState.debridProvider == prov,
                                    onClick = { updateSettings { it.copy(debridProvider = prov) } },
                                    label = { Text(prov, fontSize = 11.sp) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = settingsState.debridApiKey,
                            onValueChange = { key -> updateSettings { it.copy(debridApiKey = key) } },
                            label = { Text("${settingsState.debridProvider} API Key") },
                            placeholder = { Text("Paste API key", color = Color(0xFF6B7294)) },
                            visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showApiKey = !showApiKey }) {
                                    Icon(
                                        imageVector = if (showApiKey) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Color(0xFFA0A5C2)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            "Library" -> {
                item {
                    SettingsSectionCard(title = "Library & Cloud Sync", icon = Icons.Default.Bookmark) {
                        SettingsSwitchRow(
                            title = "Auto-remove finished items",
                            subtitle = "Automatically clean up items watched past 95%",
                            checked = settingsState.autoRemoveFinished,
                            onCheckedChange = { chk -> updateSettings { it.copy(autoRemoveFinished = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        Text("Cloud Sync Frequency: ${settingsState.cloudSyncInterval}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Hourly", "Daily", "Weekly", "Manual").forEach { interval ->
                                FilterChip(
                                    selected = settingsState.cloudSyncInterval == interval,
                                    onClick = { updateSettings { it.copy(cloudSyncInterval = interval) } },
                                    label = { Text(interval, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Default Sort Order: ${settingsState.defaultSortOrder}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Title", "Release Date", "IMDb Rating", "Recently Added").forEach { sort ->
                                FilterChip(
                                    selected = settingsState.defaultSortOrder == sort,
                                    onClick = { updateSettings { it.copy(defaultSortOrder = sort) } },
                                    label = { Text(sort, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Poster View Style: ${settingsState.posterViewStyle}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Grid Cards", "Compact List", "Wide Banners").forEach { style ->
                                FilterChip(
                                    selected = settingsState.posterViewStyle == style,
                                    onClick = { updateSettings { it.copy(posterViewStyle = style) } },
                                    label = { Text(style, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            "Addons" -> {
                item {
                    SettingsSectionCard(title = "Addon Management", icon = Icons.Default.Extension) {
                        SettingsSwitchRow(
                            title = "Automatic Addon Updates",
                            subtitle = "Keep installed addon manifests refreshed automatically",
                            checked = settingsState.autoUpdateAddons,
                            onCheckedChange = { chk -> updateSettings { it.copy(autoUpdateAddons = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Verify Addon SSL Certificates",
                            subtitle = "Enforce HTTPS validation for external community endpoints",
                            checked = settingsState.verifyAddonSsl,
                            onCheckedChange = { chk -> updateSettings { it.copy(verifyAddonSsl = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Allow Community Addons",
                            subtitle = "Enable loading unofficial Stremio community repository manifests",
                            checked = settingsState.allowCommunityAddons,
                            onCheckedChange = { chk -> updateSettings { it.copy(allowCommunityAddons = chk) } }
                        )
                    }
                }
            }

            "Playback" -> {
                item {
                    SettingsSectionCard(title = "Playback & Video Decoding", icon = Icons.Default.PlayCircle) {
                        Text("Default Decoder: ${settingsState.defaultDecoder}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Hardware (HW+)", "Software (SW)", "Auto").forEach { dec ->
                                FilterChip(
                                    selected = settingsState.defaultDecoder == dec,
                                    onClick = { updateSettings { it.copy(defaultDecoder = dec) } },
                                    label = { Text(dec, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "HDR Tone Mapping",
                            subtitle = "Map HDR10/Dolby Vision content accurately onto SDR displays",
                            checked = settingsState.hdrToneMapping,
                            onCheckedChange = { chk -> updateSettings { it.copy(hdrToneMapping = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Autoplay Next Episode",
                            subtitle = "Automatically queue and launch the next TV episode",
                            checked = settingsState.autoplayNextEpisode,
                            onCheckedChange = { chk -> updateSettings { it.copy(autoplayNextEpisode = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Night Mode Color Filter",
                            subtitle = "Reduce blue light emission for late-night viewing",
                            checked = settingsState.nightModeFilter,
                            onCheckedChange = { chk -> updateSettings { it.copy(nightModeFilter = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        Text("Double-Tap Seek: ${settingsState.doubleTapSeekSeconds} Seconds", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(5, 10, 15, 30).forEach { sec ->
                                FilterChip(
                                    selected = settingsState.doubleTapSeekSeconds == sec,
                                    onClick = { updateSettings { it.copy(doubleTapSeekSeconds = sec) } },
                                    label = { Text("${sec}s", fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Stats for Nerds (Video Diagnostic Overlay)",
                            subtitle = "Display FPS, dropped frames, decoder, and bitrate stats",
                            checked = settingsState.statsForNerds,
                            onCheckedChange = { chk -> updateSettings { it.copy(statsForNerds = chk) } }
                        )
                    }
                }
            }

            "Audio" -> {
                item {
                    SettingsSectionCard(title = "Audio & Passthrough", icon = Icons.Default.VolumeUp) {
                        Text("Preferred Audio Language: ${settingsState.audioLanguage}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("English", "Spanish", "French", "German", "Japanese").forEach { lang ->
                                FilterChip(
                                    selected = settingsState.audioLanguage == lang,
                                    onClick = { updateSettings { it.copy(audioLanguage = lang) } },
                                    label = { Text(lang, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Audio Output Mode: ${settingsState.audioOutputMode}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Stereo", "Dolby 5.1", "Atmos 7.1", "Auto Passthrough").forEach { mode ->
                                FilterChip(
                                    selected = settingsState.audioOutputMode == mode,
                                    onClick = { updateSettings { it.copy(audioOutputMode = mode) } },
                                    label = { Text(mode, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Dolby Digital & DTS Passthrough",
                            subtitle = "Bitstream multichannel audio to optical or HDMI ARC receivers",
                            checked = settingsState.passthroughDolbyDts,
                            onCheckedChange = { chk -> updateSettings { it.copy(passthroughDolbyDts = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        Text("Volume Boost Level: ${settingsState.volumeBoostLevel}%", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Slider(
                            value = settingsState.volumeBoostLevel.toFloat(),
                            onValueChange = { lvl -> updateSettings { it.copy(volumeBoostLevel = lvl.toInt()) } },
                            valueRange = 100f..200f
                        )
                    }
                }
            }

            "Subtitles" -> {
                item {
                    SettingsSectionCard(title = "Subtitles & OpenSubtitles", icon = Icons.Default.Subtitles) {
                        Text("Auto-load Subtitles: ${settingsState.autoLoadSubtitles}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("For Foreign Audio Only", "Always On", "Off").forEach { mode ->
                                FilterChip(
                                    selected = settingsState.autoLoadSubtitles == mode,
                                    onClick = { updateSettings { it.copy(autoLoadSubtitles = mode) } },
                                    label = { Text(mode, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Primary Subtitle Language: ${settingsState.primarySubtitleLang}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("English", "Spanish", "French", "German", "Portuguese").forEach { lang ->
                                FilterChip(
                                    selected = settingsState.primarySubtitleLang == lang,
                                    onClick = { updateSettings { it.copy(primarySubtitleLang = lang) } },
                                    label = { Text(lang, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Subtitle Font Size: ${settingsState.subtitleSize} sp", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Slider(
                            value = settingsState.subtitleSize.toFloat(),
                            onValueChange = { sz -> updateSettings { it.copy(subtitleSize = sz.toInt()) } },
                            valueRange = 12f..36f
                        )

                        Divider(color = Color(0xFF22263C))

                        Text("Subtitle Text Color: ${settingsState.subtitleTextColor}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("White", "Yellow", "Cyan", "Green").forEach { col ->
                                FilterChip(
                                    selected = settingsState.subtitleTextColor == col,
                                    onClick = { updateSettings { it.copy(subtitleTextColor = col) } },
                                    label = { Text(col, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Subtitle Background: ${settingsState.subtitleBgColor} (${settingsState.subtitleBgOpacity}% Opacity)", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Black", "Semi-Transparent", "None").forEach { bg ->
                                FilterChip(
                                    selected = settingsState.subtitleBgColor == bg,
                                    onClick = { updateSettings { it.copy(subtitleBgColor = bg) } },
                                    label = { Text(bg, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }
            }

            "Streaming" -> {
                item {
                    SettingsSectionCard(title = "Streaming Server & Torrent P2P", icon = Icons.Default.CloudQueue) {
                        Text("Streaming Server Port: ${settingsState.streamingServerPort}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("Pre-buffer Cache Size: ${settingsState.prebufferSizeMb} MB", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(25, 50, 100, 200).forEach { mb ->
                                FilterChip(
                                    selected = settingsState.prebufferSizeMb == mb,
                                    onClick = { updateSettings { it.copy(prebufferSizeMb = mb) } },
                                    label = { Text("${mb}MB", fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Torrent Profile: ${settingsState.torrentProfile}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Default / Fast", "Ultra Seeder", "Low Memory").forEach { prof ->
                                FilterChip(
                                    selected = settingsState.torrentProfile == prof,
                                    onClick = { updateSettings { it.copy(torrentProfile = prof) } },
                                    label = { Text(prof, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Torrent Connection Timeout: ${settingsState.torrentTimeoutSec} sec", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Slider(
                            value = settingsState.torrentTimeoutSec.toFloat(),
                            onValueChange = { t -> updateSettings { it.copy(torrentTimeoutSec = t.toInt()) } },
                            valueRange = 10f..60f
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "UPnP / Port Forwarding",
                            subtitle = "Automatic router port mapping for direct stream peering",
                            checked = settingsState.upnpPortForwarding,
                            onCheckedChange = { chk -> updateSettings { it.copy(upnpPortForwarding = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "Distributed Hash Table (DHT)",
                            subtitle = "Trackerless peer discovery for public magnet links",
                            checked = settingsState.dhtEnabled,
                            onCheckedChange = { chk -> updateSettings { it.copy(dhtEnabled = chk) } }
                        )
                    }
                }
            }

            "Storage" -> {
                item {
                    SettingsSectionCard(title = "Storage & Cache Management", icon = Icons.Default.Storage) {
                        Text("Cache Location: ${settingsState.cacheStorageLocation}", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Internal Storage", "External SD Card").forEach { loc ->
                                FilterChip(
                                    selected = settingsState.cacheStorageLocation == loc,
                                    onClick = { updateSettings { it.copy(cacheStorageLocation = loc) } },
                                    label = { Text(loc, fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Text("Streaming Cache Size: ${settingsState.streamingCacheSizeMb} MB", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(512, 1024, 2048, 4096).forEach { sz ->
                                FilterChip(
                                    selected = settingsState.streamingCacheSizeMb == sz,
                                    onClick = { updateSettings { it.copy(streamingCacheSizeMb = sz) } },
                                    label = { Text("${sz}MB", fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        Button(
                            onClick = {
                                updateSettings { it.copy(streamingCacheSizeMb = 1024) }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Clear All Streaming Caches & Temp Files", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            "Accessibility" -> {
                item {
                    SettingsSectionCard(title = "Accessibility & Android TV", icon = Icons.Default.Accessibility) {
                        SettingsSwitchRow(
                            title = "High Contrast Text",
                            subtitle = "Enhance font borders and stroke outlines for readability",
                            checked = settingsState.highContrastText,
                            onCheckedChange = { chk -> updateSettings { it.copy(highContrastText = chk) } }
                        )

                        Divider(color = Color(0xFF22263C))

                        Text("TV Focus Scale Multiplier: ${String.format("%.2f", settingsState.tvFocusScaleMultiplier)}x", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(1.05f, 1.10f, 1.15f, 1.25f).forEach { sc ->
                                FilterChip(
                                    selected = settingsState.tvFocusScaleMultiplier == sc,
                                    onClick = { updateSettings { it.copy(tvFocusScaleMultiplier = sc) } },
                                    label = { Text("${sc}x", fontSize = 11.sp) }
                                )
                            }
                        }

                        Divider(color = Color(0xFF22263C))

                        SettingsSwitchRow(
                            title = "TalkBack Enhancements",
                            subtitle = "Provide detailed audio spoken descriptions for movie posters",
                            checked = settingsState.talkBackEnhancements,
                            onCheckedChange = { chk -> updateSettings { it.copy(talkBackEnhancements = chk) } }
                        )
                    }
                }
            }
        }

        // Save confirmation toast
        item {
            if (saveFeedback) {
                Surface(
                    color = Color(0xFF1B382B),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF00E676))
                        Text(
                            text = "Settings successfully synchronized and saved to database.",
                            color = Color(0xFF00E676),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF252940))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NovaPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            content()
        }
    }
}

@Composable
fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF888FA8)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NovaPrimary,
                uncheckedTrackColor = Color(0xFF282C44)
            )
        )
    }
}
