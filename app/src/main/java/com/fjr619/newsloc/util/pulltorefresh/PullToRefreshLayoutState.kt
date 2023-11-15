package com.fjr619.newsloc.util.pulltorefresh

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PullToRefreshLayoutState(
  val onTimeUpdated: (Long) -> String,
) {

  private val _lastRefreshTime = MutableStateFlow(System.currentTimeMillis())
  val lastRefreshTime = _lastRefreshTime.asStateFlow()

  private var _refreshIndicatorState = MutableStateFlow(RefreshIndicatorState.Default)
  var refreshIndicatorState = _refreshIndicatorState.asStateFlow()

  private var _lastRefreshText = MutableStateFlow("")
  var lastRefreshText = _lastRefreshText.asStateFlow()

  fun updateRefreshState(refreshState: RefreshIndicatorState) {
    val now = System.currentTimeMillis()
    val timeElapsed = now - _lastRefreshTime.value
    _lastRefreshText.value = onTimeUpdated(timeElapsed)
    _refreshIndicatorState.value = refreshState
  }

  fun refresh() {
    _lastRefreshTime.value = System.currentTimeMillis()
    updateRefreshState(RefreshIndicatorState.Refreshing)
  }
}

@Composable
fun rememberPullToRefreshState(
  onTimeUpdated: (Long) -> String,
): PullToRefreshLayoutState =
  remember {
    PullToRefreshLayoutState(onTimeUpdated)
  }