package com.example.superagenda.presentation.screens.taskEdit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.superagenda.presentation.screens.taskEdit.composables.TaskStatusDropDown
import java.time.LocalDateTime

@Composable
fun TaskEditScreen(
   taskEditViewModel: TaskEditViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
   val popupsQueue: List<Triple<String, String, String>> by taskEditViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second,
         error = popupsQueue.first().third
      ) {
         taskEditViewModel.dismissPopup()
      }
   }

   Navigation(
      navController = navController,
      topBarTitle = "Edit Task",
      navigationIcon = { BackIconButton(onClick = { navController.navigateUp() }) },
      navigationViewModel = navigationViewModel
   ) { padding ->
      Column(modifier = Modifier.padding(padding)) {
         TaskEdit(taskEditViewModel, navController)
      }
   }
}

@Composable
fun TaskEdit(taskEditViewModel: TaskEditViewModel, navController: NavController) {
   val title: String? by taskEditViewModel.title.observeAsState()
   val description: String? by taskEditViewModel.description.observeAsState()
   val taskStatus: TaskStatus? by taskEditViewModel.taskStatus.observeAsState()
   val startDateTime: LocalDateTime? by taskEditViewModel.startDateTime.observeAsState()
   val endDateTime: LocalDateTime? by taskEditViewModel.endDateTime.observeAsState()

   LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(8.dp)
   ) {
      item {
         title?.let {
            OutlinedTextField(
               value = it,
               onValueChange = { it2 -> taskEditViewModel.onTitleChange(it2) },
               label = { Text("Title") },
               modifier = Modifier.fillMaxWidth()
            )
         }

      }

      item {
         description?.let {
            OutlinedTextField(
               value = it,
               onValueChange = { it2 -> taskEditViewModel.onDescriptionChange(it2) },
               label = { Text("Description") },
               modifier = Modifier.fillMaxWidth()
            )
         }
      }

      item {
         taskStatus?.let {
            TaskStatusDropDown(it) { it2 ->
               taskEditViewModel.onTaskStatusChange(it2)
            }
         }
      }

      item {
         startDateTime?.let {
            LocalDateTimePickerTextField(
               label = "Start DateTime",
               value = it,

               onLocalDateTimeChange = { it2 ->
                  taskEditViewModel.onStartDateTimeChange(it2)
               },

               modifier = Modifier.fillMaxWidth()
            )
         }
      }

      item {
         endDateTime?.let {
            LocalDateTimePickerTextField(
               label = "End DateTime",
               value = it,

               onLocalDateTimeChange = { it2 ->
                  taskEditViewModel.onEndDateTimeChange(it2)
               },

               modifier = Modifier.fillMaxWidth()
            )
         }
      }

      item {
         Button(
            onClick = { taskEditViewModel.onDeleteButtonPress(navController) },
            modifier = Modifier
               .fillMaxWidth()
         ) {
            Text("Delete")
         }
      }

      item {
         Button(
            onClick = { taskEditViewModel.onUpdateButtonPress(navController) },
            modifier = Modifier
               .fillMaxWidth()
         ) {
            Text("Update")
         }
      }
   }
}
