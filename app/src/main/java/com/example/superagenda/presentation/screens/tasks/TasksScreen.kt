package com.example.superagenda.presentation.screens.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun TasksScreen(tasksViewModel: TasksViewModel, navController: NavController) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
        ) {
            Tasks()
        }
    }
}

@Composable
fun Tasks() {
    val task =
        Task(_id = ObjectId(), title = "hi", description = "hi", status = TaskStatus.NotStarted)

    LazyColumn {
        item {
            LazyRow {
                item {
                    for (i in 1..20) {
                        TaskCard(task)
                    }
                }
            }

            LazyRow {
                item {
                    for (i in 1..20) {
                        TaskCard(task)
                    }
                }
            }

            LazyRow {
                item {
                    for (i in 1..20) {
                        TaskCard(task)
                    }
                }
            }
        }
    }
}