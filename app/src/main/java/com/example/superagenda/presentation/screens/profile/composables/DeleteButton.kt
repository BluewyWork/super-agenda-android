package com.example.superagenda.presentation.screens.profile.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DeleteButton(onClick: () -> Unit) {
    Button(onClick) {
        Text("Delete Profile")
    }
}