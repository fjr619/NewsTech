package com.fjr619.newsloc.presentation.biometric

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.fjr619.newsloc.util.composestateevents.EventEffect

@Composable
fun BiometricScreen(
  state: BiometricUiState,
  promptManager: BiometricPromptManager,
  onBiometricEvent: (BiometricEvent) -> Unit,
  navigateToMain: () -> Unit
) {
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
      val observer = LifecycleEventObserver { _, event ->
        when (event) {
          Lifecycle.Event.ON_RESUME -> {
            onBiometricEvent(BiometricEvent.TriggerShowDialog)
          }
          Lifecycle.Event.ON_PAUSE -> {
//            promptManager.dismissAuthentication()
            onBiometricEvent(BiometricEvent.ConsumedShowDialog)
          }
          else -> {}
        }
      }

      // Add the observer to the lifecycle
      lifecycleOwner.lifecycle.addObserver(observer)

      // When the effect leaves the Composition, remove the observer
      onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
      }
    }


    //Event effect untuk melakukan action 1 time event navigation
    EventEffect(
      event = state.processSucceededEvent,
      onConsumed = {
        onBiometricEvent(BiometricEvent.ConsumedSucceedBiometric)
      },
      action = navigateToMain
    )

    //Event effect untuk melakukan action 1 time event show dialog biometric
    EventEffect(
      event = state.processShowPromptEvent,
      onConsumed = {
        onBiometricEvent(BiometricEvent.ConsumedShowDialog)
      },
      action = {
        if (state.biometricResult !is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
          promptManager.showBiometricPrompt(
            title = "Sample prompt",
            description = "Sample prompt description",
            onResult = {
              onBiometricEvent(BiometricEvent.UpdateResult(it))
            }
          )
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

        AnimatedVisibility(visible =
        result != BiometricPromptManager.BiometricResult.Init && result !=
          BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
          Button(onClick = { onBiometricEvent(BiometricEvent.TriggerShowDialog) }) {
            Text(text = "Retry")
          }
        }
      }
    }
  }
}