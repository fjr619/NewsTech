package com.fjr619.newsloc.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

class NewsSnackbarVisual(
  override val message: String,
  override val actionLabel: String? = null,
  override val withDismissAction: Boolean = false,
  override val duration: SnackbarDuration = SnackbarDuration.Short,
  val isError: Boolean = false
) : SnackbarVisuals

@Composable
fun NewsSnackbar(
  data: SnackbarData
) {
  val isError = (data.visuals as? NewsSnackbarVisual)?.isError ?: false
  Snackbar(
    modifier = Modifier.padding(16.dp).clip(RoundedCornerShape(0.dp)),
    containerColor = if (isError)
      MaterialTheme.colorScheme.error
    else
      MaterialTheme.colorScheme.onSecondaryContainer,
    action = {
      data.visuals.actionLabel?.let {
        TextButton(onClick = { data.performAction() }) {
          Text(
            text = it, style = MaterialTheme.typography.labelSmall.copy(
              color = if (isError)
                MaterialTheme.colorScheme.onError
              else
                MaterialTheme.colorScheme.onTertiary
            )
          )
        }
      }
    },
  )

  {
    Text(
      text = data.visuals.message,
      style = MaterialTheme.typography.labelSmall
    )
  }
}