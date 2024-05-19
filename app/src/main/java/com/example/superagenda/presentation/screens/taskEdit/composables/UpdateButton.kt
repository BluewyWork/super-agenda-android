package com.example.superagenda.presentation.screens.taskEdit.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun UpdateButton(onClick: () -> Unit) {
    Button(onClick) {
        Text("Update")
    }
}