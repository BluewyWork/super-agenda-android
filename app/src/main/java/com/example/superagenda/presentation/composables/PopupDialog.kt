package com.example.superagenda.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PopupDialog(title: String, message: String, onDismiss: () -> Unit) {
   AlertDialog(
      onDismissRequest = { onDismiss() },
      title = { Text(text = title) },
      text = { Text(text = message) },

      confirmButton = {
         TextButton(onClick = { onDismiss() }) {
            Text(text = "OK")
         }
      }
   )
}
