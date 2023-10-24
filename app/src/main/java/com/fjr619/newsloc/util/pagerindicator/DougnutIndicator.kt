package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DougnutIndicator(
  itemSize: Dp = 10.dp,
  itemOuterSize: Dp = 14.dp,
  itemSpacing: Dp = 5.dp,
  pagerState: PagerState,
  selectedColor: Color = MaterialTheme.colorScheme.primary,
  innerColor: Color = Color.White,
) {

  Box(
    modifier = Modifier, contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.height(itemOuterSize)) {
      val distance = (itemSize + itemSpacing).toPx()
      val centerY = itemSize.toPx() / 2
      val startX = itemSpacing.toPx()

      repeat(pagerState.pageCount) {
        val pageOffset = pagerState.calculateCurrentOffsetForPage(it)

        val alpha = 0.8f.coerceAtLeast(1 - pageOffset.absoluteValue)
        val scale = 1f.coerceAtMost(pageOffset.absoluteValue)

        val x = startX + (it * distance)
        val circleCenter = Offset(x, centerY)
        val innerRadius = (itemSize.toPx() * scale) / 2
        val radius = itemOuterSize.toPx() / 2

        drawCircle(
          color = selectedColor,
          center = circleCenter,
          radius = radius, alpha = alpha,
        )

        drawCircle(
          color = innerColor,
          center = circleCenter,
          radius = innerRadius)
      }
    }
  }
}
