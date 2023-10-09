package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.navgraph.NewsNavController
import com.fjr619.newsloc.presentation.navgraph.Route

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBarScreen(
        Route.HomeScreen.route, "Home", Icons.Outlined.Home
    )

    data object Search : BottomBarScreen(
        Route.SearchScreen.route, "Search", Icons.Outlined.Search
    )

    data object Bookmark : BottomBarScreen(
        Route.BookmarkScreen.route, "Bookmark", Icons.Outlined.FavoriteBorder
    )
}

@Composable
fun BottomBar(
    navHostController: NavHostController,
    screens: List<BottomBarScreen>,
    onNavigateBottomBar: (BottomBarScreen) -> Unit
) {

    val shouldShowBottomBar =
        navHostController.currentBackStackEntryAsState().value?.destination?.route in screens.map {
            it.route
        }

    if (shouldShowBottomBar) {
        NavigationBar {
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    onNavigateBottomBar = onNavigateBottomBar
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    onNavigateBottomBar: (BottomBarScreen) -> Unit
) {
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = screen.icon,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = screen.title, style = MaterialTheme.typography.labelSmall)
            }
        },
        onClick = { onNavigateBottomBar(screen) }
    )
}