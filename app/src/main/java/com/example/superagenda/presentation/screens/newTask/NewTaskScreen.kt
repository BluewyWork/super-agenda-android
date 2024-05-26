package com.example.superagenda.presentation.screens.newTask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.newTask.composables.DateTimePicker
import com.example.superagenda.presentation.screens.newTask.composables.DescriptionTextField
import com.example.superagenda.presentation.screens.newTask.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.newTask.composables.TitleTextField
import com.example.superagenda.presentation.screens.newTask.composables.UpdateButton
import java.time.LocalDateTime

@Composable
fun NewTaskScreen(newTaskViewModel: NewTaskViewModel, navController: NavController) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            Text(text = "Edit your task: ")
            NewTask(newTaskViewModel)
            UpdateButton {
                newTaskViewModel.onCreateButtonPress(navController)
            }
        }
    }
}

@Composable
fun NewTask(newTaskViewModel: NewTaskViewModel) {
    val title: String by newTaskViewModel.title.observeAsState("")
    val description: String by newTaskViewModel.description.observeAsState("")
    val taskStatus: TaskStatus? by newTaskViewModel.taskStatus.observeAsState()
    val startDateTime: LocalDateTime? by newTaskViewModel.startDateTime.observeAsState()
    val endDateTime: LocalDateTime? by newTaskViewModel.endDateTime.observeAsState()

    LazyColumn {
        item {
            TitleTextField(title) {
                newTaskViewModel.onTitleChange(it)
            }
            DescriptionTextField(description) {
                newTaskViewModel.onDescriptionChange(it)
            }
            taskStatus?.let { it1 ->
                TaskStatusDropDown(it1) {
                    newTaskViewModel.onTaskStatusChange(it)
                }
            }
            startDateTime?.let { it3 ->
                DateTimePicker(initialDateTime = it3) { it2 ->
                    newTaskViewModel.onStartDateTimeChange(it2)
                }
            }

            Text("-----------------------------------------")

            endDateTime?.let { it5 ->
                DateTimePicker(initialDateTime = it5) { it2 ->
                    newTaskViewModel.onEndDateTimeChange(it2)

                }
            }
        }
    }
}