package com.fjr619.newsloc.presentation.mainactivity

import android.os.Build
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor() : ViewModel() {

  private val resultChannel = MutableStateFlow<BiometricPromptManager.BiometricResult>(BiometricPromptManager.BiometricResult.Init)
  val promptResults = resultChannel.asStateFlow()
  init {
    println("init BiometricViewModel")
  }

  fun updateResult(biometricResult: BiometricPromptManager.BiometricResult) {
    resultChannel.update {
      biometricResult
    }
  }
}

class BiometricPromptManager (
  private val activity: AppCompatActivity,
) {

  private val viewModel: BiometricViewModel by activity.viewModels<BiometricViewModel>()

  fun getResult() = viewModel.promptResults

  init {
    println("init BiometricPromptManager ")
  }

  fun showBiometricPrompt(
    title: String,
    description: String
  ) {
    val manager = BiometricManager.from(activity)
    val authenticators = if(Build.VERSION.SDK_INT >= 30) {
      BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    } else BIOMETRIC_STRONG

    val promptInfo = PromptInfo.Builder()
      .setTitle(title)
      .setDescription(description)
      .setAllowedAuthenticators(authenticators)

    if(Build.VERSION.SDK_INT < 30) {
      promptInfo.setNegativeButtonText("Cancel")
    }

    when(manager.canAuthenticate(authenticators)) {
      BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
        viewModel.updateResult(BiometricResult.HardwareUnavailable)
        return
      }
      BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
        viewModel.updateResult(BiometricResult.FeatureUnavailable)
        return
      }
      BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
        viewModel.updateResult(BiometricResult.AuthenticationNotSet)
        return
      }
      else -> Unit
    }

    val prompt = BiometricPrompt(
      activity,
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
          super.onAuthenticationError(errorCode, errString)
          viewModel.updateResult(BiometricResult.AuthenticationError(errString.toString()))
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
          super.onAuthenticationSucceeded(result)
          viewModel.updateResult(BiometricResult.AuthenticationSuccess)
        }

        override fun onAuthenticationFailed() {
          super.onAuthenticationFailed()
          viewModel.updateResult(BiometricResult.AuthenticationFailed)
        }
      }
    )
    prompt.authenticate(promptInfo.build())
  }

  sealed class BiometricResult {
    data object HardwareUnavailable: BiometricResult()
    data object FeatureUnavailable: BiometricResult()
    data class AuthenticationError(val error: String): BiometricResult()
    data object AuthenticationFailed: BiometricResult()
    data object AuthenticationSuccess: BiometricResult()
    data object AuthenticationNotSet: BiometricResult()

    data object Init: BiometricResult()
  }
}