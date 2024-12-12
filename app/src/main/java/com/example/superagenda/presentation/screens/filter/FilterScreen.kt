package com.example.superagenda.presentation.screens.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.filter.composables.CardTask
import com.example.superagenda.presentation.screens.filter.composables.TaskStatusDropDown
import com.example.superagenda.ui.theme.oneDarkProSurface

@Composable
fun FilterScreen(
   filterScreenViewModel: FilterScreenViewModel,
   navController: NavController,
) {
   val popupsQueue: List<Triple<String, String, String>> by filterScreenViewModel.popupsQueue.collectAsStateWithLifecycle()

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second,
         error = popupsQueue.first().third
      ) {
         filterScreenViewModel.dismissPopup()
      }
   }

   Column {
      Filter(filterScreenViewModel, navController)
   }
}

@Composable
fun Filter(filterScreenViewModel: FilterScreenViewModel, navController: NavController) {
   val title by filterScreenViewModel.title.collectAsStateWithLifecycle()
   val taskStatus by filterScreenViewModel.taskStatus.collectAsStateWithLifecycle()
   val startDateTime by filterScreenViewModel.startDateTime.collectAsStateWithLifecycle()
   val endDateTime by filterScreenViewModel.endEstimatedDateTime.collectAsStateWithLifecycle()
   val filteredTaskList by filterScreenViewModel.filteredTaskList.collectAsStateWithLifecycle()

   Column {
      LazyColumn(
         modifier = Modifier.padding(8.dp),
         verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         item {
            OutlinedTextField(
               value = title,
               onValueChange = { filterScreenViewModel.onTitleChange(it) },
               label = { Text("Title") },
               modifier = Modifier.fillMaxWidth()
            )
         }

         item {
            TaskStatusDropDown(taskStatus = taskStatus) {
               filterScreenViewModel.onTaskStatusChange(it)
            }
         }

         item {
            LocalDateTimePickerTextField(
               value = startDateTime,
               label = "Start DateTime",
               modifier = Modifier.fillMaxWidth()
            ) {
               filterScreenViewModel.setStartDateTime(it)
            }
         }

         item {
            LocalDateTimePickerTextField(
               value = endDateTime,
               label = "End DateTime",
               modifier = Modifier.fillMaxWidth()
            ) {
               filterScreenViewModel.setEndEstimatedTime(it)
            }
         }

         item {
            Button(
               onClick = { filterScreenViewModel.onFilterPress() },
               modifier = Modifier
                  .align(Alignment.CenterHorizontally)
                  .fillMaxWidth()
            ) {
               Text("Filter")
            }
         }
      }

      Box(
         modifier = Modifier
            .fillMaxSize()
            .clip(
               RoundedCornerShape(
                  topStart = 38.dp,
                  topEnd = 38.dp,
                  bottomEnd = 0.dp,
                  bottomStart = 0.dp
               )
            )
            .background(color = oneDarkProSurface)
            .padding(8.dp)
      ) {
         LazyColumn {
            item {
               for (task in filteredTaskList) {
                  CardTask(task) {
                     filterScreenViewModel.setTaskToEdit(task)
                     navController.navigate(Destinations.TaskEdit.route + "2")
                  }
               }
            }
         }
      }
   }
}
