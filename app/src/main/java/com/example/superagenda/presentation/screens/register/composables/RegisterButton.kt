package com.example.superagenda.presentation.screens.register.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RegisterButton(onClick: () -> Unit) {
    Button(onClick) {
        Text("Register")
    }
}