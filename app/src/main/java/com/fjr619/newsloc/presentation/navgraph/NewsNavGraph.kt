package com.fjr619.newsloc.presentation.navgraph

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.bookmark.BookmarkScreen
import com.fjr619.newsloc.presentation.bookmark.BookmarkViewModel
import com.fjr619.newsloc.presentation.detail.DetailScreen
import com.fjr619.newsloc.presentation.detail.DetailViewModel
import com.fjr619.newsloc.presentation.home.HomeScreen
import com.fjr619.newsloc.presentation.home.HomeViewModel
import com.fjr619.newsloc.presentation.mainactivity.MainActivity
import com.fjr619.newsloc.presentation.news_navigator.BottomBarScreen
import com.fjr619.newsloc.presentation.search.SearchScreen
import com.fjr619.newsloc.presentation.search.SearchViewModel
import com.fjr619.newsloc.util.UiEffect
import com.fjr619.newsloc.util.UiText
import dagger.hilt.android.EntryPointAccessors

@Composable
fun NewsGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    onItemClick: (BottomBarScreen) -> Unit
) {
    NavHost(
        navController = navController,
        route = Route.NewsNavigation.route,
        startDestination = Route.HomeScreen.route,
    ) {

        composable(route = Route.HomeScreen.route) { navBackEntry ->
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                paddingValues = paddingValues,
                articles = viewModel.news.collectAsLazyPagingItems(),
                navigateToSearch = {
                    onItemClick(BottomBarScreen.Search)
                },
                navigateToDetail = {
                    navigateToDetails(navController, it)
                })
        }

        composable(route = BottomBarScreen.Search.route) {
            val viewModel: SearchViewModel = hiltViewModel()
            val state by viewModel.state

            SearchScreen(
                state = state,
                event = viewModel::onEvent,
                navigateToDetail = {
                    navigateToDetails(navController, it)
                }
            )
        }

        composable(route = Route.DetailsScreen.route,) {
            val context = LocalContext.current
            val article = remember {
                navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
            }

//            it.savedStateHandle["aticle"] = article

            val factory = remember {
                derivedStateOf {
                    EntryPointAccessors.fromActivity(
                        context as Activity, MainActivity.ViewModelFactoryProvider::class.java
                    ).detailViewModelFactory()
                }

            }

            val viewModel: DetailViewModel = viewModel(
                factory = DetailViewModel.provideFactory(factory.value, article)
            )

            val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(
                initialValue = UiEffect.None()
            )

            article?.let {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DetailScreen(
                        article = it,
                        bookmarkArticle = viewModel.bookmarkArticle.value,
                        event = viewModel::onEvent,
                        navigateUp = { navController.popBackStack() },
                        sideEffect = sideEffect
                    )
                }
            }
        }

        composable(route = BottomBarScreen.Bookmark.route) {
            val viewModel: BookmarkViewModel = hiltViewModel()
            val state by viewModel.state
            BookmarkScreen(paddingValues = paddingValues, state = state, navigateToDetails = { article ->
                navigateToDetails(
                    navController = navController, article = article
                )
            })
        }
    }
}

private fun navigateToDetails(navController: NavHostController, article: Article) {
    navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
    navController.navigate(
        route = Route.DetailsScreen.route
    )
}