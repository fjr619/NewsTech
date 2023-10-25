package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavRail(
  bottomBarState: BottomBarState,
  screens: List<BottomBarScreen>,
  onNavigateBottomBar: (BottomBarScreen) -> Unit,
  countBookmark: Int
) {
  NavigationRail(
    header = {
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
      }
      FloatingActionButton(onClick = { /*TODO*/ }, elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
      }
    }
  ) {

    val navHostController = bottomBarState.newsNavController.navController

    val shouldShowBottomBar =
      navHostController.currentBackStackEntryAsState().value?.destination?.route in screens.map {
        it.route
      }

    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
    ) {
      screens.forEachIndexed { index, screen ->
        val selected = bottomBarState.isSelected(screen).apply {
          if (this) bottomBarState.setCurrentIndex(index)
        }
        NavigationRailItem(
          selected = selected,
          onClick = { onNavigateBottomBar(screen) },
          icon = {
            BadgedBox(badge = {
              if (screen.hasBadge && countBookmark != 0)
                Badge {
                  Text(text = "$countBookmark")
                }
            }) {
              Icon(
                modifier = Modifier.size(20.dp),
                imageVector = screen.icon,
                contentDescription = null
              )
            }
          },
          label = {
            Text(text = screen.title, style = MaterialTheme.typography.labelSmall)
          }
        )
      }
    }

  }
}

