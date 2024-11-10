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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.composables.TaskCard
import com.example.superagenda.presentation.screens.filter.composables.TaskStatusDropDown
import java.time.LocalDateTime

@Composable
fun FilterScreen(
   filterScreenViewModel: FilterScreenViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
   val popupsQueue: List<Pair<String, String>> by filterScreenViewModel.popupsQueue.observeAsState(
      listOf()
   )

   if (popupsQueue.isNotEmpty()) {
      PopupDialog(
         title = popupsQueue.first().first,
         message = popupsQueue.first().second
      ) {
         filterScreenViewModel.dismissPopup()
      }
   }

   Navigation(
      content = { padding ->
         Column(modifier = Modifier.padding(padding)) {
            Filter(filterScreenViewModel, navController)
         }
      },
      navController = navController,
      topBarTitle = "Find Tasks",
      navigationViewModel = navigationViewModel
   )
}

@Composable
fun Filter(filterScreenViewModel: FilterScreenViewModel, navController: NavController) {
   val title: String by filterScreenViewModel.title.observeAsState("")
   val taskStatus: TaskStatus? by filterScreenViewModel.taskStatus.observeAsState()
   val startDateTime: LocalDateTime? by filterScreenViewModel.startDateTime.observeAsState()
   val endDateTime: LocalDateTime? by filterScreenViewModel.endDateTime.observeAsState()
   val filteredTaskList: List<Task> by filterScreenViewModel.filteredTaskList.observeAsState(listOf())

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
               filterScreenViewModel.onStartDateTimeChange(it)
            }
         }

         item {
            LocalDateTimePickerTextField(
               value = endDateTime,
               label = "End DateTime",
               modifier = Modifier.fillMaxWidth()
            ) {
               filterScreenViewModel.onEndDateTimeChange(it)
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
            .background(Color.Gray)
            .padding(8.dp)
      ) {
         LazyColumn {
            item {
               for (task in filteredTaskList!!) {
                  TaskCard(task = task) {
                     filterScreenViewModel.setTaskToEdit(task)
                     navController.navigate(Destinations.TaskEdit.route + "2")
                  }
               }
            }
         }
      }
   }
}
