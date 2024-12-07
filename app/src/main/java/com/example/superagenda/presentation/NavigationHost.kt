package com.example.superagenda.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.FloatingActionButtonPlus
import com.example.superagenda.presentation.screens.filter.FilterScreen
import com.example.superagenda.presentation.screens.filter.FilterScreenViewModel
import com.example.superagenda.presentation.screens.initial.InitialScreen
import com.example.superagenda.presentation.screens.initial.InitialViewModel
import com.example.superagenda.presentation.screens.login.LoginScreen
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.navigation.Navigation
import com.example.superagenda.presentation.screens.navigation.WrapperNavigationViewModel
import com.example.superagenda.presentation.screens.newTask.NewTaskScreen
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.presentation.screens.other.OtherScreen
import com.example.superagenda.presentation.screens.other.OtherViewModel
import com.example.superagenda.presentation.screens.profile.ProfileScreen
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.register.RegisterScreen
import com.example.superagenda.presentation.screens.register.RegisterViewModel
import com.example.superagenda.presentation.screens.slider.SliderScreen
import com.example.superagenda.presentation.screens.slider.SliderScreenViewModel
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
   wrapperNavigationViewModel: WrapperNavigationViewModel,
   otherViewModel: OtherViewModel,
   initialViewModel: InitialViewModel,
   sliderScreenViewModel: SliderScreenViewModel,
) {
   val navController = rememberNavController()

   NavHost(
      navController = navController,
      startDestination = Destinations.Initial.route
   ) {
      composable(Destinations.Initial.route) {
         Scaffold { padding ->
            Box(Modifier.padding(padding)) {
               InitialScreen(initialViewModel, navController)
            }
         }
      }

      composable(Destinations.Slider.route) {
         Scaffold { padding ->
            Box(modifier = Modifier.padding(padding)) {
               SliderScreen(sliderScreenViewModel, navController)
            }
         }
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
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            profileViewModel.onShow()
            ProfileScreen(profileViewModel, navController)
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
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            TaskEditScreen(taskEditViewModel, navController, wrapperNavigationViewModel)
         }
      }

      // This is for the filter screen
      composable(Destinations.TaskEdit.route + "2") {
         val taskToEdit = filterViewModel.getTaskToEdit()

         if (taskToEdit == null) {
            navController.navigate(Destinations.Filter.route)
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
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            TaskEditScreen(taskEditViewModel, navController, wrapperNavigationViewModel)
         }
      }


      composable(Destinations.NewTask.route) {
         Navigation(
            navController = navController,
            topBarTitle = "New Task",
            navigationIcon = { BackIconButton(onClick = { navController.navigateUp() }) },
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            NewTaskScreen(newTaskViewModel, navController)
         }
      }

      composable(Destinations.Filter.route) {
         Navigation(
            navController = navController,
            topBarTitle = "Find Tasks",
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            FilterScreen(filterViewModel, navController)
         }
      }

      composable(Destinations.Tasks.route) {
         Navigation(
            navController,
            topBarTitle = "TASKS",

            floatingActionButton = {
               FloatingActionButtonPlus {
                  navController.navigate(Destinations.NewTask.route)
               }
            },

            navigationIcon = {},
            wrapperNavigationViewModel
         ) {
            TasksScreen(tasksViewModel, navController)
         }
      }

      composable(Destinations.Other.route) {
         Navigation(
            navController = navController,
            topBarTitle = "Other",
            wrapperNavigationViewModel = wrapperNavigationViewModel
         ) {
            OtherScreen(otherViewModel)
         }
      }
   }
}