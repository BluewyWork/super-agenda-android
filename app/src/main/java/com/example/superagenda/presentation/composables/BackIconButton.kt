package com.example.superagenda.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackIconButton(onClick: () -> Unit) {
   IconButton(onClick = onClick) {
      Icon(
         imageVector = Icons.AutoMirrored.Filled.ArrowBack,
         contentDescription = "Arrow Back"
      )
   }
}
