package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.core.layout.WindowWidthSizeClass
import com.fjr619.newsloc.presentation.common.NewsSnackbar
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController
import com.fjr619.newsloc.util.snackbar.ProvideSnackbarController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator(
//  navigationType: NavigationType,
  countBookmark: Int,
) {

  val screens = listOf(
    MaterialNavScreen.Home,
    MaterialNavScreen.Search,
    MaterialNavScreen.Bookmark
  )

  val newsNavController = rememberNewsNavController()
  val adaptiveInfo = currentWindowAdaptiveInfo()
  val customNavSuiteType =
    if (newsNavController.showNavigation {
      screens
    }) {
    with(adaptiveInfo) {
      if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        NavigationSuiteType.NavigationDrawer
      } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
        NavigationSuiteType.NavigationRail
      } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
      }
    }
  } else {
    NavigationSuiteType.None
  }


  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val currentDestination = newsNavController.currentDestination()
  val showNavigator = newsNavController.showNavigation {
    screens
  }

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
    ) {
      NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteColors = if (showNavigator) {
          NavigationSuiteDefaults.colors()
        } else {
          NavigationSuiteDefaults.colors(
            navigationBarContainerColor = Color.Transparent,
            navigationBarContentColor = Color.Transparent,
            navigationRailContainerColor = Color.Transparent,
            navigationRailContentColor = Color.Transparent,
            navigationDrawerContainerColor = Color.Transparent,
            navigationDrawerContentColor = Color.Transparent,
          )
        },
        layoutType = customNavSuiteType,
        navigationSuiteItems = {
          if (showNavigator) {
            screens.forEach { screen ->
              item(
                icon = {
                  BadgedBox(badge = {
                    if (screen.hasBadge && countBookmark != 0)
                      Badge {
                        Text(text = "$countBookmark")
                      }
                  }) {
                    Icon(
                      modifier = Modifier.size(20.dp),
                      imageVector = screen.icon,
                      contentDescription = null
                    )
                  }
                },
                label = {
                  Text(text = screen.title, style = MaterialTheme.typography.labelSmall)
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                  newsNavController.navigateToBottomBarRoute(screen)
                }
              )
            }
          }
        }
      ) {
        NewsGraph(
          paddingValues = PaddingValues(),
          newsNavController = newsNavController,
        )
      }
    }



//    Scaffold(
//      snackbarHost = {
//        SnackbarHost(hostState = snackbarHostState)
//        { data ->
//          NewsSnackbar(data = data)
//        }
//      },
//      bottomBar = {
//        AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAV) {
//          BottomBar(
//            newsNavController = newsNavController,
//            screens = screens,
//            countBookmark = countBookmark,
//            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute
//          )
//        }
//      }
//    ) {
//
//      Row(
//        modifier = Modifier.fillMaxSize()
//      ) {
//
//        AnimatedVisibility(visible = navigationType == NavigationType.NAV_RAIL && newsNavController.showNavigation { screens }) {
//          NavRail(
//            newsNavController = newsNavController,
//            screens = screens,
//            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
//            countBookmark = countBookmark
//          )
//        }
//
//        PermanentNavigationDrawer(drawerContent = {
//          AnimatedVisibility(visible = navigationType == NavigationType.PERMANENT_NAV_DRAWER && newsNavController.showNavigation { screens }) {
//            PermanentNavDrawer(
//              newsNavController = newsNavController,
//              screens = screens,
//              onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
//              countBookmark = countBookmark
//            )
//          }
//        }
//        ) {
//          NewsGraph(
//            paddingValues = it,
//            newsNavController = newsNavController,
//          )
//        }
//      }
//    }
  }
}

