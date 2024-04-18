package com.fjr619.newsloc.presentation.biometric

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
class BiometricPromptManager (
  private val activity: AppCompatActivity,
) {
  fun showBiometricPrompt(
    title: String,
    description: String,
    onResult: (BiometricResult) -> Unit
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
        onResult(BiometricResult.HardwareUnavailable)
        return
      }
      BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
        onResult(BiometricResult.FeatureUnavailable)
        return
      }
      BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
        onResult(BiometricResult.AuthenticationNotSet)
        return
      }
      else -> Unit
    }

    val prompt = BiometricPrompt(
      activity,
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
          super.onAuthenticationError(errorCode, errString)
          onResult(BiometricResult.AuthenticationError(errString.toString()))
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
          super.onAuthenticationSucceeded(result)
          onResult(BiometricResult.AuthenticationSuccess)
        }

        override fun onAuthenticationFailed() {
          super.onAuthenticationFailed()
          onResult(BiometricResult.AuthenticationFailed)
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