package com.fjr619.newsloc.presentation.common.pulltorefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * https://github.com/OyaCanli/tutorial_samples/tree/master/PullRefreshComposeSample
 * https://canlioya.medium.com/customise-pull-to-refresh-on-android-with-jetpack-compose-24a7119a4b94
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshLayout(
  modifier: Modifier = Modifier,
  pullRefreshLayoutState: PullToRefreshLayoutState,
  onRefresh: () -> Unit,
  content: @Composable () -> Unit,
) {
  val refreshIndicatorState by rememberSaveable {
    pullRefreshLayoutState.refreshIndicatorState
  }

  val timeElapsedSinceLastRefresh by pullRefreshLayoutState.lastRefreshText


  val pullToRefreshState = rememberPullRefreshState(
    refreshing = refreshIndicatorState == RefreshIndicatorState.Refreshing,
    refreshThreshold = 120.dp,
    onRefresh = {
      onRefresh()
      pullRefreshLayoutState.refresh()
    },
  )

  LaunchedEffect(key1 = pullToRefreshState.progress) {
    when {
      pullToRefreshState.progress >= 1 -> {
        pullRefreshLayoutState.updateRefreshState(RefreshIndicatorState.ReachedThreshold)
      }

      pullToRefreshState.progress > 0 -> {
        pullRefreshLayoutState.updateRefreshState(RefreshIndicatorState.PullingDown)
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .pullRefresh(pullToRefreshState),
  ) {
    PullToRefreshIndicator(
      indicatorState = refreshIndicatorState,
      pullToRefreshProgress = pullToRefreshState.progress,
      timeElapsed = timeElapsedSinceLastRefresh,
    )
    Box(modifier = Modifier.weight(1f)) {
      content()
    }
  }
}