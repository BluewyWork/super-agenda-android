package com.example.superagenda.presentation.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
   profileViewModel: ProfileViewModel,
   navController: NavController,
) {
   val popups by profileViewModel.popupsQueue.collectAsStateWithLifecycle()

   if (popups.isNotEmpty()) {
      val popup = popups.first()

      AlertDialog(onDismissRequest = {
         popup.code()
         profileViewModel.onPopupDismissed()
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
               profileViewModel.onPopupDismissed()
            }) {
               Text("OK")
            }
         })
   }

   Profile(profileViewModel, navController)
}

@Composable
fun Profile(
   profileViewModel: ProfileViewModel,
   navController: NavController,
) {
   val userForProfile by profileViewModel.userForProfile.collectAsStateWithLifecycle()

   LazyColumn(
      modifier = Modifier.fillMaxSize(),

      contentPadding = PaddingValues(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      item {
         OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),

            value = userForProfile.username,
            readOnly = true,
            onValueChange = {},
            label = { Text(text = "Username") },
            leadingIcon = { Icon(Icons.Default.Person, null) })

         OutlinedTextField(
            label = { Text("Plan") },
            value = userForProfile?.membership.toString(),
            onValueChange = {},
            readOnly = true,

            modifier = Modifier
               .fillMaxWidth()
               .padding(start = 8.dp, end = 8.dp),
         )

         Spacer(modifier = Modifier.padding(16.dp))

         Button(
            onClick = {
               profileViewModel.onRefreshProfilePress()
            }, modifier = Modifier.fillMaxWidth()
         ) {
            Text("Refresh Profile")
         }

         Button(
            onClick = { profileViewModel.onDeleteButtonPressButton(navController) },
            modifier = Modifier.fillMaxWidth(),
         ) {
            Text("Delete Profile")
         }

         Button(
            onClick = { profileViewModel.onLogoutPressed(navController) },
            modifier = Modifier.fillMaxWidth()
         ) {
            Text("Logout")
         }
      }
   }
}