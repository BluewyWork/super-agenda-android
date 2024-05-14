package com.example.superagenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.core.navigations.NavigationHost
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.shared.taskEdit.TaskEditViewModel
import com.example.superagenda.presentation.screens.shared.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.shared.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.shared.tasksOnGoing.TasksOngoingViewModel
import com.example.superagenda.ui.theme.SuperAgendaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val tasksNotStartedViewModel: TasksNotStartedViewModel by viewModels()
    private val tasksOngoingViewModel: TasksOngoingViewModel by viewModels()
    private val tasksCompletedViewModel: TasksCompletedViewModel by viewModels()
    private val taskEditViewModel: TaskEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SuperAgendaTheme {
                NavigationHost(
                    loginViewModel = loginViewModel,
                    profileViewModel = profileViewModel,
                    tasksNotStartedViewModel = tasksNotStartedViewModel,
                    tasksOngoingViewModel = tasksOngoingViewModel,
                    tasksCompletedViewModel = tasksCompletedViewModel,
                    taskEditViewModel = taskEditViewModel
                )
            }
        }
    }
}