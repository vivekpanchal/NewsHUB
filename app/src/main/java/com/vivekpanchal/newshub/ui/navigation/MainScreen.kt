package com.vivekpanchal.newshub.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vivekpanchal.newshub.domain.model.Article
import com.vivekpanchal.newshub.ui.common.ConnectivityViewModel
import com.vivekpanchal.newshub.ui.common.NoConnectionBanner
import com.vivekpanchal.newshub.ui.favorites.FavoritesScreen
import com.vivekpanchal.newshub.ui.search.SearchScreen
import com.vivekpanchal.newshub.ui.topheadlines.TopHeadlinesScreen
import com.vivekpanchal.newshub.ui.yourfeed.YourFeedScreen

@Composable
fun MainScreen(
    onArticleClick: (Article) -> Unit,
    connectivityViewModel: ConnectivityViewModel = hiltViewModel(),
) {
    val tabNavController: NavHostController = rememberNavController()
    val isConnected by connectivityViewModel.isConnected.collectAsState()

    Scaffold(
        bottomBar = { NewsHubBottomNavBar(tabNavController) },
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NoConnectionBanner(visible = !isConnected)
            NavHost(
                navController = tabNavController,
                startDestination = MainTabRoutes.TOP_HEADLINES,
                modifier = Modifier.weight(1f),
            ) {
                composable(MainTabRoutes.TOP_HEADLINES) {
                    TopHeadlinesScreen(onArticleClick = onArticleClick)
                }
                composable(MainTabRoutes.YOUR_FEED) {
                    YourFeedScreen(onArticleClick = onArticleClick)
                }
                composable(MainTabRoutes.FAVORITES) {
                    FavoritesScreen(onArticleClick = onArticleClick)
                }
                composable(MainTabRoutes.SEARCH) {
                    SearchScreen(onArticleClick = onArticleClick)
                }
            }
        }
    }
}
