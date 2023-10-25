package com.fjr619.newsloc.presentation.news_navigator.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.presentation.news_navigator.MaterialNavScreen
import com.fjr619.newsloc.presentation.news_navigator.MaterialNavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermanentNavDrawer(
  materialNavigationState: MaterialNavigationState,
  screens: List<MaterialNavScreen>,
  onNavigateBottomBar: (MaterialNavScreen) -> Unit,
  countBookmark: Int
) {
  PermanentDrawerSheet(
    modifier = Modifier.sizeIn(minWidth = 150.dp, maxWidth = 230.dp),
  ) {
    screens.forEachIndexed { index, screen ->
      val selected = materialNavigationState.isSelected(screen).apply {
        if (this) materialNavigationState.setCurrentIndex(index)
      }

      NavigationDrawerItem(
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