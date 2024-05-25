package com.fjr619.newsloc.presentation.navgraph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjr619.newsloc.presentation.biometric.BiometricPromptManager
import com.fjr619.newsloc.presentation.biometric.BiometricScreen
import com.fjr619.newsloc.presentation.biometric.BiometricViewModel
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigator
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigatorViewModel
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel

@Composable
fun OnboardingGraph(
  newsNavController: NewsNavController,
//  navigationType: NavigationType,
  startDestination: String,
  promptManager: BiometricPromptManager
) {
  NavHost(
    navController = newsNavController.navController,
    route = Route.RootNavigation.route,
    startDestination = startDestination,
    enterTransition = { fadeIn(animationSpec = tween(200)) },
    exitTransition = { fadeOut(animationSpec = tween(200)) },
  ) {
    navigation(
      route = Route.AppStartNavigation.route,
      startDestination = Route.OnBoardingScreen.route
    ) {
      composable(route = Route.OnBoardingScreen.route) {
        val viewModel: OnboardingViewModel = hiltViewModel()
        OnboardingScreen(viewModel::onEvent)
      }
    }

    composable(
      route = Route.NewsNavigation.route
    ) {
      val viewModel: NewsNavigatorViewModel = hiltViewModel()
      val countBookmark by viewModel.state
      NewsNavigator(
//        navigationType = navigationType,
        countBookmark = countBookmark.articles.size
      )
    }

    composable(
      route = Route.TestScreen.route
    ) {
      // A surface container using the 'background' color from the theme
      val viewModel: BiometricViewModel = hiltViewModel()
      val state by viewModel.state.collectAsStateWithLifecycle()



      BiometricScreen(
        state = state,
        promptManager = promptManager,
        onBiometricEvent = viewModel::onEvent,
        navigateToMain = {
          newsNavController.navigateToMain()
        },
      )
    }
  }
}

