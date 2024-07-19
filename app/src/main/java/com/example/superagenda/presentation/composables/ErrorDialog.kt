package com.example.superagenda.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorDialog(errorMessage: String?, onDismiss: () -> Unit) {
   if (errorMessage != null) {
      AlertDialog(
         onDismissRequest = { onDismiss() },
         title = { Text(text = "Error") },
         text = { Text(text = errorMessage) },
         confirmButton = {
            TextButton(onClick = { onDismiss() }) {
               Text(text = "OK")
            }
         }
      )
   }
}
