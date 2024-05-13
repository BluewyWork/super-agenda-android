package com.example.superagenda.presentation.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.login.composables.PasswordTextField
import com.example.superagenda.presentation.screens.login.composables.UsernameTextField

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
   Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
       Column(modifier = Modifier.padding(innerPadding)) {
           Login()
       }
   }
}

@Composable
fun Login() {
    Column {
        UsernameTextField(username = "placeholder") {

        }
        PasswordTextField(password = "placeholder") {

        }
    }
}