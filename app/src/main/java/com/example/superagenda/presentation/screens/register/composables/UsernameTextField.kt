package com.example.superagenda.presentation.screens.register.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UsernameTextField(username: String, onTextChange: (String) -> Unit) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
        value = username,
        enabled = true,
        onValueChange = { onTextChange(it) },
        placeholder = { Text(text = "Username") },
        leadingIcon = { Icon(Icons.Default.Person, null) }
    )
}