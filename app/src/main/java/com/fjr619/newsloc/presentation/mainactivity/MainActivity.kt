package com.fjr619.newsloc.presentation.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.fjr619.newsloc.presentation.navgraph.OnboardingGraph
import com.fjr619.newsloc.ui.theme.NewsLOCTheme
import dagger.hilt.android.AndroidEntryPoint

enum class NavigationType {
  BOTTOM_NAV, NAV_RAIL, PERMANENT_NAV_DRAWER
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  val viewModel by viewModels<MainViewModel>()

  private val promptManager by lazy {
    BiometricPromptManager(this)
  }

//  @Inject
//  lateinit var promptManager: BiometricPromptManager

  /**
   * https://medium.com/scalereal/providing-assistedinject-supported-viewmodel-for-composable-using-hilt-ae973632e29a
   */
//    @EntryPoint
//    @InstallIn(ActivityComponent::class)
//    interface ViewModelFactoryProvider {
//        fun detailViewModelFactory(): DetailViewModel.Factory
//    }

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    installSplashScreen()
      .apply {
        setKeepOnScreenCondition(condition = {
          viewModel.splashCondition.value
        })
      }

    setContent {
      val windowSizeClass = calculateWindowSizeClass(activity = this)
      val navigationType by remember(windowSizeClass.widthSizeClass) {
        derivedStateOf {
          when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAV
            WindowWidthSizeClass.Medium -> NavigationType.NAV_RAIL
            WindowWidthSizeClass.Expanded -> NavigationType.PERMANENT_NAV_DRAWER
            else -> NavigationType.BOTTOM_NAV
          }
        }

      }

      NewsLOCTheme {
        ChangeSystemBarsTheme(!isSystemInDarkTheme())
        Surface(
          modifier = Modifier.fillMaxSize(),
        ) {
          println( "-- main activity ")
          OnboardingGraph(
            navController = rememberNavController(),
            startDestination = viewModel.startDestination.value,
            navigationType = navigationType,
            promptManager = promptManager
          )
        }
      }
    }
  }
}

@Composable
private fun ComponentActivity.ChangeSystemBarsTheme(lightTheme: Boolean) {
  val barColor = Color.Transparent.toArgb()
  LaunchedEffect(lightTheme) {
    if (lightTheme) {
      enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.light(
          barColor, barColor,
        ),
        navigationBarStyle = SystemBarStyle.light(
          barColor, barColor,
        ),
      )
    } else {
      enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.dark(
          barColor,
        ),
        navigationBarStyle = SystemBarStyle.dark(
          barColor,
        ),
      )
    }
  }
}