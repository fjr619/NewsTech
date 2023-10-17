package com.fjr619.newsloc.presentation.navgraph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.fjr619.newsloc.presentation.detail.DetailEvent
import com.fjr619.newsloc.presentation.detail.DetailScreen
import com.fjr619.newsloc.presentation.detail.DetailViewModel
import com.fjr619.newsloc.presentation.home.HomeScreen
import com.fjr619.newsloc.presentation.home.HomeViewModel
import com.fjr619.newsloc.presentation.news_navigator.BottomBarScreen
import com.fjr619.newsloc.presentation.search.SearchScreen
import com.fjr619.newsloc.presentation.search.SearchViewModel
import com.fjr619.newsloc.util.UiEffect

@Composable
fun NewsGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onNavigateBottomBar: (BottomBarScreen) -> Unit,
    onNavigateToDetail: (NavBackStackEntry) -> Unit
) {
    NavHost(
        navController = navController,
//        route = Route.NewsNavigation.route,
        startDestination = Route.NewsNavigation.route,
    ) {

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.HomeScreen.route
        ) {
            composable(route = Route.HomeScreen.route) { from ->
                val viewModel: HomeViewModel = hiltViewModel()
                val detailViewModel: DetailViewModel = from.hiltSharedViewModel(navController = navController)
                HomeScreen(
                    paddingValues = paddingValues,
                    articles = viewModel.news.collectAsLazyPagingItems(),
                    navigateToSearch = onNavigateBottomBar,
                    navigateToDetail = {
                        detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
                        onNavigateToDetail(from)
                    }
                )
            }

            composable(route = BottomBarScreen.Search.route) { from ->
                val viewModel: SearchViewModel = hiltViewModel()
                val detailViewModel: DetailViewModel = from.hiltSharedViewModel(navController = navController)
                val state by viewModel.state

                SearchScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetail = {
                        detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
                        onNavigateToDetail(from)
                    }
                )
            }

            composable(route = BottomBarScreen.Bookmark.route) { from ->
                val viewModel: BookmarkViewModel = hiltViewModel()
                val detailViewModel: DetailViewModel = from.hiltSharedViewModel(navController = navController)
                val state by viewModel.state
                BookmarkScreen(paddingValues = paddingValues, state = state, navigateToDetails = {
                    detailViewModel.onEvent(DetailEvent.GetDetailArticle(it))
                    onNavigateToDetail(from)
                })
            }

            composable(route = Route.DetailsScreen.route) { from ->
                val viewModel: DetailViewModel = from.hiltSharedViewModel(navController = navController)

//                val context = LocalContext.current
//                val article = remember {
//                    navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
//                }
//
//                val factory = remember {
//                    derivedStateOf {
//                        EntryPointAccessors.fromActivity(
//                            context as Activity, MainActivity.ViewModelFactoryProvider::class.java
//                        ).detailViewModelFactory()
//                    }
//
//                }
//
//                val viewModel: DetailViewModel = viewModel(
//                    factory = DetailViewModel.provideFactory(factory.value, article)
//                )
//
                val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(
                    initialValue = UiEffect.None()
                )
//
                viewModel.article.collectAsStateWithLifecycle().value?.let {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        DetailScreen(
                            article = it,
                            bookmarkArticle = viewModel.bookMarkArticle.collectAsStateWithLifecycle().value,
                            event = viewModel::onEvent,
                            navigateUp = { navController.popBackStack() },
                            sideEffect = sideEffect
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
