package com.example.superagenda.presentation.screens.tasksOnGoing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.presentation.composables.NavigationBar

@Composable
fun TasksOngoingScreen(
    tasksOngoingViewModel: TasksOngoingViewModel,
    navController: NavController
) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
        }
    }
}