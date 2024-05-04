package com.example.superagenda.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.tasks.TasksScreen
import com.example.superagenda.presentation.screens.tasks.TasksViewModel

@Composable
fun NavigationHost(
    profileViewModel: ProfileViewModel,
    tasksViewModel: TasksViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Profile.route
    ) {
        composable(Destinations.Profile.route) {
            ProfileScreen(profileViewModel, navController)
        }
        composable(Destinations.Tasks.route) {
            TasksScreen(tasksViewModel, navController)
        }
    }
}