package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fjr619.newsloc.presentation.navgraph.NewsNavController
import com.fjr619.newsloc.presentation.navgraph.Route
import com.fjr619.newsloc.presentation.news_navigator.components.AnimatedBottomNavigationItem

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
    newsNavController: NewsNavController,
    bottomBarState: BottomBarState,
    screens: List<BottomBarScreen>,
    onNavigateBottomBar: (BottomBarScreen) -> Unit
) {

    val navHostController = newsNavController.navController

    val shouldShowBottomBar =
        navHostController.currentBackStackEntryAsState().value?.destination?.route in screens.map {
            it.route
        }

    if (shouldShowBottomBar) {
        Box {
            val offsetAnim by animateFloatAsState(
                targetValue = when (bottomBarState.currentIndex) {
                    1 -> bottomBarState.width.toFloat() / 3
                    2 -> (bottomBarState.width.toFloat() / 3) * 2
                    else -> 0f
                }, label = ""
            )

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .onGloballyPositioned {
                        bottomBarState.setWidth(it.size.width.toFloat())
                    }
            ) {

                screens.forEachIndexed { index, screen ->
                    val selected = bottomBarState.isSelected(screen).apply {
                        if (this) bottomBarState.setCurrentIndex(index)
                    }

                    AddItem(
                        screen = screen,
                        selected = selected,
                        onNavigateBottomBar = {
                            bottomBarState.setCurrentIndex(index)
                            onNavigateBottomBar(it)
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(with(LocalDensity.current) {
                        bottomBarState.width
                            .toFloat()
                            .toDp() / 3
                    })
                    .height(3.dp)
                    .offset(with(LocalDensity.current) { offsetAnim.toDp() }, 0.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            )
        }

    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    selected: Boolean,
    onNavigateBottomBar: (BottomBarScreen) -> Unit
) {

    AnimatedBottomNavigationItem(
        modifier = Modifier.height(70.dp),
        selected = selected,
        onClick = { onNavigateBottomBar(screen) },
        icon = screen.icon,
        label = screen.title
    )

//    NavigationBarItem(
//        selected = selected,
//        icon = {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Icon(
//                    modifier = Modifier.size(20.dp),
//                    imageVector = screen.icon,
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//                Text(text = screen.title, style = MaterialTheme.typography.labelSmall)
//            }
//        },
//        onClick = { onNavigateBottomBar(screen)
//        }
//    )
}