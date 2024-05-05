package com.example.superagenda.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.tasks.TasksScreen
import com.example.superagenda.presentation.screens.tasks.TasksViewModel
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedScreen
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedScreen
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingScreen
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingViewModel

@Composable
fun NavigationHost(
    profileViewModel: ProfileViewModel,
    tasksViewModel: TasksViewModel,
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    tasksOngoingViewModel: TasksOngoingViewModel,
    tasksCompletedViewModel: TasksCompletedViewModel
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
        composable(Destinations.TasksNotStarted.route) {
            TasksNotStartedScreen(tasksNotStartedViewModel, navController)
        }
        composable(Destinations.TasksOngoing.route) {
            TasksOngoingScreen(tasksOngoingViewModel, navController)
        }
        composable(Destinations.TasksCompleted.route) {
            TasksCompletedScreen(tasksCompletedViewModel, navController)
        }
    }
}