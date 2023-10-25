package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController
import com.fjr619.newsloc.presentation.news_navigator.components.BottomBar
import com.fjr619.newsloc.presentation.news_navigator.components.NavRail
import com.fjr619.newsloc.presentation.news_navigator.components.PermanentNavDrawer

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator(
  navigationType: NavigationType,
  countBookmark: Int,
) {
  val newsNavController = rememberNewsNavController()
  val materialNavigationState = rememberMaterialNavigationState(
    newsNavController = newsNavController,
    navBackStackEntry = newsNavController.navController.currentBackStackEntryAsState(),
  )

  val screens = listOf(
    MaterialNavScreen.Home,
    MaterialNavScreen.Search,
    MaterialNavScreen.Bookmark
  )

  Scaffold(
    bottomBar = {
      AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAV) {
        BottomBar(
          materialNavigationState = materialNavigationState,
          screens = screens,
          countBookmark = countBookmark,
          onNavigateBottomBar = materialNavigationState::navigateToBottomBarRoute
        )
      }
    }
  ) {

    Row(
      modifier = Modifier.fillMaxSize()
    ) {

      AnimatedVisibility(visible = navigationType == NavigationType.NAV_RAIL && materialNavigationState.showNavigation { screens }) {
        NavRail(
          materialNavigationState = materialNavigationState,
          screens = screens,
          onNavigateBottomBar = materialNavigationState::navigateToBottomBarRoute,
          countBookmark = countBookmark
        )
      }

      PermanentNavigationDrawer(drawerContent = {
        AnimatedVisibility(visible = navigationType == NavigationType.PERMANENT_NAV_DRAWER && materialNavigationState.showNavigation { screens }) {
          PermanentNavDrawer(
            materialNavigationState = materialNavigationState,
            screens = screens,
            onNavigateBottomBar = materialNavigationState::navigateToBottomBarRoute,
            countBookmark = countBookmark
          )
        }
      }
      ) {
        NewsGraph(
          paddingValues = it,
          navController = newsNavController.navController,
          onNavigateBottomBar = materialNavigationState::navigateToBottomBarRoute,
          onNavigateToDetail = newsNavController::navigateToDetail
        )
      }
    }
  }
}

