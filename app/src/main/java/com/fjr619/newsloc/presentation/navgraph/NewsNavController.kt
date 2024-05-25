package com.fjr619.newsloc.presentation.navgraph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 *  https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/navigation/JetsnackNavController.kt
 */


sealed class MaterialNavScreen(
  val route: String,
  val title: String,
  val icon: ImageVector,
  val iconFiiled: ImageVector,
  val hasBadge: Boolean,
) {
  data object Home : MaterialNavScreen(
    Route.HomeScreen.route, "Home", Icons.Outlined.Home, Icons.Filled.Home, hasBadge = false
  )

  data object Search : MaterialNavScreen(
    Route.SearchScreen.route, "Search", Icons.Outlined.Search, Icons.Filled.Search, hasBadge = false
  )

  data object Bookmark : MaterialNavScreen(
    Route.BookmarkScreen.route,
    "Bookmark",
    Icons.Outlined.FavoriteBorder,
    Icons.Filled.Favorite,
    hasBadge = true
  )
}

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
  private val currentRoute: String? = navController.currentDestination?.route

  /**
   * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
   *
   * This is used to de-duplicate navigation events.
   */

  private fun lifecycleIsResumed() =
    navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED

//  /**
//   * Copied from similar function in NavigationUI.kt
//   *
//   * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:navigation/navigation-ui/src/main/java/androidx/navigation/ui/NavigationUI.kt
//   */
//  private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
//    return if (graph is NavGraph) findStartDestination(graph.findStartDestination()) else graph
//  }

  fun navigateToBottomBarRoute(materialNavScreen: MaterialNavScreen) {
    if (lifecycleIsResumed() && materialNavScreen.route != currentRoute) {
      navController.navigate(materialNavScreen.route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
          saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true

      }
    }
  }

  fun navigateToDetail() {
    if (lifecycleIsResumed()) {
//            navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
      navController.navigate(
        route = Route.DetailsScreen.route
      )
    }
  }

  fun navigateBack() {
    if (lifecycleIsResumed()) {
      navController.popBackStack()
    }      
  }

  fun navigateToMain() {
    if (lifecycleIsResumed()) {
      navController.navigate(Route.NewsNavigation.route) {
        popUpTo(Route.RootNavigation.route) {
          inclusive = true
        }
      }
    }
  }

  @Composable
  fun showNavigation(list: () -> List<MaterialNavScreen>): Boolean {
    return currentDestination()?.route in list().map {
      it.route
    }
  }

  @Composable
  fun isSelected(screen: MaterialNavScreen): Boolean {
    return currentDestination()?.hierarchy?.any {
      it.route == screen.route
    } == true
  }

  @Composable
  fun currentDestination(): NavDestination? {
    return navController.currentBackStackEntryAsState().value?.destination
  }
}