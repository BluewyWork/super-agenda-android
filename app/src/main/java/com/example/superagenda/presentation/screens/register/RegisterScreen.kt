package com.example.superagenda.presentation.screens.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.register.composables.GoToLoginScreen
import com.example.superagenda.presentation.screens.register.composables.PasswordTextField
import com.example.superagenda.presentation.screens.register.composables.RegisterButton
import com.example.superagenda.presentation.screens.register.composables.UsernameTextField

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
    Scaffold(bottomBar = {
        NavigationBar(
            navController
        )
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Register(registerViewModel, navController)
        }
    }
}

@Composable
fun Register(registerViewModel: RegisterViewModel, navController: NavController) {
    val username: String by registerViewModel.username.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")

    UsernameTextField(username) {
        registerViewModel.onUsernameChange(it)
    }
    PasswordTextField(password) {
        registerViewModel.onPasswordChange(it)
    }
    RegisterButton {
        registerViewModel.onRegisterButtonPress(navController)
    }
    GoToLoginScreen(navController)
}