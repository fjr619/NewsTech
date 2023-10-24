package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JumpingIndicator(
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
        .width(itemWidth)
        .height(itemHeight)
        .jumpingDotTransition(
          (itemWidth + itemSpacing).ToPx(),
          pagerState.currentPage,
          pagerState.currentPageOffsetFraction,
          0.5f
        )
        .background(
          color = selectedColor,
          shape = CircleShape,
        )
    )
  }
}


private fun Modifier.jumpingDotTransition(
  distance: Float,
  currentPage: Int,
  currentPageOffsetFraction: Float,
  jumpScale: Float
) =
  graphicsLayer {
    val scrollPosition = currentPage + currentPageOffsetFraction
    translationX = scrollPosition * distance

    val scale: Float
    val targetScale = jumpScale - 1f

    scale = if (currentPageOffsetFraction.absoluteValue < .5) {
      1.0f + (currentPageOffsetFraction.absoluteValue * 2) * targetScale;
    } else {
      jumpScale + ((1 - (currentPageOffsetFraction.absoluteValue * 2)) * targetScale);
    }

    scaleX = scale
    scaleY = scale
  }