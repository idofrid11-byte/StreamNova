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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
fun AppNavigation(
    viewModel: StreamNovaViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var isAuthenticated by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            isAuthenticated = true
        }
    }

    if (!isAuthenticated) {
        AuthScreen(
            viewModel = viewModel,
            onLoginSuccess = { isAuthenticated = true }
        )
    } else {
        val isPlayerScreen = currentRoute?.startsWith("player") == true
        val isTvMode = currentRoute == "wuplay_tv"

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFF090A10),
            bottomBar = {
                if (!isPlayerScreen && !isTvMode) {
                    NavigationBar(
                        containerColor = Color(0xFF0E101D),
                        contentColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.testTag("main_bottom_nav")
                    ) {
                        val isHome = currentRoute == "home"
                        val isDiscover = currentRoute == "discover"
                        val isHubs = currentRoute == "hubs"
                        val isLibrary = currentRoute == "library"
                        val isAddons = currentRoute == "addons"
                        val isSettings = currentRoute == "settings"

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            },
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
                            modifier = Modifier.testTag("nav_item_home")
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isDiscover) Icons.Filled.Explore else Icons.Outlined.Explore,
                                    contentDescription = "Discover"
                                )
                            },
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
                            modifier = Modifier.testTag("nav_item_discover")
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isHubs) Icons.Filled.Widgets else Icons.Outlined.Widgets,
                                    contentDescription = "Hubs"
                                )
                            },
                            label = { Text("Hubs", fontSize = 11.sp, fontWeight = if (isHubs) FontWeight.Bold else FontWeight.Normal) },
                            selected = isHubs,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isHubs) {
                                    navController.navigate("hubs") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("nav_item_hubs")
                        )

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isLibrary) Icons.Filled.VideoLibrary else Icons.Outlined.VideoLibrary,
                                    contentDescription = "Library"
                                )
                            },
                            label = { Text("Library", fontSize = 11.sp, fontWeight = if (isLibrary) FontWeight.Bold else FontWeight.Normal) },
                            selected = isLibrary,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = NovaPrimary,
                                unselectedIconColor = Color(0xFF7E84A6),
                                unselectedTextColor = Color(0xFF7E84A6)
                            ),
                            onClick = {
                                if (!isLibrary) {
                                    navController.navigate("library") {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            modifier = Modifier.testTag("nav_item_library")
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
                    FlowvidHomeScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        },
                        onPlayDirect = { streamUrl, title ->
                            val encodedUrl = Uri.encode(streamUrl)
                            val encodedTitle = Uri.encode(title)
                            navController.navigate("player/$encodedUrl/$encodedTitle")
                        },
                        onSwitchToTvMode = {
                            navController.navigate("wuplay_tv")
                        },
                        onSearchClick = {
                            navController.navigate("discover")
                        }
                    )
                }

                composable("mediahoard") {
                    MediaHoardHomeScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("discover") {
                    DiscoverScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("hubs") {
                    MediaHubsScreen(
                        viewModel = viewModel,
                        onPlayMedia = { streamUrl, title ->
                            val encodedUrl = Uri.encode(streamUrl)
                            val encodedTitle = Uri.encode(title)
                            navController.navigate("player/$encodedUrl/$encodedTitle")
                        },
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        }
                    )
                }

                composable("library") {
                    LibraryScreen(
                        viewModel = viewModel,
                        onItemClick = { type, id ->
                            navController.navigate("details/$type/$id")
                        },
                        onPlayDirect = { streamUrl, title ->
                            val encodedUrl = Uri.encode(streamUrl)
                            val encodedTitle = Uri.encode(title)
                            navController.navigate("player/$encodedUrl/$encodedTitle")
                        }
                    )
                }

                composable("addons") {
                    AddonManagerScreen(viewModel = viewModel)
                }

                composable("settings") {
                    SettingsDashboardScreen(
                        viewModel = viewModel,
                        onLogoutClick = { isAuthenticated = false },
                        onSwitchProfileClick = {
                            navController.navigate("wuplay_tv")
                        }
                    )
                }

                composable("wuplay_tv") {
                    WuPlayTvNavigation(viewModel = viewModel)
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
                            val encodedTitle = Uri.encode("Stream Playback")
                            navController.navigate("player/$encodedUrl/$encodedTitle?id=$id&type=$type")
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
