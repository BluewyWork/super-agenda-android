package com.example.superagenda.core.navigations

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.screens.filter.FilterScreen
import com.example.superagenda.presentation.screens.filter.FilterScreenViewModel
import com.example.superagenda.presentation.screens.login.LoginScreen
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.newTask.NewTaskScreen
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.presentation.screens.other.OtherScreen
import com.example.superagenda.presentation.screens.other.OtherViewModel
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.register.RegisterScreen
import com.example.superagenda.presentation.screens.register.RegisterViewModel
import com.example.superagenda.presentation.screens.taskEdit.TaskEditScreen
import com.example.superagenda.presentation.screens.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.tasks.TasksScreen
import com.example.superagenda.presentation.screens.tasks.TasksViewModel

@Composable
fun NavigationHost(
   registerViewModel: RegisterViewModel,
   loginViewModel: LoginViewModel,
   profileViewModel: ProfileViewModel,
   tasksViewModel: TasksViewModel,
   taskEditViewModel: TaskEditViewModel,
   newTaskViewModel: NewTaskViewModel,
   filterViewModel: FilterScreenViewModel,
   navigationViewModel: NavigationViewModel,
   otherViewModel: OtherViewModel,
) {
   val navController = rememberNavController()

   NavHost(
      navController = navController,
      startDestination = Destinations.Tasks.route
   ) {
      composable(Destinations.Login.route) {
         LoginScreen(loginViewModel, navController)
      }

      composable(Destinations.Register.route) {
         RegisterScreen(registerViewModel, navController)
      }

      composable(Destinations.Profile.route) {
         profileViewModel.onShow()
         ProfileScreen(profileViewModel, navController, navigationViewModel)
      }

      composable(Destinations.TaskEdit.route) {
         val task = tasksViewModel.taskToEdit.value
         if (task == null) {
            navController.navigateUp()
            return@composable
         }

         taskEditViewModel.setTaskToEdit(task)
         taskEditViewModel.onShow(navController)
         TaskEditScreen(taskEditViewModel, navController, navigationViewModel)
      }

      composable(Destinations.TaskEdit.route + "2") {
         val task = filterViewModel.taskToEdit.value
         if (task == null) {
            navController.navigateUp()
            return@composable
         }

         taskEditViewModel.setTaskToEdit(task)
         taskEditViewModel.onShow(navController)
         TaskEditScreen(taskEditViewModel, navController, navigationViewModel)
      }

      composable(Destinations.NewTask.route) {
         newTaskViewModel.onShow()
         NewTaskScreen(newTaskViewModel, navController, navigationViewModel)
      }

      composable(Destinations.Filter.route) {
         FilterScreen(filterViewModel, navController, navigationViewModel)
      }

      composable(Destinations.Tasks.route) {
         TasksScreen(tasksViewModel, navController, navigationViewModel)
      }

      composable(Destinations.Other.route) {
         OtherScreen(otherViewModel, navController, navigationViewModel)
      }
   }
}