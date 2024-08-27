package com.example.superagenda.presentation.composables

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingPopup(onDismissRequest: () -> Unit) {
   Dialog(onDismissRequest = onDismissRequest) {
      CircularProgressIndicator()
   }
}