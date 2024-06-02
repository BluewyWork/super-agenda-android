package com.example.superagenda.presentation.screens.login.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GoToRegisterScreen(navController: NavController) {
    Text(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("register") },
        text = "Haz click aqui para registrarte!",
        style = TextStyle(textAlign = TextAlign.Right)
    )
}