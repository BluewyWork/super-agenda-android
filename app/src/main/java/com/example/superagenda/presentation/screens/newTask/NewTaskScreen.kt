package com.example.superagenda.presentation.screens.newTask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.newTask.composables.TaskStatusDropDown
import java.time.LocalDateTime

@Composable
fun NewTaskScreen(
   newTaskViewModel: NewTaskViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
   val popupsQueue: List<Pair<String, String>> by newTaskViewModel.popupsQueue.observeAsState(listOf())

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second,

         onDismiss = {
            newTaskViewModel.dismissPopup()
         }
      )
   }

   Navigation(
      content = { padding ->
         Column(
            modifier = Modifier.padding(padding),

            content = {
               NewTask(newTaskViewModel, navController)

               Button(
                  onClick = { newTaskViewModel.onCreateButtonPress(navController) },
                  modifier = Modifier
                     .padding(vertical = 8.dp)
                     .fillMaxWidth(),

                  content = {
                     Text("Create")
                  }
               )
            }
         )
      },

      navController = navController,
      topBarTitle = "New Task",
      navigationIcon = { BackIconButton(onClick = { navController.navigateUp() }) },
      navigationViewModel = navigationViewModel
   )
}

@Composable
fun NewTask(newTaskViewModel: NewTaskViewModel, navController: NavController) {
   val title: String by newTaskViewModel.title.observeAsState(initial = "")
   val description: String by newTaskViewModel.description.observeAsState(initial = "")
   val taskStatus: TaskStatus by newTaskViewModel.taskStatus.observeAsState(initial = TaskStatus.NotStarted)
   val startDateTime: LocalDateTime by newTaskViewModel.startDateTime.observeAsState(initial = LocalDateTime.now())
   val endDateTime: LocalDateTime by newTaskViewModel.endDateTime.observeAsState(initial = LocalDateTime.now())

   Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),

      modifier = Modifier.fillMaxWidth(),

      content = {
         OutlinedTextField(
            value = title,
            onValueChange = { newTaskViewModel.onTitleChange(title) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
         )

         OutlinedTextField(
            value = description,
            onValueChange = { newTaskViewModel.onDescriptionChange(description) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
         )

         TaskStatusDropDown(taskStatus) {
            newTaskViewModel.onTaskStatusChange(taskStatus)
         }

         LocalDateTimePickerTextField(
            value = startDateTime,

            onLocalDateTimeChange = {
               newTaskViewModel.onStartDateTimeChange(it)
            },

            modifier = Modifier.fillMaxWidth(),

            label = "Start DateTime"
         )

         LocalDateTimePickerTextField(
            value = endDateTime,

            onLocalDateTimeChange = {
               newTaskViewModel.onEndDateTimeChange(it)
            },

            modifier = Modifier.fillMaxWidth(),

            label = "End DateTime"
         )
      }
   )
}
