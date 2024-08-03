package com.example.superagenda.presentation.screens.newTask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.newTask.composables.DateTimePicker
import com.example.superagenda.presentation.screens.newTask.composables.DescriptionTextField
import com.example.superagenda.presentation.screens.newTask.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.newTask.composables.TitleTextField
import com.example.superagenda.presentation.screens.newTask.composables.UpdateButton
import java.time.LocalDateTime

@Composable
fun NewTaskScreen(newTaskViewModel: NewTaskViewModel, navController: NavController) {
   val showPopup: Boolean by newTaskViewModel.showPopup.observeAsState(initial = false)
   val popupTitle: String by newTaskViewModel.popupTitle.observeAsState(initial = "")
   val popupMessage: String by newTaskViewModel.popupMessage.observeAsState(initial = "")

   if (showPopup) {
      PopupDialog(popupTitle, popupMessage) {
         newTaskViewModel.dismissPopup()
      }
   }

   Navigation(content = { padding ->
      Column(modifier = Modifier.padding(padding)) {
         NewTask(newTaskViewModel)
         UpdateButton {
            newTaskViewModel.onCreateButtonPress(navController)
         }
      }
   }, navController, "New Task",
      navigationIcon = { BackIconButton(onClick = { navController.popBackStack() }) }
   )
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

         Text(text = "START DATETIME")
         startDateTime?.let { it3 ->
            DateTimePicker(initialDateTime = it3) { it2 ->
               newTaskViewModel.onStartDateTimeChange(it2)
            }
         }

         Text(text = "END DATETIME")
         endDateTime?.let { it5 ->
            DateTimePicker(initialDateTime = it5) { it2 ->
               newTaskViewModel.onEndDateTimeChange(it2)

            }
         }
      }
   }
}