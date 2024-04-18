package com.fjr619.newsloc.presentation.navgraph

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

@Composable
fun NewsGraph(
  paddingValues: PaddingValues,
  navController: NavHostController,
  onNavigateBottomBar: (MaterialNavScreen) -> Unit,
  onNavigateToDetail: () -> Unit,
  onNavigateBack: () -> Unit
) {

  val snackbarController = LocalSnackbarController.current

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

        HomeScreen(
          paddingValues = paddingValues,
          articles = viewModel.news.collectAsLazyPagingItems(),
          navigateToSearch = onNavigateBottomBar,
          navigateToDetail = {
            detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
            onNavigateToDetail()
          },
          pullToRefreshLayoutState = viewModel.pullToRefreshState,
          onRefresh = {
            viewModel.onEvent(HomeEvent.GetArticles)
          }
        )
      }

      composable(route = MaterialNavScreen.Search.route) { from ->
        val viewModel: SearchViewModel = hiltViewModel()
        val detailViewModel: DetailViewModel =
          from.hiltSharedViewModel(navController = navController)
        val state by viewModel.state

        SearchScreen(
          paddingValues = paddingValues,
          state = state,
          event = viewModel::onEvent,
          navigateToDetail = {
            detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
            onNavigateToDetail()
          }
        )
      }

      composable(route = MaterialNavScreen.Bookmark.route) { from ->
        val viewModel: BookmarkViewModel = hiltViewModel()
        val detailViewModel: DetailViewModel =
          from.hiltSharedViewModel(navController = navController)
        val state by viewModel.state
        BookmarkScreen(paddingValues = paddingValues, state = state, navigateToDetails = {
          detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
          onNavigateToDetail()
        })
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

        Surface(modifier = Modifier.fillMaxSize()) {
          DetailScreen(
            article = viewState.article,
            bookmarkArticle = viewState.bookmark,
            event = viewModel::onEvent,
            navigateUp = { onNavigateBack() },
          )
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
