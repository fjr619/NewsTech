package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.navgraph.NewsNavController
import com.fjr619.newsloc.presentation.navgraph.Route
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController

sealed class MaterialNavScreen(
  val route: String,
  val title: String,
  val icon: ImageVector,
  val hasBadge: Boolean,
) {
  data object Home : MaterialNavScreen(
    Route.HomeScreen.route, "Home", Icons.Outlined.Home, hasBadge = false
  )

  data object Search : MaterialNavScreen(
    Route.SearchScreen.route, "Search", Icons.Outlined.Search, hasBadge = false
  )

  data object Bookmark : MaterialNavScreen(
    Route.BookmarkScreen.route, "Bookmark", Icons.Outlined.FavoriteBorder, hasBadge = true
  )
}

@Composable
fun rememberMaterialNavigationState(
  currentIndex: Int = 0,
  newsNavController: NewsNavController = rememberNewsNavController(),
  navBackStackEntry: State<NavBackStackEntry?>
): MaterialNavigationState {
  return rememberSaveable(
    saver = MaterialNavigationState.saver(
      navBackStackEntry,
      newsNavController
    )
  ) {
    MaterialNavigationState(currentIndex, newsNavController, navBackStackEntry)
  }
}

@Stable
class MaterialNavigationState(
  currentIndex: Number,
  val newsNavController: NewsNavController,
  val navBackStackEntry: State<NavBackStackEntry?>,
) {
  var currentIndex by mutableStateOf(currentIndex)
    private set

  fun setCurrentIndex(curIndex: Int) {
    this.currentIndex = curIndex
  }

  fun isSelected(screen: MaterialNavScreen): Boolean {
    return navBackStackEntry.value?.destination?.hierarchy?.any {
      it.route == screen.route
    } == true
  }

  fun navigateToBottomBarRoute(materialNavScreen: MaterialNavScreen) {
    newsNavController.navigateToBottomBarRoute(materialNavScreen)
  }

  @Composable
  fun showNavigation(list: () -> List<MaterialNavScreen>): Boolean {
    return newsNavController.navController.currentBackStackEntryAsState().value?.destination?.route in list().map {
      it.route
    }
  }

  companion object {
    fun saver(
      navBackStackEntry: State<NavBackStackEntry?>,
      newsNavController: NewsNavController
    ): Saver<MaterialNavigationState, *> = listSaver(
      save = {
        listOf(/*it.width,*/ it.currentIndex)
      },
      restore = {
        MaterialNavigationState(
          /*width = it[0],*/
          currentIndex = it[0],
          navBackStackEntry = navBackStackEntry,
          newsNavController = newsNavController
        )
      }
    )
  }
}