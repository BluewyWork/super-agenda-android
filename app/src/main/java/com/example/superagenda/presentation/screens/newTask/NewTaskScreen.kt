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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.newTask.composables.ImageRow
import com.example.superagenda.presentation.screens.newTask.composables.TaskStatusDropDown

@Composable
fun NewTaskScreen(
   newTaskViewModel: NewTaskViewModel,
   navController: NavController,
) {
   val popupsQueue: List<Triple<String, String, String>> by newTaskViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second,
         error = popupsQueue.first().third,

         onDismiss = {
            newTaskViewModel.dismissPopup()
         }
      )
   }

   NewTask(newTaskViewModel, navController)
}

@Composable
fun NewTask(newTaskViewModel: NewTaskViewModel, navController: NavController) {
   val title by newTaskViewModel.title.collectAsStateWithLifecycle()
   val description by newTaskViewModel.description.collectAsStateWithLifecycle()
   val taskStatus by newTaskViewModel.taskStatus.collectAsStateWithLifecycle()
   val startDateTime by newTaskViewModel.startDateTime.collectAsStateWithLifecycle()
   val endEstimatedDateTime by newTaskViewModel.endEstimatedDateTime.collectAsStateWithLifecycle()
   val images by newTaskViewModel.images.collectAsStateWithLifecycle()

   Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(8.dp),
   ) {
      OutlinedTextField(
         value = title,
         onValueChange = { newTaskViewModel.setTitle(it) },
         label = { Text("Title") },
         modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
         value = description,
         onValueChange = { newTaskViewModel.setDescription(it) },
         label = { Text("Description") },
         modifier = Modifier.fillMaxWidth()
      )

      TaskStatusDropDown(taskStatus) {
         newTaskViewModel.setTaskStatus(it)
      }

      LocalDateTimePickerTextField(
         value = startDateTime,

         onLocalDateTimeChange = {
            newTaskViewModel.setStartDateTime(it)
         },

         modifier = Modifier.fillMaxWidth(),
         label = "Start DateTime"
      )

      LocalDateTimePickerTextField(
         value = endEstimatedDateTime,

         onLocalDateTimeChange = {
            newTaskViewModel.setEndEstimatedDateTime(it)
         },

         modifier = Modifier.fillMaxWidth(),
         label = "End DateTime"
      )

      ImageRow(newTaskViewModel, images)

      Button(
         onClick = { newTaskViewModel.onCreateButtonPress(navController) },

         modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
      ) {
         Text("Create")
      }
   }
}
