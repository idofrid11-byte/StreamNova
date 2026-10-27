package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.db.SettingsEntity
import com.example.data.model.ApiKeysEntity
import com.example.data.model.CloudSyncConfigEntity
import com.example.data.model.ProfileEntity
import com.example.data.model.ThemeConfigEntity
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.theme.NovaRatingGold
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun SettingsDashboardScreen(
    viewModel: StreamNovaViewModel,
    onLogoutClick: () -> Unit = {},
    onSwitchProfileClick: () -> Unit = {}
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val profiles by viewModel.allProfiles.collectAsStateWithLifecycle()
    val currentProfile by viewModel.currentProfile.collectAsStateWithLifecycle()
    val cloudSyncConfig by viewModel.cloudSyncConfig.collectAsStateWithLifecycle()
    val apiKeys by viewModel.apiKeys.collectAsStateWithLifecycle()
    val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var activeTab by remember { mutableStateOf(0) } // 0: MediaHoard Vault, 1: Cloud Sync, 2: Player & Cache, 3: Theming
    val tabs = listOf("Keys & Debrid", "Cloud Sync", "Player & Decoders", "Profiles & Theme")

    // API Keys local form state
    var tmdbKey by remember(apiKeys) { mutableStateOf(apiKeys.tmdbApiKey) }
    var geminiKey by remember(apiKeys) { mutableStateOf(apiKeys.geminiApiKey) }
    var debridService by remember(apiKeys) { mutableStateOf(apiKeys.debridService) }
    var debridKey by remember(apiKeys) { mutableStateOf(apiKeys.debridApiKey) }
    var rpdbKey by remember(apiKeys) { mutableStateOf(apiKeys.rpdbApiKey) }
    var keysSavedFeedback by remember { mutableStateOf(false) }

    // Supabase local form state
    var supabaseUrl by remember(cloudSyncConfig) { mutableStateOf(cloudSyncConfig.supabaseUrl) }
    var supabaseAnonKey by remember(cloudSyncConfig) { mutableStateOf(cloudSyncConfig.supabaseAnonKey) }
    var syncFeedbackMessage by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }

    // Profile creation dialog
    var showNewProfileDialog by remember { mutableStateOf(false) }
    var newProfileName by remember { mutableStateOf("") }

    if (showNewProfileDialog) {
        AlertDialog(
            onDismissRequest = { showNewProfileDialog = false },
            title = { Text("Create New Profile") },
            text = {
                OutlinedTextField(
                    value = newProfileName,
                    onValueChange = { newProfileName = it },
                    label = { Text("Profile Name") },
                    placeholder = { Text("e.g. Cinema Room, Guest, Kids") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NovaPrimary,
                        unfocusedBorderColor = Color(0xFF22263C)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newProfileName.isNotBlank()) {
                            viewModel.createProfile(newProfileName.trim())
                            newProfileName = ""
                            showNewProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary)
                ) {
                    Text("Create Profile")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewProfileDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
            },
            containerColor = Color(0xFF131526)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("settings_dashboard_screen")
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "⚙️ MediaHoard Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "API Vault, Supabase Sync, Multi-Profile & Playback Controls",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B92B2)
                    )
                }

                Surface(
                    color = Color(0xFF1B1E34),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2A2E4E))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = NovaAccentCyan, modifier = Modifier.size(14.dp))
                        Text(
                            text = currentProfile?.name ?: "Main Hoarder",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Tab row
            ScrollableTabRow(
                selectedTabIndex = activeTab,
                containerColor = Color(0xFF0E101D),
                contentColor = Color.White,
                edgePadding = 0.dp,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTab == index,
                        onClick = { activeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == index) NovaAccentCyan else Color(0xFF8B92B2),
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 36.dp)
        ) {
            when (activeTab) {
                0 -> {
                    // API Keys & Debrid
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.VpnKey, contentDescription = null, tint = NovaRatingGold)
                                    Text(
                                        text = "Debrid Streaming Provider",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Text(
                                    text = "Connect Real-Debrid, Premiumize, or TorBox to convert torrent magnets into instant, high-speed 10Gbps CDN direct streams.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF8B92B2)
                                )

                                // Debrid Provider Selector
                                val debridProviders = listOf("Real-Debrid", "AllDebrid", "Premiumize", "TorBox", "Debrid-Link")
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(debridProviders) { provider ->
                                        val isSel = debridService == provider
                                        FilterChip(
                                            selected = isSel,
                                            onClick = { debridService = provider },
                                            label = { Text(provider) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = NovaPrimary,
                                                selectedLabelColor = Color.White,
                                                containerColor = Color(0xFF1E2238),
                                                labelColor = Color(0xFF8B92B2)
                                            )
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = debridKey,
                                    onValueChange = {
                                        debridKey = it
                                        keysSavedFeedback = false
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("debrid_api_key_input"),
                                    label = { Text("$debridService API Token / Key") },
                                    placeholder = { Text("e.g. XXXXXXXXXXXXXXXXXXXX") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CloudQueue, contentDescription = null, tint = NovaAccentCyan)
                                    Text(
                                        text = "Metadata & AI Provider Keys",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                OutlinedTextField(
                                    value = tmdbKey,
                                    onValueChange = { tmdbKey = it; keysSavedFeedback = false },
                                    modifier = Modifier.fillMaxWidth().testTag("tmdb_key_input"),
                                    label = { Text("TMDB API Key (v3 auth)") },
                                    placeholder = { Text("Optional for enriched movie posters") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )

                                OutlinedTextField(
                                    value = rpdbKey,
                                    onValueChange = { rpdbKey = it; keysSavedFeedback = false },
                                    modifier = Modifier.fillMaxWidth().testTag("rpdb_key_input"),
                                    label = { Text("RPDB Rating Poster Database Key") },
                                    placeholder = { Text("Optional for posters with overlay ratings") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )

                                OutlinedTextField(
                                    value = geminiKey,
                                    onValueChange = { geminiKey = it; keysSavedFeedback = false },
                                    modifier = Modifier.fillMaxWidth().testTag("gemini_key_input"),
                                    label = { Text("Google Gemini AI API Key") },
                                    placeholder = { Text("For intelligent recommendations & synopsis") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )

                                Button(
                                    onClick = {
                                        viewModel.saveApiKeys(
                                            apiKeys.copy(
                                                tmdbApiKey = tmdbKey.trim(),
                                                geminiApiKey = geminiKey.trim(),
                                                debridService = debridService,
                                                debridApiKey = debridKey.trim(),
                                                rpdbApiKey = rpdbKey.trim()
                                            )
                                        )
                                        keysSavedFeedback = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("save_api_keys_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save API Keys Vault", fontWeight = FontWeight.Bold)
                                }

                                if (keysSavedFeedback) {
                                    Text(
                                        text = "✅ API keys securely stored in encrypted Room database!",
                                        color = Color(0xFF00E676),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                1 -> {
                    // Supabase Cloud Sync
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, tint = NovaAccentCyan)
                                    Text(
                                        text = "Supabase Cloud Database Sync",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }

                                Text(
                                    text = "Synchronize watch progress, custom profiles, bookmarks, and installed addons across all your Android TVs, phones, tablets, and desktop devices.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF8B92B2)
                                )

                                OutlinedTextField(
                                    value = supabaseUrl,
                                    onValueChange = { supabaseUrl = it; syncFeedbackMessage = null },
                                    modifier = Modifier.fillMaxWidth().testTag("supabase_url_input"),
                                    label = { Text("Supabase Project URL") },
                                    placeholder = { Text("https://your-project.supabase.co") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )

                                OutlinedTextField(
                                    value = supabaseAnonKey,
                                    onValueChange = { supabaseAnonKey = it; syncFeedbackMessage = null },
                                    modifier = Modifier.fillMaxWidth().testTag("supabase_anon_key_input"),
                                    label = { Text("Supabase Anon / Public Key") },
                                    placeholder = { Text("eyJhbGciOiJIUzI1NiIsInR5cCI6...") },
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = NovaPrimary,
                                        unfocusedBorderColor = Color(0xFF22263C)
                                    )
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (cloudSyncConfig.lastSyncTimestamp > 0L)
                                            "Last Synced: Just now"
                                        else
                                            "Status: Sync Ready",
                                        fontSize = 12.sp,
                                        color = Color(0xFF8B92B2)
                                    )

                                    Button(
                                        onClick = {
                                            viewModel.saveCloudSync(
                                                cloudSyncConfig.copy(
                                                    supabaseUrl = supabaseUrl.trim(),
                                                    supabaseAnonKey = supabaseAnonKey.trim()
                                                )
                                            )
                                            isSyncing = true
                                            syncFeedbackMessage = null
                                            viewModel.triggerSupabaseSync { success, msg ->
                                                isSyncing = false
                                                syncFeedbackMessage = msg
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                                        shape = RoundedCornerShape(10.dp),
                                        enabled = !isSyncing,
                                        modifier = Modifier.testTag("trigger_sync_button")
                                    ) {
                                        if (isSyncing) {
                                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Syncing...")
                                        } else {
                                            Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Sync Now")
                                        }
                                    }
                                }

                                syncFeedbackMessage?.let { msg ->
                                    Text(
                                        text = msg,
                                        color = if (msg.contains("successfully", ignoreCase = true)) Color(0xFF00E676) else Color(0xFFFF5252),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Player & Decoder Settings
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    text = "Playback & Hardware Decoders",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Hardware Acceleration (HW+)", color = Color.White, fontWeight = FontWeight.SemiBold)
                                        Text("Direct GPU decoding for 4K HEVC & AV1", color = Color(0xFF8B92B2), fontSize = 12.sp)
                                    }
                                    Switch(
                                        checked = settings.hardwareAcceleration,
                                        onCheckedChange = { viewModel.saveSettings(settings.copy(hardwareAcceleration = it)) }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Auto Next Episode", color = Color.White, fontWeight = FontWeight.SemiBold)
                                        Text("Seamless binge play next season/episode", color = Color(0xFF8B92B2), fontSize = 12.sp)
                                    }
                                    Switch(
                                        checked = settings.autoplayNextEpisode,
                                        onCheckedChange = { viewModel.saveSettings(settings.copy(autoplayNextEpisode = it)) }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("HDR Tone Mapping", color = Color.White, fontWeight = FontWeight.SemiBold)
                                        Text("Convert HDR10 / Dolby Vision for SDR displays", color = Color(0xFF8B92B2), fontSize = 12.sp)
                                    }
                                    Switch(
                                        checked = settings.hdrToneMapping,
                                        onCheckedChange = { viewModel.saveSettings(settings.copy(hdrToneMapping = it)) }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Streaming Cache Size", color = Color.White, fontWeight = FontWeight.SemiBold)
                                        Text("${settings.streamingCacheSizeMb} MB buffer pool", color = Color(0xFF8B92B2), fontSize = 12.sp)
                                    }
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf(512, 1024, 2048).forEach { size ->
                                            FilterChip(
                                                selected = settings.streamingCacheSizeMb == size,
                                                onClick = { viewModel.saveSettings(settings.copy(streamingCacheSizeMb = size)) },
                                                label = { Text("${size}M", fontSize = 11.sp) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    // Profiles & Theming
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131526)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF22263C))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Active Profiles (${profiles.size})",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Button(
                                        onClick = { showNewProfileDialog = true },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("New Profile", fontSize = 11.sp)
                                    }
                                }

                                profiles.forEach { profile ->
                                    val isCurrent = currentProfile?.profileId == profile.profileId || profile.isCurrent
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isCurrent) NovaPrimary.copy(alpha = 0.2f) else Color(0xFF181B2E))
                                            .clickable { viewModel.selectProfile(profile.profileId) }
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.AccountCircle,
                                                contentDescription = null,
                                                tint = if (isCurrent) NovaAccentCyan else Color(0xFF7E84A6)
                                            )
                                            Text(
                                                text = profile.name,
                                                color = Color.White,
                                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                                            )
                                        }

                                        if (isCurrent) {
                                            Surface(
                                                color = NovaPrimary,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = "ACTIVE",
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Logout & Account
                    item {
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
                                    text = "Current Session",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = "Logged in as ${currentUser?.email ?: "StreamNova Hoarder"}",
                                    color = Color(0xFF8B92B2),
                                    fontSize = 13.sp
                                )

                                OutlinedButton(
                                    onClick = {
                                        viewModel.logout()
                                        onLogoutClick()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("logout_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF5252)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.5f))
                                ) {
                                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Log Out & Clear Session", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
