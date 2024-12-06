package com.example.superagenda.presentation.screens.initial

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations

@Composable
fun InitialScreen(initialViewModel: InitialViewModel, navController: NavController) {
   val showLoading by initialViewModel.showLoading.collectAsStateWithLifecycle()

   if (showLoading) {
      CircularProgressIndicator()
   }

   var done = false

   initialViewModel.refreshTasksIfOutdated {
      done = true
   }

   var done2 =  true

   if (done && done2) {
      navController.navigate(Destinations.Tasks.route)
   }
}