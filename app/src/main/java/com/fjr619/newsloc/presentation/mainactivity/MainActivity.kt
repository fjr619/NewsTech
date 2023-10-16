package com.fjr619.newsloc.presentation.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.fjr619.newsloc.presentation.navgraph.RootNavGraph
import com.fjr619.newsloc.ui.theme.NewsLOCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
      val viewModel by viewModels<MainViewModel>()

//      /**
//       * https://medium.com/scalereal/providing-assistedinject-supported-viewmodel-for-composable-using-hilt-ae973632e29a
//       */
//      @EntryPoint
//      @InstallIn(ActivityComponent::class)
//      interface ViewModelFactoryProvider {
//            fun detailViewModelFactory(): DetailViewModel.Factory
//      }

      @OptIn(ExperimentalMaterial3Api::class)
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
                  NewsLOCTheme {
                        ChangeSystemBarsTheme(!isSystemInDarkTheme())
                        Surface(
                              modifier = Modifier.fillMaxSize(),
                        ) {
                              RootNavGraph(
                                    startDestination = viewModel.startDestination.value
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