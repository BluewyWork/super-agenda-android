package com.example.superagenda.presentation.screens.login.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(onClick) {
        Text("Login")
    }
}