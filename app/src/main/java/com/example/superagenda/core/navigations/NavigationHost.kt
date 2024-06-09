package com.example.superagenda.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.core.NotificationService
import com.example.superagenda.presentation.screens.login.LoginScreen
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.newTask.NewTaskScreen
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.presentation.screens.noInternet.NoInternetScreen
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.register.RegisterScreen
import com.example.superagenda.presentation.screens.register.RegisterViewModel
import com.example.superagenda.presentation.screens.taskEdit.TaskEditScreen
import com.example.superagenda.presentation.screens.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.taskOverview.TasksOverviewScreen
import com.example.superagenda.presentation.screens.taskOverview.TasksOverviewViewModel
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedScreen
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedScreen
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingScreen
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingViewModel

@Composable
fun NavigationHost(
    service: NotificationService,
    registerViewModel: RegisterViewModel,
    loginViewModel: LoginViewModel,
    profileViewModel: ProfileViewModel,
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    tasksOngoingViewModel: TasksOngoingViewModel,
    tasksCompletedViewModel: TasksCompletedViewModel,
    taskEditViewModel: TaskEditViewModel,
    newTaskViewModel: NewTaskViewModel,
    tasksOverviewViewModel: TasksOverviewViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route
    ) {
        composable(Destinations.Register.route) {
            RegisterScreen(registerViewModel, navController)
        }
        composable(Destinations.Login.route) {
            loginViewModel.onShow(navController)
            LoginScreen(loginViewModel, navController)
        }
        composable(Destinations.Profile.route) {
            profileViewModel.onShow()
            ProfileScreen(profileViewModel, navController, service)
        }
        composable(Destinations.TasksNotStarted.route) {
            tasksNotStartedViewModel.onShow(service)
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
        composable(Destinations.NewTask.route) {
            newTaskViewModel.onShow()
            NewTaskScreen(newTaskViewModel, navController)
        }
        composable(Destinations.NoInternet.route) {
            NoInternetScreen()
        }
        composable(Destinations.TasksOverview.route) {
            tasksOverviewViewModel.onShow()
            TasksOverviewScreen(tasksOverviewViewModel, navController)
        }
    }
}