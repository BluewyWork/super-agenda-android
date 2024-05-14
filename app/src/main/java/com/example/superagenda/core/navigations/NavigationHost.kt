package com.example.superagenda.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.screens.login.LoginScreen
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.shared.taskEdit.TaskEditScreen
import com.example.superagenda.presentation.screens.shared.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.shared.tasksCompleted.TasksCompletedScreen
import com.example.superagenda.presentation.screens.shared.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.shared.tasksNotStarted.TasksNotStartedScreen
import com.example.superagenda.presentation.screens.shared.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.shared.tasksOnGoing.TasksOngoingScreen
import com.example.superagenda.presentation.screens.shared.tasksOnGoing.TasksOngoingViewModel

@Composable
fun NavigationHost(
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    tasksOngoingViewModel: TasksOngoingViewModel,
    tasksCompletedViewModel: TasksCompletedViewModel,
    taskEditViewModel: TaskEditViewModel,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route
    ) {
        composable(Destinations.Login.route) {
            loginViewModel.onShow()
            LoginScreen(loginViewModel, navController)
        }
        composable(Destinations.Profile.route) {
            profileViewModel.onShow()
            ProfileScreen(profileViewModel, navController)
        }
        composable(Destinations.TasksNotStarted.route) {
            tasksNotStartedViewModel.onShow()
            TasksNotStartedScreen(tasksNotStartedViewModel, navController)
        }
        composable(Destinations.TasksOngoing.route) {
            tasksOngoingViewModel.onShow()
            TasksOngoingScreen(tasksOngoingViewModel, navController)
        }
        composable(Destinations.TasksCompleted.route) {
            tasksCompletedViewModel.onShow()
            TasksCompletedScreen(tasksCompletedViewModel, navController)
        }
        composable(Destinations.TaskEdit.route) {
            taskEditViewModel.onShow()
            TaskEditScreen(taskEditViewModel, navController)
        }
    }
}