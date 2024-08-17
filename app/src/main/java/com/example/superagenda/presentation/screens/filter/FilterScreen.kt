package com.example.superagenda.presentation.screens.filter

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
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.composables.Navigation
import com.example.superagenda.presentation.composables.NavigationViewModel
import com.example.superagenda.presentation.composables.TaskCard
import com.example.superagenda.presentation.screens.filter.composables.DateTimePicker
import com.example.superagenda.presentation.screens.filter.composables.FilterButton
import com.example.superagenda.presentation.screens.filter.composables.TaskStatusDropDown
import com.example.superagenda.presentation.screens.filter.composables.TitleTextField
import java.time.LocalDateTime

@Composable
fun FilterScreen(
   filterScreenViewModel: FilterScreenViewModel,
   navController: NavController,
   navigationViewModel: NavigationViewModel,
) {
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
   val filteredTaskList: List<Task>? by filterScreenViewModel.filteredTaskList.observeAsState()

   LazyColumn {
      item {
         TitleTextField(title) {
            filterScreenViewModel.onTitleChange(it)
         }

         TaskStatusDropDown(null) {
            if (it != null) {
               filterScreenViewModel.onTaskStatusChange(it)
            }
         }

         Spacer(modifier = Modifier.padding(8.dp))

         Text(text = "START DATETIME")

         DateTimePicker(initialDateTime = null) { it2 ->
            if (it2 != null) {
               filterScreenViewModel.onStartDateTimeChange(it2)
            }
         }

         Spacer(modifier = Modifier.padding(8.dp))
         Text(text = "END DATETIME")

         DateTimePicker(initialDateTime = null) { it2 ->
            if (it2 != null) {
               filterScreenViewModel.onEndDateTimeChange(it2)
            }
         }

         FilterButton {
            filterScreenViewModel.onFilterPress(navController)
         }

         if (filteredTaskList.isNullOrEmpty()) {
            Text(text = "No tasks found.")
         } else {
            filteredTaskList?.forEach {
               TaskCard(task = it) {
               }
            }
         }
      }
   }
}
