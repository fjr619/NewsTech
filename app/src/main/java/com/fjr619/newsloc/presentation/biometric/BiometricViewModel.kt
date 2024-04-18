package com.fjr619.newsloc.presentation.biometric

import androidx.lifecycle.ViewModel
import com.fjr619.newsloc.util.composestateevents.consumed
import com.fjr619.newsloc.util.composestateevents.triggered
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BiometricViewModel @Inject constructor() : ViewModel() {

  private val _state = MutableStateFlow<BiometricUiState>(BiometricUiState())
  val state = _state.asStateFlow()

  fun onEvent(event: BiometricEvent) {
    when(event) {
      is BiometricEvent.UpdateResult -> updateResult(event.result)
      is BiometricEvent.ConsumedSucceedBiometric -> onConsumedSucceededEvent()
      is BiometricEvent.ConsumedShowDialog -> onConsumedShowDialogEvent()
      is BiometricEvent.TriggerShowDialog -> onTriggerShowDialogEvent()
    }
  }


  private fun updateResult(biometricResult: BiometricPromptManager.BiometricResult) {
    _state.update {
      it.copy(
        biometricResult = biometricResult
      )
    }

    //klo sukses trigger 1 time event : navigation
    if (biometricResult == BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
      _state.update {
        it.copy(
          processSucceededEvent = triggered
        )
      }
    }
  }

  //1 time eventnya sudah terconsumed
  private fun onConsumedSucceededEvent() {
    _state.update {
      it.copy(
        processSucceededEvent = consumed
      )
    }
  }

  private fun onConsumedShowDialogEvent() {
    _state.update {
      it.copy(
        processShowPromptEvent = consumed
      )
    }
  }

  private fun onTriggerShowDialogEvent() {
    _state.update {
      it.copy(
        processShowPromptEvent = triggered
      )
    }
  }
}