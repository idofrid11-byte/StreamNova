package com.example.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.theme.NovaAccentCyan
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun WuPlayTvNavigation(
    viewModel: StreamNovaViewModel = viewModel()
) {
    val navController = rememberNavController()
    var currentProfileId by remember { mutableStateOf<String?>(null) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentProfileId == null) {
        WhoIsWatchingScreen(
            onProfileSelected = { profileId ->
                currentProfileId = profileId
            }
        )
    } else {
        val isPlayerScreen = currentRoute?.startsWith("player") == true

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFF090A10),
            bottomBar = {
                if (!isPlayerScreen) {
                    NavigationBar(
                        containerColor = Color(0xFF0E101D),
                        contentColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("wuplay_bottom_nav")
                    ) {
                        val isHome = currentRoute == "home"
                        val isDiscover = currentRoute == "discover"
                        val isSearch = currentRoute == "search"
                        val isWatchlist = currentRoute == "watchlist"
                        val isAddons = currentRoute == "addons"
                        val isSettings = currentRoute == "settings"

                        NavigationBarItem(
                            icon = { Icon(if (isHome) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 11.sp, fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal) },
                            selected = isHome,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isHome) {
                                    navController.navigate("home") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_home")
                        )

                        NavigationBarItem(
                            icon = { Icon(if (isDiscover) Icons.Filled.Explore else Icons.Outlined.Explore, contentDescription = "Discover") },
                            label = { Text("Discover", fontSize = 11.sp, fontWeight = if (isDiscover) FontWeight.Bold else FontWeight.Normal) },
                            selected = isDiscover,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isDiscover) {
                                    navController.navigate("discover") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_discover")
                        )

                        NavigationBarItem(
                            icon = { Icon(if (isSearch) Icons.Filled.Search else Icons.Outlined.Search, contentDescription = "Search") },
                            label = { Text("Search", fontSize = 11.sp, fontWeight = if (isSearch) FontWeight.Bold else FontWeight.Normal) },
                            selected = isSearch,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isSearch) {
                                    navController.navigate("search") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_search")
                        )

                        NavigationBarItem(
                            icon = { Icon(if (isWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, contentDescription = "Watchlist") },
                            label = { Text("Watchlist", fontSize = 11.sp, fontWeight = if (isWatchlist) FontWeight.Bold else FontWeight.Normal) },
                            selected = isWatchlist,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isWatchlist) {
                                    navController.navigate("watchlist") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_watchlist")
                        )

                        NavigationBarItem(
                            icon = { Icon(if (isAddons) Icons.Filled.Extension else Icons.Outlined.Extension, contentDescription = "Addons") },
                            label = { Text("Addons", fontSize = 11.sp, fontWeight = if (isAddons) FontWeight.Bold else FontWeight.Normal) },
                            selected = isAddons,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isAddons) {
                                    navController.navigate("addons") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_addons")
                        )

                        NavigationBarItem(
                            icon = { Icon(if (isSettings) Icons.Filled.Settings else Icons.Outlined.Settings, contentDescription = "Settings") },
                            label = { Text("Settings", fontSize = 11.sp, fontWeight = if (isSettings) FontWeight.Bold else FontWeight.Normal) },
                            selected = isSettings,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isSettings) {
                                    navController.navigate("settings") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("wuplay_nav_settings")
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    WuPlayHomeScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("discover") {
                    WuPlayDiscoverScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("search") {
                    WuPlaySearchScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("watchlist") {
                    WuPlayWatchlistScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("addons") {
                    WuPlayAddonsScreen(viewModel = viewModel)
                }

                composable("settings") {
                    WuPlaySettingsScreen()
                }

                composable(
                    route = "details/{type}/{id}",
                    arguments = listOf(
                        navArgument("type") { type = NavType.StringType },
                        navArgument("id") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val type = backStackEntry.arguments?.getString("type") ?: "movie"
                    val id = backStackEntry.arguments?.getString("id") ?: ""
                    WuPlayDetailsScreen(
                        type = type,
                        id = id,
                        viewModel = viewModel,
                        onPlayClick = { streamUrl ->
                            val encodedUrl = Uri.encode(streamUrl)
                            navController.navigate("player/$encodedUrl")
                        },
                        onPersonClick = { personId ->
                            navController.navigate("person/$personId")
                        }
                    )
                }

                composable(
                    route = "person/{personId}",
                    arguments = listOf(navArgument("personId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val personId = backStackEntry.arguments?.getString("personId") ?: ""
                    PersonDetailScreen(
                        personId = personId,
                        onBackClick = { navController.popBackStack() },
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable(
                    route = "player/{url}",
                    arguments = listOf(navArgument("url") { type = NavType.StringType })
                ) { backStackEntry ->
                    val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
                    PlayerScreen(
                        videoUrl = url,
                        title = "WuPlay TV Stream",
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
