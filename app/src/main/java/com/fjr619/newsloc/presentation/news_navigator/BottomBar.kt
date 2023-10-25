package com.fjr619.newsloc.presentation.news_navigator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.presentation.navgraph.Route

sealed class BottomBarScreen(
  val route: String,
  val title: String,
  val icon: ImageVector,
  val hasBadge: Boolean,
  val count: Int = 0
) {
  data object Home : BottomBarScreen(
    Route.HomeScreen.route, "Home", Icons.Outlined.Home, hasBadge = false
  )

  data object Search : BottomBarScreen(
    Route.SearchScreen.route, "Search", Icons.Outlined.Search, hasBadge = false
  )

  data object Bookmark : BottomBarScreen(
    Route.BookmarkScreen.route, "Bookmark", Icons.Outlined.FavoriteBorder, hasBadge = true
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
//  newsNavController: NewsNavController,
  bottomBarState: BottomBarState,
  screens: List<BottomBarScreen>,
  onNavigateBottomBar: (BottomBarScreen) -> Unit,
  countBookmark: Int
) {

  if (bottomBarState.showNavigation {
      screens
    }) {
    NavigationBar(
      modifier = Modifier
        .fillMaxWidth()
    ) {

      screens.forEachIndexed { index, screen ->
        val selected = bottomBarState.isSelected(screen).apply {
          if (this) bottomBarState.setCurrentIndex(index)
        }

        NavigationBarItem(
          selected = selected,
          icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
              Spacer(modifier = Modifier.height(6.dp))
              Text(text = screen.title, style = MaterialTheme.typography.labelSmall)
            }
          },
          onClick = {
            onNavigateBottomBar(screen)
          }
        )

      }
    }
  }
}