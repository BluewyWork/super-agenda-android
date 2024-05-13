package com.example.superagenda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.superagenda.core.navigations.NavigationHost
import com.example.superagenda.presentation.screens.login.LoginViewModel
import com.example.superagenda.presentation.screens.profile.ProfileViewModel
import com.example.superagenda.presentation.screens.tasks.TasksViewModel
import com.example.superagenda.presentation.screens.tasksCompleted.TasksCompletedViewModel
import com.example.superagenda.presentation.screens.tasksNotStarted.TasksNotStartedViewModel
import com.example.superagenda.presentation.screens.tasksOnGoing.TasksOngoingViewModel
import com.example.superagenda.ui.theme.SuperAgendaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//    private val loginViewModel: LoginViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val tasksNotStartedViewModel: TasksNotStartedViewModel by viewModels()
    private val tasksOngoingViewModel: TasksOngoingViewModel by viewModels()
    private val tasksCompletedViewModel: TasksCompletedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.onCreate()

        setContent {
            SuperAgendaTheme {
                NavigationHost(
//                    loginViewModel = loginViewModel,
                    profileViewModel = profileViewModel,
                    tasksViewModel = tasksViewModel,
                    tasksNotStartedViewModel = tasksNotStartedViewModel,
                    tasksOngoingViewModel = tasksOngoingViewModel,
                    tasksCompletedViewModel = tasksCompletedViewModel
                )
            }
        }
    }
}