package com.fjr619.newsloc.util.pagerindicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.ToPx() = with(LocalDensity.current) { this@ToPx.toPx() }


@Composable
fun Int.ToDp() = with(LocalDensity.current) { this@ToDp.toDp() }

@OptIn(ExperimentalFoundationApi::class)
val PagerState.pageOffset: Float
  get() = this.currentPage + this.currentPageOffsetFraction


// To get scrolled offset from snap position
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
  return (currentPage - page) + currentPageOffsetFraction
}



