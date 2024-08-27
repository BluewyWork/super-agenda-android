package com.example.superagenda.presentation.screens.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.presentation.composables.LoadingPopup
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.other.composables.ImportTaskList

@Composable
fun OtherScreen(
   otherViewModel: OtherViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
   val showLoadingPopup: Boolean by otherViewModel.showLoadingPopup.observeAsState(false)

   if (showLoadingPopup) {
      LoadingPopup {
         otherViewModel.dismissPopup()
      }
   }

   val popupsQueue: List<Pair<String, String>> by otherViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second
      ) {
         otherViewModel.dismissPopup()
      }
   }

   Navigation(
      navController = navController,
      topBarTitle = "Other",
      navigationViewModel = navigationViewModel
   ) { paddingValues ->
      Box(modifier = Modifier.padding(paddingValues)) {
         Other(otherViewModel = otherViewModel)
      }
   }
}

@Composable
fun Other(otherViewModel: OtherViewModel) {
   LazyColumn(
      modifier = Modifier.padding(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      item {
         Button(
            onClick = { otherViewModel.onBackUpButtonPress() },
            modifier = Modifier.fillMaxWidth()
         ) {
            Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
               Text("Make a Backup")
            }
         }
      }

      item {
         ImportTaskList(modifier = Modifier.fillMaxWidth()) { contentResolver, filePath ->
            otherViewModel.onImportButtonPress(contentResolver, filePath)
         }
      }
   }
}