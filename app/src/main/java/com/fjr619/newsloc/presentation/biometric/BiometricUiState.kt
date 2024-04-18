package com.fjr619.newsloc.presentation.biometric

import com.fjr619.newsloc.util.composestateevents.StateEvent
import com.fjr619.newsloc.util.composestateevents.consumed

data class BiometricUiState(
  val biometricResult: BiometricPromptManager.BiometricResult = BiometricPromptManager.BiometricResult.Init,
  val processSucceededEvent: StateEvent = consumed,
  val processShowPromptEvent: StateEvent = consumed
)