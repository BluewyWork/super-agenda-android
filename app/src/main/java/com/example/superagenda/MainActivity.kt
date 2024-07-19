package com.example.superagenda

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.superagenda.core.NotificationService
import com.example.superagenda.core.navigations.NavigationHost
import com.example.superagenda.presentation.screens.filter.FilterScreenViewModel
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.register.RegisterViewModel
import com.example.superagenda.presentation.screens.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.taskOverview.TasksOverviewViewModel
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingViewModel
import com.example.superagenda.ui.theme.SuperAgendaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   private val registerViewModel: RegisterViewModel by viewModels()
   private val loginViewModel: LoginViewModel by viewModels()
   private val profileViewModel: ProfileViewModel by viewModels()
   private val tasksNotStartedViewModel: TasksNotStartedViewModel by viewModels()
   private val tasksOngoingViewModel: TasksOngoingViewModel by viewModels()
   private val tasksCompletedViewModel: TasksCompletedViewModel by viewModels()
   private val taskEditViewModel: TaskEditViewModel by viewModels()
   private val newTaskViewModel: NewTaskViewModel by viewModels()
   private val tasksOverviewViewModel: TasksOverviewViewModel by viewModels()
   private val filterScreenViewModel: FilterScreenViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      createNotificationChannel()
      super.onCreate(savedInstanceState)

      val service = NotificationService(applicationContext)

      setContent {
         SuperAgendaTheme {
            NavigationHost(
               service = service,
               registerViewModel = registerViewModel,
               loginViewModel = loginViewModel,
               profileViewModel = profileViewModel,
               tasksNotStartedViewModel = tasksNotStartedViewModel,
               tasksOngoingViewModel = tasksOngoingViewModel,
               tasksCompletedViewModel = tasksCompletedViewModel,
               taskEditViewModel = taskEditViewModel,
               newTaskViewModel = newTaskViewModel,
               tasksOverviewViewModel = tasksOverviewViewModel,
               filterScreenViewModel = filterScreenViewModel
            )

         }
      }
   }

   private fun createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         val channel = NotificationChannel(
            NotificationService.CHANNEL_ID,
            "Random Name",
            NotificationManager.IMPORTANCE_DEFAULT
         )

         channel.description = "This is an example description."

         val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         notificationManager.createNotificationChannel(channel)
      }
   }
}

