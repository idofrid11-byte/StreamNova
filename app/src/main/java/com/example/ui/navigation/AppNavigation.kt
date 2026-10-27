package com.example.ui.navigation

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.NovaPrimary
import com.example.ui.viewmodel.StreamNovaViewModel

@Composable
fun AppNavigation(
    viewModel: StreamNovaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                    modifier = Modifier.testTag("main_bottom_nav")
                ) {
                    val isHome = currentRoute == "home"
                    val isSearch = currentRoute == "search"
                    val isAddons = currentRoute == "addons"
                    val isSettings = currentRoute == "settings"

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Discover"
                            )
                        },
                        label = { Text("Discover", fontSize = 11.sp, fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal) },
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
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isSearch) Icons.Filled.Search else Icons.Outlined.Search,
                                contentDescription = "Search"
                            )
                        },
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
                        modifier = Modifier.testTag("nav_item_search")
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isAddons) Icons.Filled.Extension else Icons.Outlined.Extension,
                                contentDescription = "Addons"
                            )
                        },
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
                        modifier = Modifier.testTag("nav_item_addons")
                    )

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isSettings) Icons.Filled.Settings else Icons.Outlined.Settings,
                                contentDescription = "Settings"
                            )
                        },
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
                        modifier = Modifier.testTag("nav_item_settings")
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
                HomeScreen(
                    viewModel = viewModel,
                    onItemClick = { type, id ->
                        navController.navigate("details/$type/$id")
                    },
                    onDirectPlayClick = { streamUrl, title ->
                        val encodedUrl = Uri.encode(streamUrl)
                        val encodedTitle = Uri.encode(title)
                        navController.navigate("player/$encodedUrl/$encodedTitle")
                    }
                )
            }

            composable("search") {
                SearchScreen(
                    viewModel = viewModel,
                    onItemClick = { type, id ->
                        navController.navigate("details/$type/$id")
                    }
                )
            }

            composable("addons") {
                AddonsScreen(viewModel = viewModel)
            }

            composable("settings") {
                SettingsScreen(viewModel = viewModel)
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
                DetailsScreen(
                    type = type,
                    id = id,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onPlayClick = { streamUrl, title ->
                        val encodedUrl = Uri.encode(streamUrl)
                        val encodedTitle = Uri.encode(title)
                        navController.navigate("player/$encodedUrl/$encodedTitle?id=$id&type=$type")
                    }
                )
            }

            composable(
                route = "player/{url}/{title}?id={id}&type={type}",
                arguments = listOf(
                    navArgument("url") { type = NavType.StringType },
                    navArgument("title") {
                        type = NavType.StringType
                        defaultValue = "Now Playing"
                    },
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("type") {
                        type = NavType.StringType
                        defaultValue = "movie"
                    }
                )
            ) { backStackEntry ->
                val url = Uri.decode(backStackEntry.arguments?.getString("url") ?: "")
                val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "Now Playing")
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val type = backStackEntry.arguments?.getString("type") ?: "movie"

                PlayerScreen(
                    videoUrl = url,
                    title = title,
                    type = type,
                    id = id,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
