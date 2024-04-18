package com.fjr619.newsloc.presentation.biometric

sealed interface BiometricEvent {

  data class  UpdateResult(val result: BiometricPromptManager.BiometricResult): BiometricEvent
  data object ConsumedSucceedBiometric : BiometricEvent
  data object ConsumedShowDialog: BiometricEvent
  data object TriggerShowDialog: BiometricEvent
}