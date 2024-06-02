package com.example.superagenda.presentation.screens.profile.composables

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
            .padding(start = 8.dp, end = 8.dp),
        value = username,
        enabled = false,
        onValueChange = { onTextChange(it) },
        placeholder = { Text(text = "Username") },
        leadingIcon = { Icon(Icons.Default.Person, null) }
    )
}