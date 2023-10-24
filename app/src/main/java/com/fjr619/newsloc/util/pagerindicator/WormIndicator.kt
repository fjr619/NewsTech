package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WormIndicator(
  pagerState: PagerState,
  modifier: Modifier = Modifier,
  selectedColor: Color = MaterialTheme.colorScheme.primary,
  unselectedColor: Color = Color.LightGray,
  itemWidth: Dp = 10.dp,
  itemHeight: Dp = 10.dp,
  itemRadius: Dp = 10.dp,
  itemSpacing: Dp = 5.dp,
) {

  Box(
    modifier = modifier,
    contentAlignment = Alignment.CenterStart,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {

      repeat(pagerState.pageCount) { pos ->
        val head = pos * (itemWidth + itemSpacing).ToPx()
        val tail = head + itemWidth.ToPx()

        Canvas(modifier = Modifier.height(itemHeight)) {
          val rect = RoundRect(
            head,
            0f,
            tail,
            itemHeight.toPx(),
            CornerRadius(itemRadius.toPx())
          )
          val path = Path().apply { addRoundRect(rect) }
          drawPath(path = path, color = unselectedColor)

        }
      }
    }

    Box(
      Modifier
        .width(itemWidth).height(itemHeight)
        .wormTransition(
          pageOffset = pagerState.pageOffset,
          spacing = itemSpacing,
          selectedColor = selectedColor,
          itemWidth = itemWidth,
          itemHeight = itemHeight,
          itemRadius = itemRadius
        )
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.wormTransition(
  pageOffset: Float,
  spacing: Dp,
  selectedColor: Color,
  itemWidth: Dp,
  itemHeight: Dp,
  itemRadius: Dp
) =
  drawBehind {
    val distance = (itemWidth + spacing).toPx()
    val wormOffset = (pageOffset % 1) * 2

    val xPos = pageOffset.toInt() * distance

    val head = xPos + distance * 0f.coerceAtLeast(wormOffset - 1)

    val tail = xPos + itemWidth.toPx() + 1f.coerceAtMost(wormOffset) * distance

    val worm = RoundRect(
      head, 0f, tail, itemHeight.toPx(),
      CornerRadius(itemRadius.toPx())
    )

    val path = Path().apply { addRoundRect(worm) }
    drawPath(path = path, color = selectedColor)
  }