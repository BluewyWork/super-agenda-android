package com.example.superagenda.presentation.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.composables.BackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
   val popupsQueue by registerViewModel.popups.collectAsStateWithLifecycle()

   if (popupsQueue.isNotEmpty()) {
      val popup = popupsQueue.first()

      AlertDialog(
         onDismissRequest = {
            popup.code()
            registerViewModel.onPopupDismissed()
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
            Button(
               onClick = {
                  popup.code()
                  registerViewModel.onPopupDismissed()
               }
            ) {
               Text("OK")
            }
         }
      )
   }

   Scaffold(
      topBar = {
         CenterAlignedTopAppBar(
            title = { Text("Register Screen") },
            navigationIcon = {
               BackIconButton {
                  navController.navigate(Destinations.Tasks.route)
               }
            }
         )
      }
   ) { innerPadding ->
      Column(modifier = Modifier.padding(innerPadding)) {
         Register(registerViewModel, navController)
      }
   }
}

@Composable
fun Register(registerViewModel: RegisterViewModel, navController: NavController) {
   val username by registerViewModel.username.collectAsStateWithLifecycle()
   val password by registerViewModel.password.collectAsStateWithLifecycle()
   val passwordConfirm by registerViewModel.passwordConfirm.collectAsStateWithLifecycle()

   OutlinedTextField(
      modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),
      value = username,
      onValueChange = { registerViewModel.onUsernameChanged(it) },
      label = { Text(text = "Username") },
      leadingIcon = { Icon(Icons.Default.Person, null) }
   )

   val matches = password != passwordConfirm

   OutlinedTextField(
      modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),

      value = password,
      visualTransformation = PasswordVisualTransformation(),
      onValueChange = { registerViewModel.onPasswordChanged(it) },
      singleLine = true,
      label = { Text(text = "Password") },
      leadingIcon = { Icon(Icons.Filled.Lock, null) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      isError = matches
   )

   OutlinedTextField(
      modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),
      value = passwordConfirm,
      visualTransformation = PasswordVisualTransformation(),
      onValueChange = { registerViewModel.onPasswordConfirmChanged(it) },
      label = { Text("Confirm Password") },
      leadingIcon = { Icon(Icons.Filled.Lock, null) },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      isError = matches
   )

   if (matches) {
      Text("Passwords does not match", color = Color(80, 0, 0))
   }

   Spacer(modifier = Modifier.padding(16.dp))

   Button(
      onClick = { registerViewModel.onRegisterButtonPressed(navController) },
      modifier = Modifier.fillMaxWidth()
   ) {
      Text("Register")
   }

   Text(
      modifier = Modifier
         .fillMaxWidth()
         .padding(top = 5.dp)
         .clickable { navController.navigate(Destinations.Login.route) },
      text = "Haz click aqui para logearte!",
      style = TextStyle(textAlign = TextAlign.Right)
   )
}