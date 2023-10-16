//package com.fjr619.newsloc.presentation.navgraph
//
//import android.app.Activity
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavBackStackEntry
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.paging.compose.collectAsLazyPagingItems
//import com.fjr619.newsloc.domain.model.Article
//import com.fjr619.newsloc.presentation.bookmark.BookmarkScreen
//import com.fjr619.newsloc.presentation.bookmark.BookmarkViewModel
//import com.fjr619.newsloc.presentation.detail.DetailScreen
//import com.fjr619.newsloc.presentation.detail.DetailViewModel
//import com.fjr619.newsloc.presentation.home.HomeScreen
//import com.fjr619.newsloc.presentation.home.HomeViewModel
//import com.fjr619.newsloc.presentation.mainactivity.MainActivity
//import com.fjr619.newsloc.presentation.news_navigator.BottomBarScreen
//import com.fjr619.newsloc.presentation.search.SearchScreen
//import com.fjr619.newsloc.presentation.search.SearchViewModel
//import com.fjr619.newsloc.util.UiEffect
//import dagger.hilt.android.EntryPointAccessors
//
//@Composable
//fun NewsGraph(
//    paddingValues: PaddingValues,
//    navController: NavHostController,
//    onNavigateBottomBar: (BottomBarScreen) -> Unit,
//    onNavigateToDetail: (Article, NavBackStackEntry) -> Unit
//) {
//    NavHost(
//        navController = navController,
//        route = Route.NewsNavigation.route,
//        startDestination = Route.HomeScreen.route,
//    ) {
//
//        composable(route = Route.HomeScreen.route) { from ->
//            val viewModel: HomeViewModel = hiltViewModel()
//            HomeScreen(
//                paddingValues = paddingValues,
//                articles = viewModel.news.collectAsLazyPagingItems(),
//                navigateToSearch = onNavigateBottomBar,
//                navigateToDetail = {
//                    onNavigateToDetail(it, from)
//                }
//            )
//        }
//
//        composable(route = BottomBarScreen.Search.route) { from ->
//            val viewModel: SearchViewModel = hiltViewModel()
//            val state by viewModel.state
//
//            SearchScreen(
//                state = state,
//                event = viewModel::onEvent,
//                navigateToDetail = {
//                    onNavigateToDetail(it, from)
//                }
//            )
//        }
//
//        composable(route = Route.DetailsScreen.route,) {
//            val context = LocalContext.current
//            val article = remember {
//                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
//            }
//
//            val factory = remember {
//                derivedStateOf {
//                    EntryPointAccessors.fromActivity(
//                        context as Activity, MainActivity.ViewModelFactoryProvider::class.java
//                    ).detailViewModelFactory()
//                }
//
//            }
//
//            val viewModel: DetailViewModel = viewModel(
//                factory = DetailViewModel.provideFactory(factory.value, article)
//            )
//
//            val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(
//                initialValue = UiEffect.None()
//            )
//
//            article?.let {
//                Surface(modifier = Modifier.fillMaxSize()) {
//                    DetailScreen(
//                        article = it,
//                        bookmarkArticle = viewModel.bookmarkArticle.value,
//                        event = viewModel::onEvent,
//                        navigateUp = { navController.popBackStack() },
//                        sideEffect = sideEffect
//                    )
//                }
//            }
//        }
//
//        composable(route = BottomBarScreen.Bookmark.route) {from ->
//            val viewModel: BookmarkViewModel = hiltViewModel()
//            val state by viewModel.state
//            BookmarkScreen(paddingValues = paddingValues, state = state, navigateToDetails = {
//                onNavigateToDetail(it, from)
//            })
//        }
//    }
//}