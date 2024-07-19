package com.example.superagenda.presentation.screens.register

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
import com.example.superagenda.presentation.composables.ErrorDialog
import com.example.superagenda.presentation.screens.register.composables.GoToLoginScreen
import com.example.superagenda.presentation.screens.register.composables.PasswordTextField
import com.example.superagenda.presentation.screens.register.composables.RegisterButton
import com.example.superagenda.presentation.screens.register.composables.UsernameTextField

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
   Scaffold { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)) {
         Register(registerViewModel, navController)
      }

      val errorMessage: String? by registerViewModel.errorMessage.observeAsState(null)

      if (errorMessage != null) {
         ErrorDialog(errorMessage = errorMessage) {
            registerViewModel.onErrorDismissed()
         }
      }
   }
}

@Composable
fun Register(registerViewModel: RegisterViewModel, navController: NavController) {
   val username: String by registerViewModel.username.observeAsState("")
   val password: String by registerViewModel.password.observeAsState("")


   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))

   UsernameTextField(username) {
      registerViewModel.onUsernameChange(it)
   }
   PasswordTextField(password) {
      registerViewModel.onPasswordChange(it)
   }

   Spacer(modifier = Modifier.padding(16.dp))

   RegisterButton {
      registerViewModel.onRegisterButtonPress(navController)
   }
   GoToLoginScreen(navController)
}