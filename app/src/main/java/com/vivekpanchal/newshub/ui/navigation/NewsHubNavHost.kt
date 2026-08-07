package com.vivekpanchal.newshub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vivekpanchal.newshub.ui.detail.NewsDetailScreen
import com.vivekpanchal.newshub.ui.onboarding.OnboardingScreen
import com.vivekpanchal.newshub.ui.splash.SplashScreen

@Composable
fun NewsHubNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = NavRoutes.SPLASH) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(NavRoutes.ONBOARDING) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.SPLASH) { inclusive = true }
                    }
                },
            )
        }
        composable(NavRoutes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToMain = {
                    navController.navigate(NavRoutes.MAIN) {
                        popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }
        composable(NavRoutes.MAIN) {
            MainScreen(
                onArticleClick = { article -> navController.navigate(NavRoutes.detail(article)) },
            )
        }
        composable(
            route = NavRoutes.DETAIL_PATTERN,
            arguments = listOf(
                navArgument(NavRoutes.DETAIL_ARG) {
                    @Suppress("UNCHECKED_CAST")
                    type = ArticleNavType as NavType<Any?>
                },
            ),
        ) {
            NewsDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
