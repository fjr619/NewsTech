package com.fjr619.newsloc.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.fjr619.newsloc.presentation.NavGraphs
import com.fjr619.newsloc.presentation.destinations.NewsNavigatorDestination
import com.fjr619.newsloc.presentation.destinations.OnboardingScreenDestination
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigator
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.spec.Route

@Composable
fun RootNavGraph(
    startDestination: Route,
) {

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        startRoute = startDestination
    ) {
        composable(OnboardingScreenDestination) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(onEvent =  viewModel::onEvent)
        }

        composable(NewsNavigatorDestination) {
            NewsNavigator()
        }

//        composable(HomeScreenDestination) {
//            val viewModel: HomeViewModel = hiltViewModel()
//            destinationsNavigator.navigate(HomeScreenDestination())
//            HomeScreen(
//                paddingValues = PaddingValues(),
//                articles = viewModel.news.collectAsLazyPagingItems(),
//                navigateToSearch = {},
//                navigateToDetail = {}
//            )
//        }
    }

//    NavHost(
//        navController = navController,
//        route = Route.RootNavigation.route,
//        startDestination = startDestination
//    ) {
//        navigation(
//            route = Route.AppStartNavigation.route,
//            startDestination = Route.OnBoardingScreen.route
//        ) {
//            composable(route = Route.OnBoardingScreen.route) {
//                val viewModel: OnboardingViewModel = hiltViewModel()
//                OnboardingScreen(onEvent =  viewModel::onEvent)
//            }
//        }
//
//        composable(
//            route = Route.NewsNavigation.route
//        ) {
//            NewsNavigator()
//        }
//    }
}

