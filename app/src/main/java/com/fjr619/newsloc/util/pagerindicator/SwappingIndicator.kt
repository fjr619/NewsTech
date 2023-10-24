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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwapDotIndicators(
  pagerState: PagerState,
//  count: Int,
//  offSet: Float,
  itemWidth: Dp = 10.dp,
  itemHeight: Dp = 10.dp,
  itemSpacing: Dp = 5.dp,
  selectedColor: Color = MaterialTheme.colorScheme.primary,
  unSelectedColor: Color = Color.LightGray,
) {

  Box(
    modifier = Modifier, contentAlignment = Alignment.Center
  ) {
    Canvas(
      modifier = Modifier.height(itemHeight)
    ) {
      val distance = (itemWidth + itemSpacing).toPx()

      val yPos = center.y

      repeat(pagerState.pageCount) { i ->
        val dotOffset = pagerState.pageOffset - pagerState.pageOffset.toInt()
        val current = pagerState.pageOffset.toInt()
        val color = if (i == current) selectedColor else unSelectedColor

        val moveX: Float = when {
          i == current -> pagerState.pageOffset
          i - 1 == current -> i - dotOffset
          else -> i.toFloat()
        }

        drawIndicator(moveX * distance, yPos, itemWidth.toPx(), itemHeight.toPx(), CornerRadius(20.dp.toPx(), 20.dp.toPx()), color)
      }
    }
  }
}

private fun DrawScope.drawIndicator(
  x: Float,
  y: Float,
  width: Float,
  height: Float,
  radius: CornerRadius,
  color: Color
) {
  val rect = RoundRect(
    x,
    y - height / 2,
    x + width,
    y + height / 2,
    radius
  )
  val path = Path().apply { addRoundRect(rect) }
  drawPath(path = path, color = color)
}
