package com.fjr619.newsloc.presentation.navgraph

import androidx.navigation.NamedNavArgument

const val OnBoardingScreen = "onBoardingScreen"

sealed class Route(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object OnBoardingScreen : Route(route = "onBoardingScreen")

    object HomeScreen : Route(route = "homeScreen")

    object SearchScreen : Route(route = "searchScreen")

    object BookmarkScreen : Route(route = "bookMarkScreen")

    object DetailsScreen : Route(route = "detailsScreen")

    object AppStartNavigation : Route(route = "appStartNavigation")

    object NewsNavigation : Route(route = "newsNavigation")

    object RootNavigation : Route(route = "rootNavigation")
}