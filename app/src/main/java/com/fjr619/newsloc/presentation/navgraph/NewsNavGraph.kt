package com.fjr619.newsloc.presentation.navgraph

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.presentation.bookmark.BookmarkScreen
import com.fjr619.newsloc.presentation.bookmark.BookmarkViewModel
import com.fjr619.newsloc.presentation.common.NewsSnackbarVisual
import com.fjr619.newsloc.presentation.detail.DetailEvent
import com.fjr619.newsloc.presentation.detail.DetailScreen
import com.fjr619.newsloc.presentation.detail.DetailViewModel
import com.fjr619.newsloc.presentation.home.HomeEvent
import com.fjr619.newsloc.presentation.home.HomeScreen
import com.fjr619.newsloc.presentation.home.HomeViewModel
import com.fjr619.newsloc.presentation.search.SearchScreen
import com.fjr619.newsloc.presentation.search.SearchViewModel
import com.fjr619.newsloc.util.composestateevents.EventEffect
import com.fjr619.newsloc.util.snackbar.LocalSnackbarController
import com.fjr619.newsloc.util.snackbar.asString

val LocalAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope> { error("") }
var refreshHome = false

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NewsGraph(
  paddingValues: PaddingValues,
  newsNavController: NewsNavController,
//  onNavigateBottomBar: (MaterialNavScreen) -> Unit,
//  onNavigateToDetail: () -> Unit,
//  onNavigateBack: () -> Unit,
//  onExitApp: () -> Unit
) {

  val navController = newsNavController.navController
  val snackbarController = LocalSnackbarController.current
  val activity = LocalContext.current as? Activity

  SharedTransitionLayout {
    NavHost(
      navController = navController,
      startDestination = Route.NewsNavigation.route,
    ) {
      navigation(
        route = Route.NewsNavigation.route,
        startDestination = Route.HomeScreen.route
      ) {
        composable(route = Route.HomeScreen.route) { from ->
          val viewModel: HomeViewModel = hiltViewModel()
          val detailViewModel: DetailViewModel =
            from.hiltSharedViewModel(navController = navController)
          val items = viewModel.news.collectAsLazyPagingItems()
          
          CompositionLocalProvider(value = LocalAnimatedVisibilityScope provides this@composable) {
            HomeScreen(
              paddingValues = paddingValues,
              articles = items,
              navigateToSearch = newsNavController::navigateToBottomBarRoute,
              navigateToDetail = {
                detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
                detailViewModel.onEvent(DetailEvent.SetPrefixSharedKey("home"))
                newsNavController.navigateToDetail()
              },
              pullToRefreshLayoutState = viewModel.pullToRefreshState,
              onRefresh = {
                items.refresh()
              },
              onExitApp = {
                activity?.finish()
              }
            )
          }
        }

        composable(route = MaterialNavScreen.Search.route) { from ->
          val viewModel: SearchViewModel = hiltViewModel()
          val detailViewModel: DetailViewModel =
            from.hiltSharedViewModel(navController = navController)
          val state by viewModel.state

          CompositionLocalProvider(value = LocalAnimatedVisibilityScope provides this@composable) {
            SearchScreen(
              paddingValues = paddingValues,
              state = state,
              event = viewModel::onEvent,
              navigateToDetail = {
                detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
                detailViewModel.onEvent(DetailEvent.SetPrefixSharedKey("search"))
                newsNavController.navigateToDetail()
              }
            )
          }
        }

        composable(route = MaterialNavScreen.Bookmark.route) { from ->
          val viewModel: BookmarkViewModel = hiltViewModel()
          val detailViewModel: DetailViewModel =
            from.hiltSharedViewModel(navController = navController)
          val state by viewModel.state
          val context = LocalContext.current
          val detailViewState by detailViewModel.viewState.collectAsStateWithLifecycle()

          EventEffect(
            event = detailViewState.processSucceededEvent,
            onConsumed = detailViewModel::onConsumedSucceededEvent,
            action = {
//              snackbarController.showMessage(
//                NewsSnackbarVisual(
//                  message = it.asString(context)
//                ), onSnackbarResult = { result ->
//                  Log.e("TAG", "action run ${result.name}")
//                })
            })


          CompositionLocalProvider(value = LocalAnimatedVisibilityScope provides this@composable) {
            BookmarkScreen(paddingValues = paddingValues, state = state, navigateToDetails = {
              detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
              detailViewModel.onEvent(DetailEvent.SetPrefixSharedKey("bookmark"))
              newsNavController.navigateToDetail()
            }, onDelete = {
              detailViewModel.onEvent(DetailEvent.UpsertDeleteArticle(it))
            })
          }
        }

        composable(route = Route.DetailsScreen.route) { from ->
          val viewModel: DetailViewModel = from.hiltSharedViewModel(navController = navController)
          val context = LocalContext.current
          val viewState by viewModel.viewState.collectAsStateWithLifecycle()

          EventEffect(
            event = viewState.processSucceededEvent,
            onConsumed = viewModel::onConsumedSucceededEvent,
            action = {
              snackbarController.showMessage(
                NewsSnackbarVisual(
                  message = it.asString(context)
                ), onSnackbarResult = { result ->
                  Log.e("TAG", "action run ${result.name}")
                })
            })

          CompositionLocalProvider(value = LocalAnimatedVisibilityScope provides this@composable) {
            DetailScreen(
              prefixSharedKey = viewState.prefixSharedKey ?: "home",
              article = viewState.article,
              bookmarkArticle = viewState.bookmark,
              event = viewModel::onEvent,
              navigateUp = newsNavController::navigateBack,
            )
          }
        }
      }
    }
  }

}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.hiltSharedViewModel(
  navController: NavHostController,
): T {
  val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
  val parentEntry = remember(this) {
    navController.getBackStackEntry(navGraphRoute)
  }
  return hiltViewModel(parentEntry)
}
