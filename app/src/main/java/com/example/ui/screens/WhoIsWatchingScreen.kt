package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults as TvCardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WhoIsWatchingScreen(
    onProfileSelected: (String) -> Unit
) {
    val profiles = remember {
        listOf(
            "profile_living_room" to "Living Room TV",
            "profile_master_bed" to "Master Bedroom",
            "profile_kids" to "Kids Zone"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("who_is_watching_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tv,
                    contentDescription = null,
                    tint = NovaPrimary,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = "Who's Watching?",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Select a profile to load personalized watchlists, recommendations, and addons",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF8B92B2)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(profiles) { (id, name) ->
                    Card(
                        onClick = { onProfileSelected(id) },
                        modifier = Modifier
                            .width(160.dp)
                            .height(200.dp)
                            .testTag("tv_profile_card_$id"),
                        scale = TvCardDefaults.scale(focusedScale = 1.12f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF131526))
                                .border(1.dp, Color(0xFF22263C), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, NovaPrimary, CircleShape),
                                    color = Color(0xFF1B1E34)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = name,
                                            tint = NovaAccentCyan,
                                            modifier = Modifier.size(36.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        onClick = { onProfileSelected("profile_guest") },
                        modifier = Modifier
                            .width(160.dp)
                            .height(200.dp)
                            .testTag("tv_profile_card_guest"),
                        scale = TvCardDefaults.scale(focusedScale = 1.12f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF131526))
                                .border(1.dp, Color(0xFF22263C), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .border(1.dp, Color(0xFF333857), CircleShape),
                                    color = Color(0xFF0F1120)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Guest",
                                            tint = Color(0xFF8B92B2),
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Guest Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF8B92B2)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
