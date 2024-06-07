package com.example.superagenda.presentation.screens.tasksNotStarted

import BeautifulTitle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.composables.TaskCard

@Composable
fun TasksNotStartedScreen(
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    navController: NavController
) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            BeautifulTitle(title = "TASKS (NOT STARTED)")
            TasksNotStarted(tasksNotStartedViewModel, navController)
        }
    }
}

@Composable
fun TasksNotStarted(
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    navController: NavController
) {
    val notStartedTaskList: List<Task>? by tasksNotStartedViewModel.notStartedTaskList.observeAsState()

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            notStartedTaskList.let {
                if (it != null) {
                    for (task in it) {
                        TaskCard(task) {
                            tasksNotStartedViewModel.onEditClick(task)
                            navController.navigate(Destinations.TaskEdit.route)
                        }
                    }
                } else {
                    Text("Either no tasks or no internet connection...")
                }
            }
        }
    }
}