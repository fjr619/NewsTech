package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator() {
    val newsNavController = rememberNewsNavController()

    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Bookmark
    )

    Scaffold(
        bottomBar = {
            BottomBar(
                navHostController = newsNavController.navController,
                screens = screens,
                onNavigateBottomBar = newsNavController::navigateToBottomBarRoute
            )
        }
    ) {
        NewsGraph(
            paddingValues = it,
            navController = newsNavController.navController,
            onNavigateBottomBar = newsNavController::navigateToBottomBarRoute,
            onNavigateToDetail = newsNavController::navigateToDetail
        )
    }
}
