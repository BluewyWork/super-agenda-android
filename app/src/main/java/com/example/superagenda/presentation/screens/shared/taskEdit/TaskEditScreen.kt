package com.example.superagenda.presentation.screens.shared.taskEdit

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
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.NavigationBar
import com.example.superagenda.presentation.screens.shared.taskEdit.composables.DescriptionTextField
import com.example.superagenda.presentation.screens.shared.taskEdit.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.shared.taskEdit.composables.TitleTextField
import com.example.superagenda.presentation.screens.shared.taskEdit.composables.UpdateButton

@Composable
fun TaskEditScreen(taskEditViewModel: TaskEditViewModel, navController: NavController) {
    Scaffold(bottomBar = { NavigationBar(navController) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            TaskEdit(taskEditViewModel)
            UpdateButton {
                taskEditViewModel.onUpdateButtonPress()
            }
        }
    }
}

@Composable
fun TaskEdit(taskEditViewModel: TaskEditViewModel) {
    val taskToEdit: Task? by taskEditViewModel.taskToEdit.observeAsState()
    val title: String by taskEditViewModel.title.observeAsState( "")
    val description: String by taskEditViewModel.description.observeAsState("")
    val taskStatus: TaskStatus? by taskEditViewModel.taskStatus.observeAsState()

    LazyColumn {
        item {
            taskToEdit?.let {
                TitleTextField(title) {
                    taskEditViewModel.onTitleChange(it)
                }
                DescriptionTextField(description) {
                    taskEditViewModel.onDescriptionChange(it)
                }
                taskStatus?.let { it1 -> TaskStatusDropDown(it1) }
            }
        }
    }
}