package com.example.superagenda.presentation.screens.taskEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.screens.profile.composables.DeleteButton
import com.example.superagenda.presentation.screens.taskEdit.composables.DateTimePicker
import com.example.superagenda.presentation.screens.taskEdit.composables.DescriptionTextField
import com.example.superagenda.presentation.screens.taskEdit.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.taskEdit.composables.TitleTextField
import com.example.superagenda.presentation.screens.taskEdit.composables.UpdateButton
import java.time.LocalDateTime

@Composable
fun TaskEditScreen(taskEditViewModel: TaskEditViewModel, navController: NavController) {
   Navigation(
      content = { padding ->
         Column(modifier = Modifier.padding(padding)) {
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

//         if (errorMessage != null)
//         {
//            PopupDialog(message = errorMessage) {
//               taskEditViewModel.onErrorDismissed()
//            }
//         }

      },
      navController,
      "Edit Task",
      navigationIcon = { BackIconButton(onClick = { navController.popBackStack() }) },
   )
}

@Composable
fun TaskEdit(taskEditViewModel: TaskEditViewModel) {
   val title: String? by taskEditViewModel.title.observeAsState()
   val description: String? by taskEditViewModel.description.observeAsState()
   val taskStatus: TaskStatus? by taskEditViewModel.taskStatus.observeAsState()
   val startDateTime: LocalDateTime? by taskEditViewModel.startDateTime.observeAsState()
   val endDateTime: LocalDateTime? by taskEditViewModel.endDateTime.observeAsState()

   LazyColumn {
      item {
         title?.let { it ->
            TitleTextField(it) {
               taskEditViewModel.onTitleChange(it)
            }
         }

         description?.let { it ->
            DescriptionTextField(it) {
               taskEditViewModel.onDescriptionChange(it)
            }
         }

         taskStatus?.let { it ->
            TaskStatusDropDown(it) {
               taskEditViewModel.onTaskStatusChange(it)
            }
         }

         Spacer(modifier = Modifier.padding(8.dp))
         Text(text = "START DATETIME")

         startDateTime?.let {
            DateTimePicker(initialDateTime = it) { it2 ->
               taskEditViewModel.onStartDateTimeChange(it2)
            }
         }

         Spacer(modifier = Modifier.padding(8.dp))
         Text(text = "END DATETIME")

         endDateTime?.let {
            DateTimePicker(initialDateTime = it) { it2 ->
               taskEditViewModel.onEndDateTimeChange(it2)

            }
         }
      }
   }
}