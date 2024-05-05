package com.example.superagenda.presentation.screens.tasksNotStarted

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.tasks.composables.TaskCard
import org.bson.types.ObjectId

@Composable
fun TasksNotStartedScreen(
    tasksNotStartedViewModel: TasksNotStartedViewModel,
    navController: NavController
) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TasksNotStarted()
        }
    }
}

@Composable
fun TasksNotStarted() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            for (x in 1..25) {
                val task = Task(ObjectId(), "Title", "Description", TaskStatus.NotStarted)

                TaskCard(task = task)
            }
        }
    }
}