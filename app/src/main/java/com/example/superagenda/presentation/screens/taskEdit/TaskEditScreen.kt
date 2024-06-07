package com.example.superagenda.presentation.screens.taskEdit

import BeautifulTitle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.ErrorDialog
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.profile.composables.DeleteButton
import com.example.superagenda.presentation.screens.taskEdit.composables.DateTimePicker
import com.example.superagenda.presentation.screens.taskEdit.composables.DescriptionTextField
import com.example.superagenda.presentation.screens.taskEdit.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.taskEdit.composables.TitleTextField
import com.example.superagenda.presentation.screens.taskEdit.composables.UpdateButton
import java.time.LocalDateTime

@Composable
fun TaskEditScreen(taskEditViewModel: TaskEditViewModel, navController: NavController) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            BeautifulTitle(title = "TASK: EDIT")
            Spacer(modifier = Modifier.padding(8.dp))
            TaskEdit(taskEditViewModel)
            DeleteButton {
                taskEditViewModel.onDeleteButtonPress(navController)
            }
            UpdateButton {
                taskEditViewModel.onUpdateButtonPress(navController)
            }
        }

        val errorMessage: String? by taskEditViewModel.errorMessage.observeAsState(null)

        if (errorMessage != null) {
            ErrorDialog(errorMessage = errorMessage) {
                taskEditViewModel.onErrorDismissed()
            }
        }
    }
}

@Composable
fun TaskEdit(taskEditViewModel: TaskEditViewModel) {
    val taskToEdit: Task? by taskEditViewModel.taskToEdit.observeAsState()
    val title: String by taskEditViewModel.title.observeAsState("")
    val description: String by taskEditViewModel.description.observeAsState("")
    val taskStatus: TaskStatus? by taskEditViewModel.taskStatus.observeAsState()
    val startDateTime: LocalDateTime? by taskEditViewModel.startDateTime.observeAsState()
    val endDateTime: LocalDateTime? by taskEditViewModel.endDateTime.observeAsState()

    LazyColumn {
        item {
            taskToEdit?.let {
                TitleTextField(title) {
                    taskEditViewModel.onTitleChange(it)
                }
                DescriptionTextField(description) {
                    taskEditViewModel.onDescriptionChange(it)
                }
                taskStatus?.let { it1 ->
                    TaskStatusDropDown(it1) {
                        taskEditViewModel.onTaskStatusChange(it)
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "START DATETIME")
                startDateTime?.let { it3 ->
                    DateTimePicker(initialDateTime = it3) { it2 ->
                        taskEditViewModel.onStartDateTimeChange(it2)
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "END DATETIME")
                endDateTime?.let { it5 ->
                    DateTimePicker(initialDateTime = it5) { it2 ->
                        taskEditViewModel.onEndDateTimeChange(it2)

                    }
                }
            }
        }
    }
}