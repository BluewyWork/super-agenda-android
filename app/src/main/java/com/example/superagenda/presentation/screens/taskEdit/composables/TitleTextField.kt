package com.example.superagenda.presentation.screens.taskEdit.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TitleTextField(title: String, onTextChange: (String) -> Unit) {
    TextField(
        value = title,
        onValueChange = onTextChange,
        label = { Text("Title") },
        modifier = Modifier.fillMaxWidth()
    )
}
