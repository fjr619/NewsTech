package com.fjr619.newsloc.presentation.news_navigator

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fjr619.newsloc.presentation.navgraph.NewsGraph
import com.fjr619.newsloc.presentation.navgraph.Route

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewsNavigator() {
    val navController = rememberNavController()

    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Bookmark
    )

    Scaffold(
        bottomBar = {
            BottomBar(navHostController = navController, screens = screens, onItemClick = {
                navigateToTab(navController, it)
            })
        }
    ) {
        NewsGraph(paddingValues = it, navController = navController) { bottomBarScreen ->
            navigateToTab(navController, bottomBarScreen)
        }
    }
}

private fun navigateToTab(navHostController: NavHostController, bottomBarScreen: BottomBarScreen) {
    navHostController.navigate(bottomBarScreen.route) {
        popUpTo(navHostController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

    }
}
