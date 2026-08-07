package com.vivekpanchal.newshub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vivekpanchal.newshub.R

private data class BottomNavTab(
    val route: String,
    val labelRes: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val TABS = listOf(
    BottomNavTab(MainTabRoutes.TOP_HEADLINES, R.string.title_top_headlines, Icons.Filled.TrendingUp),
    BottomNavTab(MainTabRoutes.YOUR_FEED, R.string.title_your_feed, Icons.Filled.Person),
    BottomNavTab(MainTabRoutes.FAVORITES, R.string.title_favorites, Icons.Filled.Favorite),
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
