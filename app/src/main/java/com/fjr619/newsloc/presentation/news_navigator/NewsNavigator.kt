package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.fjr619.newsloc.presentation.common.NewsSnackbar
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController
import com.fjr619.newsloc.presentation.news_navigator.components.BottomBar
import com.fjr619.newsloc.presentation.news_navigator.components.NavRail
import com.fjr619.newsloc.presentation.news_navigator.components.PermanentNavDrawer
import com.fjr619.newsloc.util.snackbar.ProvideSnackbarController

@Composable
fun NewsNavigator(
  navigationType: NavigationType,
  countBookmark: Int,
) {
  val newsNavController = rememberNewsNavController()


  val screens = listOf(
    MaterialNavScreen.Home,
    MaterialNavScreen.Search,
    MaterialNavScreen.Bookmark
  )

  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope
  ) {
    Scaffold(
      snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
        { data ->
          NewsSnackbar(data = data)
        }
      },
      bottomBar = {
        AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAV) {
          BottomBar(
            newsNavController = newsNavController,
            screens = screens,
            countBookmark = countBookmark,
            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute
          )
        }
      }
    ) { paddingValues ->

      Row(
        modifier = Modifier.fillMaxSize().padding(paddingValues)
      ) {

        AnimatedVisibility(visible = navigationType == NavigationType.NAV_RAIL && newsNavController.showNavigation { screens }) {
          NavRail(
            newsNavController = newsNavController,
            screens = screens,
            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
            countBookmark = countBookmark
          )
        }

        PermanentNavigationDrawer(drawerContent = {
          AnimatedVisibility(visible = navigationType == NavigationType.PERMANENT_NAV_DRAWER && newsNavController.showNavigation { screens }) {
            PermanentNavDrawer(
              newsNavController = newsNavController,
              screens = screens,
              onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
              countBookmark = countBookmark
            )
          }
        }
        ) {
          NewsGraph(
            navController = newsNavController.navController,
            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
            onNavigateToDetail = newsNavController::navigateToDetail,
            onNavigateBack = newsNavController::navigateBack
          )
        }
      }
    }
  }
}

