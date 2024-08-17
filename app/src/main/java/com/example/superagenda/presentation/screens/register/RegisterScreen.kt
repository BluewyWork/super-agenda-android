package com.example.superagenda.presentation.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.PopupDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navController: NavController) {
   val popupsQueue: List<Pair<String, String>> by registerViewModel.popupsQueue.observeAsState(
      listOf()
   )
   if (popupsQueue.isNotEmpty()) {
      PopupDialog(popupsQueue.first().first, popupsQueue.first().second) {
         registerViewModel.dismissPopup()
      }
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
   val username: String by registerViewModel.username.observeAsState("")
   val password: String by registerViewModel.password.observeAsState("")

   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))
   Spacer(modifier = Modifier.padding(16.dp))

   TextField(
      modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),
      value = username,
      onValueChange = { registerViewModel.onUsernameChange(it) },
      label = { Text(text = "Username") },
      leadingIcon = { Icon(Icons.Default.Person, null) }
   )

   TextField(
      modifier = Modifier
         .fillMaxWidth()
         .padding(start = 8.dp, end = 8.dp),

      value = password,
      visualTransformation = PasswordVisualTransformation(),
      onValueChange = { registerViewModel.onPasswordChange(it) },
      singleLine = true,
      label = { Text(text = "Password") },
      leadingIcon = { Icon(Icons.Filled.Lock, null) }
   )

   Spacer(modifier = Modifier.padding(16.dp))

   Button(
      onClick = { registerViewModel.onRegisterButtonPress(navController) },
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