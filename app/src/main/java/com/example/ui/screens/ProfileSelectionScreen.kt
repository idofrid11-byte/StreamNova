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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary

@Composable
fun ProfileSelectionScreen(
    onProfileSelected: (String) -> Unit
) {
    val profiles = remember {
        listOf("Main Hoarder", "Kids Safe", "Cinema Room", "Guest")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A10))
            .testTag("profile_selection_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🎬 MediaHoard",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Text(
                text = "Who is hoarding today?",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFA5ABC8),
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(profiles) { profileName ->
                    ProfileAvatarItem(
                        name = profileName,
                        onClick = { onProfileSelected(profileName) }
                    )
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onProfileSelected("Custom Profile") }
                            .padding(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color(0xFF333857), CircleShape),
                            color = Color(0xFF131525)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Profile",
                                    tint = NovaPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Add Profile",
                            color = Color(0xFF888EA8),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onProfileSelected("DefaultUser") },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(48.dp)
                    .testTag("main_hoarder_profile_button"),
                colors = ButtonDefaults.buttonColors(containerColor = NovaPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "👤 Quick Enter (Main Hoarder)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileAvatarItem(
    name: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
            .testTag("profile_item_$name")
    ) {
        Surface(
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .border(2.dp, NovaPrimary, CircleShape),
            color = Color(0xFF1B1E34),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = name,
                    tint = NovaAccentCyan,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}
