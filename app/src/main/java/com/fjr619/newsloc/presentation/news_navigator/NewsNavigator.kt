package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.window.core.layout.WindowWidthSizeClass
import com.fjr619.newsloc.presentation.common.NewsSnackbar
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController
import com.fjr619.newsloc.util.Digit
import com.fjr619.newsloc.util.compareTo
import com.fjr619.newsloc.util.snackbar.ProvideSnackbarController


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
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
  val showNavigator = newsNavController.showNavigation {
    screens
  }

  //ketika di navigation item bentukannya list-detail, dan detailnya lagi muncul
  val isRootNavigationDetailAppear by newsNavController.isRootNavigationDetailApppear()

  val customNavSuiteType = remember(showNavigator, isRootNavigationDetailAppear) {
    if (showNavigator) {
      with(adaptiveInfo) {
        println("adaptive info $adaptiveInfo")
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM) {
          NavigationSuiteType.NavigationRail
        } else if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT && isRootNavigationDetailAppear){
          NavigationSuiteType.None
        } else {
          NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }
      }
    } else {
      NavigationSuiteType.None
    }
  }



  val snackbarHostState = remember { SnackbarHostState() }
  val coroutineScope = rememberCoroutineScope()

  val currentDestination = newsNavController.currentDestination()


  ProvideSnackbarController(
    snackbarHostState = snackbarHostState,
    coroutineScope = coroutineScope
  ) {
    NavigationSuiteScaffold(
      modifier = Modifier.fillMaxSize(),
      layoutType = customNavSuiteType,
      navigationSuiteItems = {
        screens.forEach { screen ->
          item(
            badge = {
              if (screen.hasBadge && countBookmark != 0)
                Badge {
                  countBookmark.toString()
                    .mapIndexed { index, c -> Digit(c, countBookmark, index) }
                    .forEach { digit ->
                      AnimatedContent(
                        targetState = digit,
                        transitionSpec = {
                          if (targetState > initialState) {
                            slideInVertically { -it } togetherWith slideOutVertically { it }
                          } else {
                            slideInVertically { it } togetherWith slideOutVertically { -it }
                          }
                        }, label = ""
                      ) { digit ->
                        Text(text = "${digit.digitChar}")
                      }
                    }
                }
            },
            icon = {
              Icon(
                modifier = Modifier.size(20.dp),
                imageVector = screen.icon,
                contentDescription = null
              )
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
    ) {
      Scaffold(
        snackbarHost = {
          SnackbarHost(hostState = snackbarHostState)
          { data ->
            NewsSnackbar(data = data)
          }
        },
      ) {
        NewsGraph(
          newsNavController = newsNavController,
        )
      }
    }
  }
}

