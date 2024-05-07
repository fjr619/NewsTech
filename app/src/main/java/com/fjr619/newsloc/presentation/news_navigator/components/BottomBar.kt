package com.fjr619.newsloc.presentation.news_navigator.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.presentation.navgraph.NewsNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
  newsNavController: NewsNavController,
  screens: List<MaterialNavScreen>,
  onNavigateBottomBar: (MaterialNavScreen) -> Unit,
  countBookmark: Int
) {

  if (newsNavController.showNavigation { screens }) {
    NavigationBar(
      modifier = Modifier
        .fillMaxWidth()
    ) {

      screens.forEach { screen ->
        val selected = newsNavController.isSelected(screen)
        
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