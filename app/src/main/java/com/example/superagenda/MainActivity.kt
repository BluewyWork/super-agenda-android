package com.example.superagenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.NavigationHost
import com.example.superagenda.presentation.screens.navigation.WrapperNavigationViewModel
import com.example.superagenda.presentation.screens.filter.FilterScreenViewModel
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.presentation.screens.other.OtherViewModel
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.register.RegisterViewModel
import com.example.superagenda.presentation.screens.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.tasks.TasksViewModel
import com.example.superagenda.ui.theme.OneDarkProTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   private val registerViewModel: RegisterViewModel by viewModels()
   private val loginViewModel: LoginViewModel by viewModels()
   private val profileViewModel: ProfileViewModel by viewModels()
   private val tasksViewModel: TasksViewModel by viewModels()
   private val taskEditViewModel: TaskEditViewModel by viewModels()
   private val newTaskViewModel: NewTaskViewModel by viewModels()
   private val filterViewModel: FilterScreenViewModel by viewModels()
   private val wrapperNavigationViewModel: WrapperNavigationViewModel by viewModels()
   private val otherViewModel: OtherViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      setContent {
         OneDarkProTheme {
            NavigationHost(
               registerViewModel = registerViewModel,
               loginViewModel = loginViewModel,
               profileViewModel = profileViewModel,
               tasksViewModel = tasksViewModel,
               taskEditViewModel = taskEditViewModel,
               newTaskViewModel = newTaskViewModel,
               filterViewModel = filterViewModel,
               wrapperNavigationViewModel = wrapperNavigationViewModel,
               otherViewModel = otherViewModel
            )
         }
      }
   }
}

