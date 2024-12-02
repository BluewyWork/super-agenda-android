package com.example.superagenda.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.NewTaskFloatingActionButton
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
import com.example.superagenda.presentation.screens.slider.SliderScreen
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
      startDestination = Destinations.Slider.route
   ) {
      composable(Destinations.Slider.route) {
         SliderScreen(navController)
      }

      composable(Destinations.Login.route) {
         LoginScreen(loginViewModel, navController)
      }

      composable(Destinations.Register.route) {
         RegisterScreen(registerViewModel, navController)
      }

      composable(Destinations.Profile.route) {
         Navigation(
            navController = navController,
            topBarTitle = "Profile",
            navigationViewModel = navigationViewModel
         ) {
            profileViewModel.onShow()
            ProfileScreen(profileViewModel, navController, navigationViewModel)
         }
      }

      composable(Destinations.TaskEdit.route) {
         val taskToEdit = tasksViewModel.getTaskToEdit()

         if (taskToEdit == null) {
            navController.navigate(Destinations.Tasks.route)
            return@composable
         }

         taskEditViewModel.setTaskId(taskToEdit.id)
         taskEditViewModel.setTitle(taskToEdit.title)
         taskEditViewModel.setDescription(taskToEdit.description)
         taskEditViewModel.setTaskStatus(taskToEdit.status)
         taskEditViewModel.setStartDateTime(taskToEdit.startDateTime)
         taskEditViewModel.setEndDateTime(taskToEdit.endEstimatedDateTime)
         taskEditViewModel.setImages(taskToEdit.images)

         Navigation(
            navController = navController,
            topBarTitle = "TASK EDIT",
            navigationIcon = { BackIconButton(onClick = { navController.navigateUp() }) },
            navigationViewModel = navigationViewModel
         ) {
            TaskEditScreen(taskEditViewModel, navController, navigationViewModel)
         }
      }

      composable(Destinations.NewTask.route) {
         Navigation(
            navController = navController,
            topBarTitle = "New Task",
            navigationIcon = { BackIconButton(onClick = { navController.navigateUp() }) },
            navigationViewModel = navigationViewModel
         ) {
            NewTaskScreen(newTaskViewModel, navController, navigationViewModel)
         }
      }

      composable(Destinations.Filter.route) {
         Navigation(
            navController = navController,
            topBarTitle = "Find Tasks",
            navigationViewModel = navigationViewModel
         ) {
            FilterScreen(filterViewModel, navController, navigationViewModel)
         }
      }

      composable(Destinations.Tasks.route) {
         Navigation(
            navController,
            topBarTitle = "TASKS",

            floatingActionButton = {
               NewTaskFloatingActionButton {
                  navController.navigate(Destinations.NewTask.route)
               }
            },

            navigationIcon = {},
            navigationViewModel
         ) {
            TasksScreen(tasksViewModel, navController)
         }
      }

      composable(Destinations.Other.route) {
         Navigation(
            navController = navController,
            topBarTitle = "Other",
            navigationViewModel = navigationViewModel
         ) {
            OtherScreen(otherViewModel, navController, navigationViewModel)
         }
      }
   }
}