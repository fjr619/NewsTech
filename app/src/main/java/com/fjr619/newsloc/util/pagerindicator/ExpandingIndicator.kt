package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ExpandingPageIndicatorView(
  isSelected: Boolean,
  selectedColor: Color,
  unSelectedColor: Color,
  itemWidth: Dp,
  itemHeight: Dp,
  selectedLength: Dp,
  itemRadius: Dp,
  animationDurationInMillis: Int,
  modifier: Modifier = Modifier,
) {
  val color: Color by animateColorAsState(
    targetValue = if (isSelected) {
      selectedColor
    } else {
      unSelectedColor
    },
    animationSpec = tween(
      durationMillis = animationDurationInMillis,
    ), label = ""
  )
  val width: Dp by animateDpAsState(
    targetValue = if (isSelected) {
      selectedLength
    } else {
      itemWidth
    },
    animationSpec = tween(
      durationMillis = animationDurationInMillis,
    ), label = ""
  )

  Canvas(
    modifier = modifier
      .size(
        width = width,
        height = itemHeight,
      ),
  ) {
    drawRoundRect(
      color = color,
      topLeft = Offset.Zero,
      size = Size(
        width = width.toPx(),
        height = itemHeight.toPx(),
      ),
      cornerRadius = CornerRadius(
        x = itemRadius.toPx(),
        y = itemRadius.toPx(),
      ),
    )
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandingPageIndicator(
  pagerState: PagerState,
  modifier: Modifier = Modifier,
  selectedColor: Color = MaterialTheme.colorScheme.primary,
  unSelectedColor: Color = Color.LightGray,
  itemWidth: Dp = 10.dp,
  itemHeight: Dp = 10.dp,
  itemRadius: Dp = 10.dp,
  selectedLength: Dp = 20.dp,
  itemSpacing: Dp = 5.dp,
  animationDurationInMillis: Int = 300,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(itemSpacing),
    modifier = modifier,
  ) {
    repeat(pagerState.pageCount) {
      val isSelected = it == pagerState.currentPage
      ExpandingPageIndicatorView(
        isSelected = isSelected,
        selectedColor = selectedColor,
        unSelectedColor = unSelectedColor,
        itemRadius = itemRadius,
        selectedLength = selectedLength,
        itemHeight = itemHeight,
        itemWidth = itemWidth,
        animationDurationInMillis = animationDurationInMillis,
      )
    }
  }
}