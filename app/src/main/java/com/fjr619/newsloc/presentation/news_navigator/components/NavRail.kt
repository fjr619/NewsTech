package com.fjr619.newsloc.presentation.news_navigator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.presentation.navgraph.NewsNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavRail(
  newsNavController: NewsNavController,
  screens: List<MaterialNavScreen>,
  onNavigateBottomBar: (MaterialNavScreen) -> Unit,
  countBookmark: Int
) {
  NavigationRail {

    Column(
      modifier = Modifier.fillMaxHeight(),
      verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
    ) {
      screens.forEach { screen ->
        val selected = newsNavController.isSelected(screen)

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

