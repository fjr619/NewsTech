package com.fjr619.newsloc.util.pulltorefresh

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.R
import kotlin.math.roundToInt

const val maxHeight = 100

@Composable
fun PullToRefreshIndicator(
  indicatorState: RefreshIndicatorState,
  pullToRefreshProgress: Float,
  timeElapsed: String,
) {

  val heightModifier = when (indicatorState) {
    RefreshIndicatorState.PullingDown -> {
      Modifier.height(
        (pullToRefreshProgress * 100)
          .roundToInt()
          .coerceAtMost(maxHeight).dp,
      )
    }

    RefreshIndicatorState.ReachedThreshold -> Modifier.height(maxHeight.dp)
    RefreshIndicatorState.Refreshing -> Modifier.wrapContentHeight()
    RefreshIndicatorState.Default -> Modifier.height(0.dp)
  }
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .animateContentSize()
      .then(heightModifier)
      .padding(15.dp),
    contentAlignment = Alignment.BottomStart,
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {

      val arrowDegree by animateFloatAsState(targetValue = (if (indicatorState  == RefreshIndicatorState.PullingDown) 270f else 90f),
        label = ""
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        AnimatedVisibility(visible = indicatorState != RefreshIndicatorState.Refreshing) {
          Icon(
            modifier = Modifier.rotate(arrowDegree).size(10.dp),
            imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }

        Text(
          text = stringResource(indicatorState.messageRes),
          style = MaterialTheme.typography.labelMedium,
        )
      }

      AnimatedVisibility(visible = indicatorState == RefreshIndicatorState.Refreshing) {
        CircularProgressIndicator(
          modifier = Modifier.size(16.dp),
          strokeWidth = 2.dp,
        )
      }

      AnimatedVisibility(visible = indicatorState != RefreshIndicatorState.Refreshing) {
        Text(
          text = stringResource(R.string.last_updated, timeElapsed),
          style = MaterialTheme.typography.labelSmall,
        )
      }
    }
  }
}