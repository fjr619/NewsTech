package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator(
  navigationType: NavigationType,
  countBookmark: Int,
) {
  val newsNavController = rememberNewsNavController()
  val bottomBarState = rememberBottomBarState(
    newsNavController = newsNavController,
    navBackStackEntry = newsNavController.navController.currentBackStackEntryAsState(),
  )

  val screens = listOf(
    BottomBarScreen.Home,
    BottomBarScreen.Search,
    BottomBarScreen.Bookmark
  )

    Scaffold(
      bottomBar = {
        AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAV) {
          BottomBar(
            bottomBarState = bottomBarState,
            screens = screens,
            countBookmark = countBookmark,
            onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute
          )
        }
      }
    ) {

      Row(
        modifier = Modifier.fillMaxSize()
      ) {

        AnimatedVisibility(visible  = navigationType == NavigationType.NAV_RAIL && bottomBarState.showNavigation { screens }) {
          NavRail(
            bottomBarState = bottomBarState,
            screens = screens,
            onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute,
            countBookmark = countBookmark
          )
        }
        
        NewsGraph(
          paddingValues = it,
          navController = newsNavController.navController,
          onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute,
          onNavigateToDetail = newsNavController::navigateToDetail
        )
      }
    }
}
