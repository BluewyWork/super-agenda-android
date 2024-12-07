package com.example.superagenda.presentation.screens.initial

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations

@Composable
fun InitialScreen(initialViewModel: InitialViewModel, navController: NavController) {
   val showLoading by initialViewModel.showLoading.collectAsStateWithLifecycle()
   val done by initialViewModel.done.collectAsStateWithLifecycle()

   if (showLoading) {
      Box(
         contentAlignment = Alignment.Center
      ) {
         CircularProgressIndicator()
      }
   }

   when (done) {
      TheDecision.NAV_SLIDER -> navController.navigate(Destinations.Slider.route)
      TheDecision.NAV_TASKS -> navController.navigate(Destinations.Tasks.route)
      TheDecision.UNDECIDED -> {}
   }
}