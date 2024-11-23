package com.example.superagenda.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PopupDialog(title: String, message: String, error: String, onDismiss: () -> Unit) {
   AlertDialog(
      onDismissRequest = { onDismiss() },
      title = { Text(text = title) },
      text = {
         Column {
            if (error.isNotEmpty()) {
               Text(text = error)
               Spacer(Modifier.height(16.dp))
            }

            Text(text = message)
         }
      },

      confirmButton = {
         TextButton(onClick = { onDismiss() }) {
            Text(text = "OK")
         }
      }
   )
}
