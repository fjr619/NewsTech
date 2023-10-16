package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.presentation.NavGraphs
import com.fjr619.newsloc.presentation.destinations.DetailScreenDestination
import com.fjr619.newsloc.presentation.destinations.DetailScreenFromSearchDestination
import com.fjr619.newsloc.presentation.destinations.HomeScreenDestination
import com.fjr619.newsloc.presentation.destinations.SearchScreenDestination
import com.fjr619.newsloc.presentation.home.HomeScreen
import com.fjr619.newsloc.presentation.home.HomeViewModel
import com.fjr619.newsloc.presentation.search.SearchScreen
import com.fjr619.newsloc.presentation.search.SearchViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.manualcomposablecalls.composable


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun NewsNavigator() {
      val navController = rememberNavController()
//      val newsNavController = rememberNewsNavController(navController)
//      val bottomBarState = rememberBottomBarState(
//            newsNavController = newsNavController,
//            navBackStackEntry = newsNavController.navController.currentBackStackEntryAsState(),
//      )

      val screens = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Search,
//        BottomBarScreen.Bookmark
      )

      Scaffold(
            bottomBar = {
                  BottomBar(
                        navController = navController,
                        screens = screens,
                  )
            }
      ) {

            DestinationsNavHost(
                  navGraph = NavGraphs.news,
                  navController = navController
            ) {
                  composable(HomeScreenDestination) {
                        val viewModel: HomeViewModel = hiltViewModel()
                        HomeScreen(paddingValues = it, articles = viewModel.news.collectAsLazyPagingItems(), navigateToSearch = {}, navigateToDetail = {
                              destinationsNavigator.navigate(DetailScreenDestination(article = it))
                        })
                  }


                  composable(SearchScreenDestination) {
                        val viewModel: SearchViewModel = hiltViewModel()
                        SearchScreen(
                              state = viewModel.state.value,
                              event = viewModel::onEvent,
                              navigateToDetail = {
                                    destinationsNavigator.navigate(DetailScreenFromSearchDestination(article = it))
                              }
                        )
                  }
            }



//        NewsGraph(
//            paddingValues = it,
//            navController = newsNavController.navController,
//            onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute,
//            onNavigateToDetail = newsNavController::navigateToDetail
//        )
      }
}
