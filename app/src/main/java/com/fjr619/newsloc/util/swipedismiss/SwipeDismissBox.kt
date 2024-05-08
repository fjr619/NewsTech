package com.fjr619.newsloc.util.swipedismiss

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import kotlinx.coroutines.flow.drop
import kotlin.math.abs

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SwipeBox(
  modifier: Modifier = Modifier,
  onDelete: () -> Unit,
  content: @Composable () -> Unit
) {

  val view = LocalView.current
  val icon: ImageVector = Icons.Outlined.Delete
  val alignment: Alignment = Alignment.CenterEnd
  var color: Color = Color.Transparent
  val density = LocalDensity.current
  var offset by remember { mutableFloatStateOf(0f) }
  val offsetMatch by remember(offset) {
    derivedStateOf {
      mutableStateOf(
        abs(offset) >= with(density) {
          { 150.dp.toPx() }
        }.invoke()
      ).value
    }
  }

  val swipeState = rememberSwipeToDismissBoxState(
    confirmValueChange = {
      if (it == SwipeToDismissBoxValue.EndToStart) {
        if (offsetMatch) {
          onDelete()
        }
        true
      } else false
    },
    positionalThreshold = with(density) {
      { 150.dp.toPx() }
    }
  )

  val iconSize = animateDpAsState(
    targetValue = if (offsetMatch) {
      40.dp
    } else {
      24.dp
    }, label = ""
  ).value

  when (swipeState.dismissDirection) {
    SwipeToDismissBoxValue.EndToStart -> {

      color = animateColorAsState(
        targetValue = if (offsetMatch) {
          MaterialTheme.colorScheme.errorContainer
        } else {
          MaterialTheme.colorScheme.surfaceContainer
        }, label = ""
      ).value
    }
    else -> {}
  }

  LaunchedEffect(Unit) {
    snapshotFlow {offsetMatch}
      .drop(1)
      .collect {
        view.performHapticFeedback(
          HapticFeedbackConstantsCompat.CLOCK_TICK
        )
      }
  }

  SwipeToDismissBox(
    enableDismissFromStartToEnd = false,
    modifier = modifier
      .animateContentSize()
      .onSizeChanged {
        offset = swipeState.requireOffset()
      },
    state = swipeState,
    backgroundContent = {
      Box(
        contentAlignment = alignment,
        modifier = Modifier
          .fillMaxSize()
          .background(color)
      ) {
        Box(
          modifier = Modifier.size(40.dp),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            modifier = Modifier
              .size(iconSize),
            imageVector = icon, contentDescription = null
          )
        }

      }
    }
  ) {
    content()
  }

//  when (swipeState.currentValue) {
//    SwipeToDismissBoxValue.EndToStart -> {
//      LaunchedEffect(swipeState) {
//        onDelete()
//        swipeState.snapTo(SwipeToDismissBoxValue.Settled)
//      }
//    }
//
//    else -> {}

//    SwipeToDismissBoxValue.StartToEnd -> {
//      LaunchedEffect(swipeState) {
////        onEdit()
//        swipeState.snapTo(SwipeToDismissBoxValue.Settled)
//      }
//    }
//
//    SwipeToDismissBoxValue.Settled -> {
//    }
//  }
}