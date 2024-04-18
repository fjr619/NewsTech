package com.fjr619.newsloc.presentation.biometric

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.provider.Settings
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fjr619.newsloc.util.composestateevents.EventEffect

@Composable
fun BiometricScreen(
  state: BiometricUiState,
  promptManager: BiometricPromptManager,
  updateResult: (BiometricPromptManager.BiometricResult) -> Unit,
  onConsumedSucceededEvent: () -> Unit,
  navigateToMain: () -> Unit
) {
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
  ) {

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
              BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
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
          onResult = updateResult
//          onResult = {
//            updateResult(it)
//
//                //navigation bisa juga dilakukan di sini, karena ini actionnya dari callback dialog, sehingga aman, tidak akan terpanggil kembali
//                if (it == BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
//                  navigateToMain()
//                }
//          }
        )
      }
    }

    //Event effect untuk melakukan action 1 time event
    EventEffect(
      event = state.processSucceededEvent,
      onConsumed = onConsumedSucceededEvent,
      action = navigateToMain
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