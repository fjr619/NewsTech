package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator() {
    val newsNavController = rememberNewsNavController()
    val bottomBarState = rememberBottomBarState(
        newsNavController = newsNavController,
        navBackStackEntry = newsNavController.navController.currentBackStackEntryAsState(),
    )

    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Bookmark
    )

    Scaffold(
        bottomBar = {
            BottomBar(
                newsNavController = newsNavController,
                bottomBarState = bottomBarState,
                screens = screens,
                onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute
            )
        }
    ) {
        NewsGraph(
            paddingValues = it,
            navController = newsNavController.navController,
            onNavigateBottomBar = bottomBarState::navigateToBottomBarRoute,
            onNavigateToDetail = newsNavController::navigateToDetail
        )
    }
}
