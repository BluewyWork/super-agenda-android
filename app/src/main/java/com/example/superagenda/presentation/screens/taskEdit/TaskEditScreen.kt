package com.example.superagenda.presentation.screens.taskEdit

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
import com.example.superagenda.presentation.composables.BackIconButton
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.taskEdit.composables.TaskStatusDropDown

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

   Column {
      TaskEdit(taskEditViewModel, navController)
   }
}

@Composable
fun TaskEdit(taskEditViewModel: TaskEditViewModel, navController: NavController) {
   val title by taskEditViewModel.title.collectAsStateWithLifecycle()
   val description by taskEditViewModel.description.collectAsStateWithLifecycle()
   val taskStatus by taskEditViewModel.taskStatus.collectAsStateWithLifecycle()
   val startDateTime by taskEditViewModel.startDateTime.collectAsStateWithLifecycle()
   val endEstimatedDateTime by taskEditViewModel.endEstimatedDateTime.collectAsStateWithLifecycle()
   val images by taskEditViewModel.images.collectAsStateWithLifecycle()

   Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier.padding(8.dp)
   ) {
      OutlinedTextField(
         value = title,
         onValueChange = { it2 -> taskEditViewModel.setTitle(it2) },
         label = { Text("Title") },
         modifier = Modifier.fillMaxWidth()
      )

      OutlinedTextField(
         value = description,
         onValueChange = { it2 -> taskEditViewModel.setDescription(it2) },
         label = { Text("Description") },
         modifier = Modifier.fillMaxWidth()
      )

      TaskStatusDropDown(taskStatus) { it2 ->
         taskEditViewModel.setTaskStatus(it2)
      }

      LocalDateTimePickerTextField(
         label = "Start DateTime",
         value = startDateTime,

         onLocalDateTimeChange = { it2 ->
            taskEditViewModel.setStartDateTime(it2)
         },

         modifier = Modifier.fillMaxWidth()
      )

      LocalDateTimePickerTextField(
         label = "End DateTime",
         value = endEstimatedDateTime,

         onLocalDateTimeChange = { it2 ->
            taskEditViewModel.setEndDateTime(it2)
         },

         modifier = Modifier.fillMaxWidth()
      )

      Button(
         onClick = {
            taskEditViewModel.onDeleteButtonPress {
               navController.navigateUp()
            }
         },
         modifier = Modifier
            .fillMaxWidth()
      ) {
         Text("Delete")
      }

      Button(
         onClick = { taskEditViewModel.onUpdateButtonPress(navController) },
         modifier = Modifier
            .fillMaxWidth()
      ) {
         Text("Update")
      }
   }
}
