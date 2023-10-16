package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.fjr619.newsloc.presentation.NavGraphs
import com.fjr619.newsloc.presentation.appCurrentDestinationAsState
import com.fjr619.newsloc.presentation.destinations.TypedDestination
import com.fjr619.newsloc.presentation.news_navigator.components.AnimatedBottomNavigationItem
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec

sealed class BottomBarScreen(
    val route: NavGraphSpec,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomBarScreen(
       NavGraphs.home , "Home", Icons.Outlined.Home
    )

    data object Search : BottomBarScreen(
       NavGraphs.search, "Search", Icons.Outlined.Search
    )

//    data object Bookmark : BottomBarScreen(
//        NavGraphs., "Bookmark", Icons.Outlined.FavoriteBorder
//    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    screens: List<BottomBarScreen>,
) {


//    val shouldShowBottomBar =
//        navHostController.currentBackStackEntryAsState().value?.destination?.route in screens.map {
//            it.route
//        }

//    if (shouldShowBottomBar) {
        Box {
//            val offsetAnim by animateFloatAsState(
//                targetValue = when (bottomBarState.currentIndex) {
//                    1 -> bottomBarState.width.toFloat() / 3
//                    2 -> (bottomBarState.width.toFloat() / 3) * 2
//                    else -> 0f
//                }, label = ""
//            )

            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .onGloballyPositioned {
//                        bottomBarState.setWidth(it.size.width.toFloat())
                    }
            ) {

                screens.forEachIndexed { index, screen ->
//                    val selected = bottomBarState.isSelected(screen).apply {
//                        if (this) bottomBarState.setCurrentIndex(index)
//                    }

                      val currentDestination  = navController.appCurrentDestinationAsState().value


//                      Log.e("TAG", "currentDestination ${currentDestination?.route}")

                    AddItem(
                        screen = screen,
                          currentDestination =  currentDestination,
                        onNavigateBottomBar = {
                              navController.navigate(screen.route)  {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                          saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                              }
//                            bottomBarState.setCurrentIndex(index)
//                            onNavigateBottomBar(it)
                        }
                    )
                }
            }

//            Box(
//                modifier = Modifier
//                    .width(with(LocalDensity.current) {
//                        bottomBarState.width
//                            .toFloat()
//                            .toDp() / 3
//                    })
//                    .height(3.dp)
//                    .offset(with(LocalDensity.current) { offsetAnim.toDp() }, 0.dp)
//                    .clip(RoundedCornerShape(5.dp))
//                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
//            )
        }

    }
//}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: TypedDestination<out Any?>?,
    onNavigateBottomBar: (BottomBarScreen) -> Unit
) {

    AnimatedBottomNavigationItem(
        modifier = Modifier.height(70.dp),
        selected =  screen.route.destinationsByRoute.entries.any {
              it.value.route == currentDestination?.route
        },
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