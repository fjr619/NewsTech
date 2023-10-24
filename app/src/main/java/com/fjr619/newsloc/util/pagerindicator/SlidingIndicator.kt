package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlidingIndicator(
  pagerState: PagerState,
  itemSpacing: Dp = 5.dp,
  itemHeight: Dp = 5.dp,
  itemWidth: Dp = 20.dp,
  itemRadius: Dp = 5.dp,

  selectedColor: Color = MaterialTheme.colorScheme.primary,
  unselectedColor: Color = Color.LightGray,
) {
  Box(
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(itemSpacing),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      repeat(pagerState.pageCount) {
        Box(
          modifier = Modifier
            .size(width = itemWidth, height = itemHeight)
            .background(
              color = unselectedColor,
              shape = RoundedCornerShape(itemRadius)
            )
        )
      }
    }

    Box(
      Modifier
        .slidingLineTransition(pagerState.pageOffset, (itemWidth + itemSpacing).ToPx())
        .size(width = itemWidth, height = itemHeight)
        .background(
          color = selectedColor,
          shape = RoundedCornerShape(itemRadius),
        )
    )
  }
}

private fun Modifier.slidingLineTransition(pageOffset: Float, distance: Float): Modifier =
  graphicsLayer {
    translationX = pageOffset * distance
  }