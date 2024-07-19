package com.example.superagenda.presentation.screens.taskOverview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.composables.Navigation

@Composable
fun TasksOverviewScreen(
    tasksOverviewViewModel: TasksOverviewViewModel,
    navController: NavController
) {
    Navigation(content = { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TasksOverview(tasksOverviewViewModel, navController)
        }
    }, navController, "Overview")
}

@Composable
fun TasksOverview(tasksOverviewViewModel: TasksOverviewViewModel, navController: NavController) {
    val tasksNotStarted by tasksOverviewViewModel.tasksNotStarted.observeAsState()
    val tasksOngoing by tasksOverviewViewModel.tasksOngoing.observeAsState()
    val tasksCompleted by tasksOverviewViewModel.taskCompleted.observeAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = "Not Started",
                modifier = Modifier.clickable {
                    navController.navigate(Destinations.TasksNotStarted.route)
                }
            )

            tasksNotStarted?.forEach { task ->
                SmallTaskCard(task = task) {
                    tasksOverviewViewModel.onEditClick(task)
                    navController.navigate(Destinations.TaskEdit.route)
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = "Ongoing",
                modifier = Modifier.clickable {
                    navController.navigate(Destinations.TasksOngoing.route)
                }
            )
            tasksOngoing?.forEach { task ->
                SmallTaskCard(task = task) {
                    tasksOverviewViewModel.onEditClick(task)
                    navController.navigate(Destinations.TaskEdit.route)
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = "Completed",
                modifier = Modifier.clickable {
                    navController.navigate(Destinations.TasksCompleted.route)
                }
            )
            tasksCompleted?.forEach { task ->
                SmallTaskCard(task = task) {
                    tasksOverviewViewModel.onEditClick(task)
                    navController.navigate(Destinations.TaskEdit.route)
                }
            }
        }
    }
}

@Composable
fun SmallTaskCard(task: Task, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = task.title,
                maxLines = 1
            )
            Text(
                text = task.description,
                maxLines = 2
            )
        }
    }
}
