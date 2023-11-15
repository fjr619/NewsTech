package com.fjr619.newsloc.presentation.common.pulltorefresh

import androidx.annotation.StringRes
import com.fjr619.newsloc.R

enum class RefreshIndicatorState(@StringRes val messageRes: Int) {
  Default(R.string.pull_to_refresh_complete_label),
  PullingDown(R.string.pull_to_refresh_pull_label),
  ReachedThreshold(R.string.pull_to_refresh_release_label),
  Refreshing(R.string.pull_to_refresh_refreshing_label)
}