package com.fjr619.newsloc.domain.preferences.navgraph

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.detail.DetailScreen
import com.fjr619.newsloc.presentation.home.HomeScreen
import com.fjr619.newsloc.presentation.home.HomeViewModel
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel
import com.fjr619.newsloc.presentation.search.SearchScreen
import com.fjr619.newsloc.presentation.search.SearchViewModel
import com.fjr619.newsloc.util.AnimationTransition

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    Log.e("TAG", "startDestination $startDestination")
    NavHost(navController = navController, startDestination = startDestination) {
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.OnBoardingScreen.route
        ) {
            composable(route = Route.OnBoardingScreen.route) {
                val viewModel: OnboardingViewModel = hiltViewModel()
                OnboardingScreen(viewModel::onEvent)
            }
        }

        navigation(
            route = Route.NewsNavigation.route,
            startDestination = Route.HomeScreen.route,
        ) {
            composable(
                route = Route.HomeScreen.route,
                enterTransition = AnimationTransition.fadeIn(300),
                exitTransition = AnimationTransition.fadeOut(300),
                popEnterTransition = AnimationTransition.fadeIn(300),
                popExitTransition = AnimationTransition.fadeOut(300),
            ) { navBackEntry ->
                val viewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    articles = viewModel.news.collectAsLazyPagingItems(),
                    navigate = {
                        navController.navigate(it) {
                            launchSingleTop = true
                        }
                    },
                    navigateToDetail = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                        navController.navigate(Route.DetailsScreen.route)
                    })
            }

            composable(
                route = Route.SearchScreen.route,
                enterTransition = AnimationTransition.slideInLeft(300),
                exitTransition = AnimationTransition.slideOutLeft(300),
                popEnterTransition = AnimationTransition.slideInRight(300),
                popExitTransition = AnimationTransition.slideOutRight(300)
            ) {
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.value

                SearchScreen(
                    state = state,
                    event = viewModel::onEvent,
                    navigateToDetail = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
                        navController.navigate(Route.DetailsScreen.route)
                    }
                )
            }


            composable(route = Route.BookmarkScreen.route) {
            }


            composable(
                route = Route.DetailsScreen.route,
                enterTransition = AnimationTransition.fadeIn(300),
                exitTransition = AnimationTransition.fadeOut(300),
                popEnterTransition = AnimationTransition.fadeIn(300),
                popExitTransition = AnimationTransition.fadeOut(300),
            ) {
                val article = remember {
                    navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
                }

                article?.let {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        DetailScreen(
                            article = it,
                            event = {},
                            navigateUp = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

