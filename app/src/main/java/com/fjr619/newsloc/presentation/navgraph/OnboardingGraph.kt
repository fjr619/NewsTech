package com.fjr619.newsloc.presentation.navgraph

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.fjr619.newsloc.presentation.mainactivity.BiometricPromptManager
import com.fjr619.newsloc.presentation.mainactivity.BiometricViewModel
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigator
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigatorViewModel
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel
import com.fjr619.newsloc.util.composestateevents.EventEffect

@Composable
fun OnboardingGraph(
  navController: NavHostController,
  navigationType: NavigationType,
  startDestination: String,
  promptManager: BiometricPromptManager
) {
  Log.e("TAG", "startDestination $startDestination")
  NavHost(
    navController = navController,
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
        navigationType = navigationType,
        countBookmark = countBookmark.articles.size
      )
    }

    composable(
      route = Route.TestScreen.route
    ) {
      // A surface container using the 'background' color from the theme
      val viewModel: BiometricViewModel = hiltViewModel()
      val state by viewModel.state.collectAsStateWithLifecycle()

      Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
      ) {

        println("biometricResult $state")

        val enrollLauncher = rememberLauncherForActivityResult(
          contract = ActivityResultContracts.StartActivityForResult(),
          onResult = {
            println("Activity result: $it")
          }
        )
        LaunchedEffect(state) {
          if (state.biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
            if (Build.VERSION.SDK_INT >= 30) {
              val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                  Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                  BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
              }
              enrollLauncher.launch(enrollIntent)
            }
          }
        }

        LaunchedEffect(Unit) {
          println("LaunchedEffect")
          if (state.biometricResult == BiometricPromptManager.BiometricResult.Init) {
            promptManager.showBiometricPrompt(
              title = "Sample prompt",
              description = "Sample prompt description",
              onResult = {
                viewModel.updateResult(it)

//                //navigation bisa juga dilakukan di sini, karena ini actionnya dari callback dialog, sehingga aman, tidak akan terpanggil kembali
//                if (it == BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
//                  navController.navigate(Route.NewsNavigation.route) {
//                    popUpTo(Route.RootNavigation.route) {
//                      inclusive = true
//                    }
//                  }
//                }
              }
            )
          }
        }

        //Event effect untuk melakukan action 1 time event
        EventEffect(
          event = state.processSucceededEvent,
          onConsumed = viewModel::onConsumedSucceededEvent,
          action = {
            navController.navigate(Route.NewsNavigation.route) {
              popUpTo(Route.RootNavigation.route) {
                inclusive = true
              }
            }
          }
        )

        Column(
          modifier = Modifier
            .fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {

          state.biometricResult.let { result ->
            Text(
              text = when (result) {
                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                  result.error
                }

                BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                  "Authentication failed"
                }

                BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                  "Authentication not set"
                }

//                BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
//                  "Authentication success"
//                }

                BiometricPromptManager.BiometricResult.FeatureUnavailable -> {
                  "Feature unavailable"
                }

                BiometricPromptManager.BiometricResult.HardwareUnavailable -> {
                  "Hardware unavailable"
                }

                else -> ""
              }
            )
          }
        }
      }
    }
  }
}

