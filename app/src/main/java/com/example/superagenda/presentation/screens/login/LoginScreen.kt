package com.example.superagenda.presentation.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.composables.BackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
   val popups by loginViewModel.popups.collectAsStateWithLifecycle()

   if (popups.isNotEmpty()) {
      val popup = popups.first()

      AlertDialog(onDismissRequest = {
         popup.code()
         loginViewModel.onPopupDismissed()
      },

         title = { Text(popup.title) },

         text = {
            Column {
               if (popup.error.isNotBlank()) {
                  Text(popup.error)
               }

               Text(popup.description)
            }
         },

         confirmButton = {
            Button(onClick = {
               popup.code()
               loginViewModel.onPopupDismissed()
            }) {
               Text("OK")
            }
         })
   }

   Scaffold(topBar = {
      CenterAlignedTopAppBar(title = { Text("Login Screen") },

         navigationIcon = {
            BackIconButton(onClick = {
               navController.navigate(Destinations.Tasks.route)
            })
         })
   },

      content = { innerPadding ->
         Column(modifier = Modifier.padding(innerPadding),

            content = {
               Login(loginViewModel, navController)
            })
      })
}

@Composable
fun Login(loginViewModel: LoginViewModel, navController: NavController) {
   val username: String by loginViewModel.username.collectAsStateWithLifecycle()
   val password: String by loginViewModel.password.collectAsStateWithLifecycle()

   Column(content = {
      OutlinedTextField(modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),
         value = username,
         enabled = true,
         onValueChange = { loginViewModel.onUsernameChanged(it) },
         label = { Text(text = "Username") },
         leadingIcon = { Icon(Icons.Default.Person, null) })

      OutlinedTextField(
         value = password,
         visualTransformation = PasswordVisualTransformation(),
         onValueChange = { loginViewModel.onPasswordChanged(it) },
         singleLine = true,
         label = { Text("Password") },
         leadingIcon = { Icon(Icons.Filled.Lock, null) },
         modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
      )

      Spacer(modifier = Modifier.padding(16.dp))

      Button(
         content = {
            Text("Login")
         },

         onClick = {
            loginViewModel.onLoginButtonPress() {
               navController.navigate(Destinations.Tasks.route)
            }
         },

         modifier = Modifier.fillMaxWidth()
      )

      Text(
         modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("register") },

         text = "Haz click aqui para registrarte!",
         style = TextStyle(textAlign = TextAlign.Right)
      )
   })
}