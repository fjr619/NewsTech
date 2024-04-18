package com.fjr619.newsloc.presentation.navgraph

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.fjr619.newsloc.presentation.mainactivity.NavigationType
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigator
import com.fjr619.newsloc.presentation.news_navigator.NewsNavigatorViewModel
import com.fjr619.newsloc.presentation.onboarding.OnboardingScreen
import com.fjr619.newsloc.presentation.onboarding.OnboardingViewModel

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
    startDestination = startDestination
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
      Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
      ) {
        val biometricResult by promptManager.getResult().collectAsStateWithLifecycle()

        println("biometricResult $biometricResult")

        val enrollLauncher = rememberLauncherForActivityResult(
          contract = ActivityResultContracts.StartActivityForResult(),
          onResult = {
            println("Activity result: $it")
          }
        )
        LaunchedEffect(biometricResult) {
          if(biometricResult is BiometricPromptManager.BiometricResult.AuthenticationNotSet) {
            if(Build.VERSION.SDK_INT >= 30) {
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
          if (biometricResult == BiometricPromptManager.BiometricResult.Init) {
            promptManager.showBiometricPrompt(
              title = "Sample prompt",
              description = "Sample prompt description"
            )
          }
          }

        Column(
          modifier = Modifier
            .fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
//          Button(onClick = {
//            promptManager.showBiometricPrompt(
//              title = "Sample prompt",
//              description = "Sample prompt description"
//            )
//          }) {
//            Text(text = "Authenticate")
//          }



          biometricResult?.let { result ->
            Text(
              text = when(result) {
                is BiometricPromptManager.BiometricResult.AuthenticationError -> {
                  result.error
                }
                BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                  "Authentication failed"
                }
                BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                  "Authentication not set"
                }
                BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                  "Authentication success"
                }
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

//        navigation(
//            route = Route.NewsNavigation.route,
//            startDestination = Route.HomeScreen.route,
//        ) {
//            composable(
//                route = Route.HomeScreen.route,
//                enterTransition = AnimationTransition.fadeIn(300),
//                exitTransition = AnimationTransition.fadeOut(300),
//                popEnterTransition = AnimationTransition.fadeIn(300),
//                popExitTransition = AnimationTransition.fadeOut(300),
//            ) { navBackEntry ->
//                val viewModel: HomeViewModel = hiltViewModel()
//                HomeScreen(
//                    articles = viewModel.news.collectAsLazyPagingItems(),
//                    navigate = {
//                        navController.navigate(it) {
//                            launchSingleTop = true
//                        }
//                    },
//                    navigateToDetail = {
//                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
//                        navController.navigate(Route.DetailsScreen.route)
//                    })
//            }
//
//            composable(
//                route = Route.SearchScreen.route,
//                enterTransition = AnimationTransition.slideInLeft(300),
//                exitTransition = AnimationTransition.slideOutLeft(300),
//                popEnterTransition = AnimationTransition.slideInRight(300),
//                popExitTransition = AnimationTransition.slideOutRight(300)
//            ) {
//                val viewModel: SearchViewModel = hiltViewModel()
//                val state = viewModel.state.value
//
//                SearchScreen(
//                    state = state,
//                    event = viewModel::onEvent,
//                    navigateToDetail = {
//                        navController.currentBackStackEntry?.savedStateHandle?.set("article", it)
//                        navController.navigate(Route.DetailsScreen.route)
//                    }
//                )
//            }
//
//
//            composable(route = Route.BookmarkScreen.route) {
//            }
//
//
//            composable(
//                route = Route.DetailsScreen.route,
//                enterTransition = AnimationTransition.fadeIn(300),
//                exitTransition = AnimationTransition.fadeOut(300),
//                popEnterTransition = AnimationTransition.fadeIn(300),
//                popExitTransition = AnimationTransition.fadeOut(300),
//            ) {
//                val article = remember {
//                    navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
//                }
//
//                it.savedStateHandle.set("aticle", article)
//
//                val viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(newsUseCases = newsUseCases, article = article))
//
//                article?.let {
//                    Surface(modifier = Modifier.fillMaxSize()) {
//                        DetailScreen(
//                            article = it,
//                            bookmarkArticle = viewModel.bookmarkArticle.value,
//                            event = viewModel::onEvent,
//                            navigateUp = { navController.popBackStack() },
//                            sideEffect = viewModel.sideEffect.collectAsStateWithLifecycle(
//                                initialValue = UIComponent.None()
//                            ).value
//                        )
//                    }
//                }
//            }
//        }
  }
}

