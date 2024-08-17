package com.example.superagenda.presentation.screens.profile

import BeautifulTitle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.superagenda.presentation.screens.profile.composables.DeleteButton
import com.example.superagenda.presentation.screens.profile.composables.ImportTaskList
import com.example.superagenda.presentation.screens.profile.composables.LogoutButton
import com.example.superagenda.presentation.screens.profile.composables.UsernameTextField

@Composable
fun ProfileScreen(
   profileViewModel: ProfileViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel
) {
   val popupsQueue: List<Pair<String, String>> by profileViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(popupsQueue.first().first, popupsQueue.first().second) {
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
            UsernameTextField(username = it.username) {}
         }

         Spacer(modifier = Modifier.padding(16.dp))

         DeleteButton {
            profileViewModel.onDeleteButtonPressButton(navController)
         }

         LogoutButton {
            profileViewModel.onLogoutPress(navController)
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