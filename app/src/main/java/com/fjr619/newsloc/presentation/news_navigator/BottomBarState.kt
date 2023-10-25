package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.navgraph.NewsNavController
import com.fjr619.newsloc.presentation.navgraph.rememberNewsNavController

@Composable
fun rememberBottomBarState(
    currentIndex: Int = 0,
    newsNavController: NewsNavController = rememberNewsNavController(),
    navBackStackEntry: State<NavBackStackEntry?>
): BottomBarState {
    return rememberSaveable(saver = BottomBarState.saver(navBackStackEntry, newsNavController)) {
        BottomBarState(currentIndex, newsNavController, navBackStackEntry)
    }
}

@Stable
class BottomBarState(
    currentIndex: Number,
    val newsNavController: NewsNavController,
    val navBackStackEntry: State<NavBackStackEntry?>,
) {
    var currentIndex by mutableStateOf(currentIndex)
        private set

    fun setCurrentIndex(curIndex: Int) {
        this.currentIndex = curIndex
    }

    fun isSelected(screen: BottomBarScreen): Boolean {
        return navBackStackEntry.value?.destination?.hierarchy?.any {
            it.route == screen.route
        } == true
    }

    fun navigateToBottomBarRoute(bottomBarScreen: BottomBarScreen) {
        newsNavController.navigateToBottomBarRoute(bottomBarScreen)
    }

    @Composable
    fun showNavigation(list: () -> List<BottomBarScreen>): Boolean {
        return newsNavController.navController.currentBackStackEntryAsState().value?.destination?.route in list().map {
            it.route
        }
    }

    companion object {
        fun saver(
            navBackStackEntry: State<NavBackStackEntry?>,
            newsNavController: NewsNavController
        ): Saver<BottomBarState, *> = listSaver(
            save = {
                listOf(/*it.width,*/ it.currentIndex)
            },
            restore = {
                BottomBarState(
                    /*width = it[0],*/
                    currentIndex = it[0],
                    navBackStackEntry = navBackStackEntry,
                    newsNavController = newsNavController
                )
            }
        )
    }
}