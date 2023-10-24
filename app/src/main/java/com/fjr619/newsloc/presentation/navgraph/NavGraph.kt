package com.fjr619.newsloc.presentation.navgraph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigator
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigatorViewModel
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    Log.e("TAG", "startDestination $startDestination")
    NavHost(
        navController = navController,
        route = Route.RootNavigation.route,
        startDestination = startDestination
    ) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnboardingViewModel = hiltViewModel()
                OnboardingScreen(viewModel::onEvent)
            }
        }

        composable(
            route = Route.NewsNavigation.route
        ) {
            val viewModel: NewsNavigatorViewModel = hiltViewModel()
            val countBookmark by viewModel.state
            NewsNavigator(countBookmark = countBookmark.articles.size)
        }

//        navigation(
//            route = Route.NewsNavigation.route,
//            startDestination = Route.HomeScreen.route,
//        ) {
//            composable(
//                route = Route.HomeScreen.route,
//                enterTransition = AnimationTransition.fadeIn(300),
//                exitTransition = AnimationTransition.fadeOut(300),
//                popEnterTransition = AnimationTransition.fadeIn(300),
//                popExitTransition = AnimationTransition.fadeOut(300),
//            ) { navBackEntry ->
//                val viewModel: HomeViewModel = hiltViewModel()
//                HomeScreen(
//                    articles = viewModel.news.collectAsLazyPagingItems(),
//                    navigate = {
//                        navController.navigate(it) {
//                            launchSingleTop = true
//                        }
//                    },
//                    navigateToDetail = {
//                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
//                        navController.navigate(Route.DetailsScreen.route)
//                    })
//            }
//
//            composable(
//                route = Route.SearchScreen.route,
//                enterTransition = AnimationTransition.slideInLeft(300),
//                exitTransition = AnimationTransition.slideOutLeft(300),
//                popEnterTransition = AnimationTransition.slideInRight(300),
//                popExitTransition = AnimationTransition.slideOutRight(300)
//            ) {
//                val viewModel: SearchViewModel = hiltViewModel()
//                val state = viewModel.state.value
//
//                SearchScreen(
//                    state = state,
//                    event = viewModel::onEvent,
//                    navigateToDetail = {
//                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
//                        navController.navigate(Route.DetailsScreen.route)
//                    }
//                )
//            }
//
//
//            composable(route = Route.BookmarkScreen.route) {
//            }
//
//
//            composable(
//                route = Route.DetailsScreen.route,
//                enterTransition = AnimationTransition.fadeIn(300),
//                exitTransition = AnimationTransition.fadeOut(300),
//                popEnterTransition = AnimationTransition.fadeIn(300),
//                popExitTransition = AnimationTransition.fadeOut(300),
//            ) {
//                val article = remember {
//                    navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
//                }
//
//                it.savedStateHandle.set("aticle", article)
//
//                val viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(newsUseCases = newsUseCases, article = article))
//
//                article?.let {
//                    Surface(modifier = Modifier.fillMaxSize()) {
//                        DetailScreen(
//                            article = it,
//                            bookmarkArticle = viewModel.bookmarkArticle.value,
//                            event = viewModel::onEvent,
//                            navigateUp = { navController.popBackStack() },
//                            sideEffect = viewModel.sideEffect.collectAsStateWithLifecycle(
//                                initialValue = UIComponent.None()
//                            ).value
//                        )
//                    }
//                }
//            }
//        }
    }
}

