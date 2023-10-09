package com.fjr619.newsloc.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.news_navigator.BottomBarScreen

/**
 *  https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/navigation/JetsnackNavController.kt
 */
@Composable
fun rememberNewsNavController(
    navHostController: NavHostController = rememberNavController()
): NewsNavController = remember(navHostController) {
    NewsNavController(navHostController)
}

@Stable
class NewsNavController(
    val navController: NavHostController
) {
    val currentRoute: String?
        get() = navController.currentDestination?.route

    /**
     * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
     *
     * This is used to de-duplicate navigation events.
     */

    private fun NavBackStackEntry.lifecycleIsResumed() =
        this.lifecycle.currentState == Lifecycle.State.RESUMED

    /**
     * Copied from similar function in NavigationUI.kt
     *
     * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
     */
    private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
        return if (graph is NavGraph) findStartDestination(graph.findStartDestination()) else graph
    }

    fun navigateToBottomBarRoute(bottomBarScreen: BottomBarScreen) {
        if (bottomBarScreen.route != currentRoute) {
            navController.navigate(bottomBarScreen.route) {
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true

            }
        }
    }

    fun navigateToDetail(article: Article, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
            navController.navigate(
                route = Route.DetailsScreen.route
            )
        }
    }
}