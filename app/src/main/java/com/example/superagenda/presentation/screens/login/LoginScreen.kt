package com.example.superagenda.presentation.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.presentation.screens.login.composables.GoToRegisterScreen
import com.example.superagenda.presentation.screens.login.composables.LoginButton
import com.example.superagenda.presentation.screens.login.composables.PasswordTextField
import com.example.superagenda.presentation.screens.login.composables.UsernameTextField

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
   Scaffold { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)) {
         Login(loginViewModel, navController)
      }

      val errorMessage: String? by loginViewModel.errorMessage.observeAsState(null)

//      if (errorMessage != null)
//      {
//         PopupDialog(message = errorMessage) {
//            loginViewModel.onErrorDismissed()
//         }
//      }
   }
}

@Composable
fun Login(loginViewModel: LoginViewModel, navController: NavController) {
   val email: String by loginViewModel.username.observeAsState("")
   val password: String by loginViewModel.password.observeAsState("")

   Column {
      Spacer(modifier = Modifier.padding(16.dp))
      Spacer(modifier = Modifier.padding(16.dp))
      Spacer(modifier = Modifier.padding(16.dp))
      Spacer(modifier = Modifier.padding(16.dp))
      Spacer(modifier = Modifier.padding(16.dp))
      Spacer(modifier = Modifier.padding(16.dp))

      UsernameTextField(email) {
         loginViewModel.onUsernameChange(it)
      }
      PasswordTextField(password) {
         loginViewModel.onPasswordChange(it)
      }

      Spacer(modifier = Modifier.padding(16.dp))

      LoginButton {
         loginViewModel.onLoginButtonPress(navController)
      }
      GoToRegisterScreen(navController)
   }
}