package com.vivekpanchal.newshub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vivekpanchal.newshub.R
import com.vivekpanchal.newshub.ui.theme.NewsHubPreviewSurface

private data class BottomNavTab(
    val route: String,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val TABS = listOf(
    BottomNavTab(MainTabRoutes.HOME, R.string.title_home, Icons.Filled.Home),
    BottomNavTab(MainTabRoutes.YOUR_FEED, R.string.title_your_feed, Icons.Filled.Person),
    BottomNavTab(MainTabRoutes.FAVORITES, R.string.title_saved, Icons.Filled.Bookmark),
    BottomNavTab(MainTabRoutes.SEARCH, R.string.title_search, Icons.Filled.Search),
)

@Composable
fun NewsHubBottomNavBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar {
        TABS.forEach { tab ->
            val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = stringResource(tab.labelRes)) },
                label = { Text(stringResource(tab.labelRes)) },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun NewsHubBottomNavBarPreview() {
    NewsHubPreviewSurface { NewsHubBottomNavBar(navController = rememberNavController()) }
}
