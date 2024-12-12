package com.example.superagenda.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.screens.history.composable.CardTask
import java.time.LocalDate

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel, navController: NavController) {
   val popups by historyViewModel.popups.collectAsStateWithLifecycle()

   if (popups.isNotEmpty()) {
      val popup = popups.first()

      AlertDialog(onDismissRequest = {
         popup.code()
         historyViewModel.onPopupDismissed()
      },

         title = { Text(popup.title) },

         text = {
            Column {
               if (popup.error.isNotBlank()) {
                  Text(popup.error)
               }

               Text(popup.description)
            }
         },

         confirmButton = {
            Button(onClick = {
               popup.code()
               historyViewModel.onPopupDismissed()
            }) {
               Text("OK")
            }
         })
   }

   History(historyViewModel, navController)
}

@Composable
fun History(historyViewModel: HistoryViewModel, navController: NavController) {
   val tasks by historyViewModel.tasks.collectAsStateWithLifecycle()

   val groupedTasks = groupTasksByDate(tasks)

   LazyColumn {
      groupedTasks.forEach { (date, eventMap) ->
         item {
            Divider()

            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
               Text(
                  text = date.toString(),
                  style = MaterialTheme.typography.headlineSmall,
                  modifier = Modifier.padding(8.dp)
               )
            }

            eventMap["started"]?.let { tasks ->
               Row {
                  Icon(Icons.Default.PlayArrow, null)
                  Text(text = "Tasks Started", modifier = Modifier.padding(start = 16.dp))
               }

               tasks.forEach { task ->
                 Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                           start = 10.dp, end = 10.dp
                        )
                  ) {
                     CardTask(task) {
                        historyViewModel.onEditPressed(task)
                        navController.navigate(Destinations.TaskEdit.route + "3")
                     }
                  }
               }
            }

            eventMap["estimated_to_end"]?.let { tasks ->
               Row {
                  Icon(Icons.Default.Lock, null)

                  Text(
                     text = "Tasks Finalizes (Estimate)",
                     modifier = Modifier.padding(start = 16.dp)
                  )
               }

               tasks.forEach { task ->
                  Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                           start = 10.dp, end = 10.dp
                        )
                  ) {
                     CardTask(task) {
                        historyViewModel.onEditPressed(task)
                        navController.navigate(Destinations.TaskEdit.route + "3")
                     }
                  }
               }
            }

            eventMap["ended"]?.let { tasks ->
               Row {
                  Icon(Icons.Default.CheckCircle, null)
                  Text(text = "Tasks Finalized (Real)", modifier = Modifier.padding(start = 16.dp))
               }

               tasks.forEach { task ->
                  Box(
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                           start = 10.dp, end = 10.dp
                        )
                  ) {
                     CardTask(task) {
                        historyViewModel.onEditPressed(task)
                        navController.navigate(Destinations.TaskEdit.route + "3")
                     }
                  }
               }
            }
         }
      }
   }
}

fun groupTasksByDate(tasks: List<Task>): Map<LocalDate, Map<String, List<Task>>> {
   val taskMap = mutableMapOf<LocalDate, MutableMap<String, MutableList<Task>>>()

   tasks.forEach { task ->
      val startDate = task.startDateTime.toLocalDate()
      val estimatedEndDate = task.endEstimatedDateTime.toLocalDate()
      val endDate = task.endDateTime?.toLocalDate()

      if (!taskMap.containsKey(startDate)) taskMap[startDate] = mutableMapOf()
      taskMap[startDate]?.getOrPut("started") { mutableListOf() }?.add(task)

      if (!taskMap.containsKey(estimatedEndDate)) taskMap[estimatedEndDate] = mutableMapOf()
      taskMap[estimatedEndDate]?.getOrPut("estimated_to_end") { mutableListOf() }?.add(task)

      endDate?.let {
         if (!taskMap.containsKey(it)) taskMap[it] = mutableMapOf()
         taskMap[it]?.getOrPut("ended") { mutableListOf() }?.add(task)
      }
   }

   return taskMap
}