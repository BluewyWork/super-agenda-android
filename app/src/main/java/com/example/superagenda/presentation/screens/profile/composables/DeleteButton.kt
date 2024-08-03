package com.example.superagenda.presentation.screens.profile.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteButton(onClick: () -> Unit) {
   Button(onClick, modifier = Modifier.fillMaxWidth()) {
      Text("Delete")
   }
}