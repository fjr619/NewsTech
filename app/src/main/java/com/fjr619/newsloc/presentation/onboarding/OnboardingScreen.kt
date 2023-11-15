package com.fjr619.newsloc.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.presentation.Dimens
import com.fjr619.newsloc.presentation.common.NewsButton
import com.fjr619.newsloc.presentation.common.NewsTextButton
import com.fjr619.newsloc.presentation.onboarding.components.OnboardingEvent
import com.fjr619.newsloc.presentation.onboarding.components.OnboardingPage
import com.fjr619.newsloc.util.pagerindicator.DougnutIndicator
import com.fjr619.newsloc.util.pagerindicator.ExpandingPageIndicator
import com.fjr619.newsloc.util.pagerindicator.JumpingIndicator
import com.fjr619.newsloc.util.pagerindicator.RevealIndicator
import com.fjr619.newsloc.util.pagerindicator.SlidingIndicator
import com.fjr619.newsloc.util.pagerindicator.SwapDotIndicators
import com.fjr619.newsloc.util.pagerindicator.WormIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
  onEvent: (OnboardingEvent) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .navigationBarsPadding()
  ) {
    val pagerState = rememberPagerState(
      initialPage = 0
    ) {
      pages.size
    }

    val buttonState = remember(pagerState.currentPage) {
      derivedStateOf {
        when (pagerState.currentPage) {
          0 -> listOf("", "Next")
          1 -> listOf("Back", "Next")
          2 -> listOf("Back", "Get Started")
          else -> listOf("", "")
        }
      }
    }

    HorizontalPager(state = pagerState) { index ->
      OnboardingPage(page = pages[index])
    }

    Spacer(modifier = Modifier.weight(1f))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Dimens.MediumPadding2)
        .navigationBarsPadding(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        SlidingIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(5.dp))
        ExpandingPageIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(5.dp))
        WormIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(5.dp))
        JumpingIndicator(pagerState = pagerState)

        Spacer(modifier = Modifier.height(5.dp))
        SwapDotIndicators(pagerState = pagerState)

        Spacer(modifier = Modifier.height(5.dp))
        RevealIndicator(pagerState = pagerState)
        
        Spacer(modifier = Modifier.height(5.dp))
        DougnutIndicator(pagerState = pagerState)
      }


      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        val scope = rememberCoroutineScope()

        if (buttonState.value[0].isNotEmpty()) {
          NewsTextButton(
            text = buttonState.value[0],
            onClick = {
              scope.launch {
                pagerState.animateScrollToPage(
                  page = pagerState.currentPage - 1
                )
              }

            }
          )

          Spacer(modifier = Modifier.width(12.dp))
        }

        NewsButton(
          text = buttonState.value[1],
          onClick = {
            scope.launch {
              if (pagerState.currentPage == pagerState.pageCount - 1) {
                onEvent(OnboardingEvent.SaveAppEntry)
              } else {
                pagerState.animateScrollToPage(
                  page = pagerState.currentPage + 1
                )
              }
            }
          }
        )
      }
    }
    Spacer(modifier = Modifier.weight(0.2f))
  }
}