package com.example.superagenda.presentation.screens.profile

import BeautifulTitle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.UserForProfile
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.profile.composables.BackupTaskList
import com.example.superagenda.presentation.screens.profile.composables.ImportTaskList

@Composable
fun ProfileScreen(
   profileViewModel: ProfileViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
   val popupsQueue: List<Pair<String, String>> by profileViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second
      ) {
         profileViewModel.dismissPopup()
      }
   }

   Navigation(
      content = { padding ->
         Profile(profileViewModel, navController, padding)
      },

      navController = navController,
      topBarTitle = "Profile",
      navigationViewModel = navigationViewModel
   )
}

@Composable
fun Profile(
   profileViewModel: ProfileViewModel,
   navController: NavController,
   padding: PaddingValues,
) {
   val userForProfile: UserForProfile? by profileViewModel.userForProfile.observeAsState()

   LazyColumn(
      modifier = Modifier
         .padding(padding)
         .fillMaxSize(),

      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      item {
         userForProfile?.let {
            OutlinedTextField(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(start = 8.dp, end = 8.dp),

               value = it.username,
               readOnly = true,
               onValueChange = { it2 -> {} },
               label = { Text(text = "Username") },
               leadingIcon = { Icon(Icons.Default.Person, null) }
            )
         } ?: run {
            OutlinedTextField(value = "Failed to communicate with API...", onValueChange = {})
         }

         Spacer(modifier = Modifier.padding(16.dp))

         Button(
            onClick = { profileViewModel.onDeleteButtonPressButton(navController) },
            modifier = Modifier.fillMaxWidth()
         ) {
            Text("Delete Profile")
         }

         Button(
            onClick = { profileViewModel.onLogoutPress(navController) },
            modifier = Modifier.fillMaxWidth()
         ) {
            Text("Logout")
         }

         Spacer(modifier = Modifier.padding(16.dp))
         Spacer(modifier = Modifier.padding(16.dp))
         Spacer(modifier = Modifier.padding(16.dp))
         Spacer(modifier = Modifier.padding(16.dp))
         Spacer(modifier = Modifier.padding(16.dp))
         Spacer(modifier = Modifier.padding(16.dp))

         BeautifulTitle(title = "OTHER")

         BackupTaskList {
            profileViewModel.onBackupButtonPress()
         }

         ImportTaskList { contentResolver, filePath ->
            profileViewModel.onImportButtonPress(contentResolver, filePath)
         }
      }
   }
}