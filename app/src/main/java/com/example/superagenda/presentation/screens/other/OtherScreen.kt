package com.example.superagenda.presentation.screens.other

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.superagenda.presentation.screens.other.composables.ImportTaskList

@Composable
fun OtherScreen(
   otherViewModel: OtherViewModel,
) {
   val popups by otherViewModel.popups.collectAsStateWithLifecycle()

   if (popups.isNotEmpty()) {
      val popup = popups.first()

      AlertDialog(onDismissRequest = {
         popup.code()
         otherViewModel.onPopupDismissed()
      }, title = { Text(popup.title) },

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
               otherViewModel.onPopupDismissed()
            }) {
               Text("OK")
            }
         })
   }

   Column {
      Other(otherViewModel)
      TasksSolving(otherViewModel)
   }
}

@Composable
fun Other(otherViewModel: OtherViewModel) {
   val tasksToResolve by otherViewModel.tasksToResolve.collectAsStateWithLifecycle()

   Column(
      modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      Row {
         Button(
            onClick = { otherViewModel.onBackUpButtonPressed() },
            modifier = Modifier
               .fillMaxWidth()
               .weight(0.5f)
         ) {
            Row(
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
               Text("Backup Tasks")
            }
         }

         Spacer(Modifier.width(8.dp))

         ImportTaskList(onFileChosen = { contentResolver, filePath ->
            otherViewModel.onImportPressed(contentResolver, filePath)
         }) { onClick ->
            Button(
               onClick = onClick, modifier = Modifier
                  .fillMaxWidth()
                  .weight(0.5f)
            ) {
               Text("Import Tasks")
            }
         }
      }

      if (tasksToResolve.isEmpty()) {
         return@Column
      }

      Text("WARNING!")
      Text("Some tasks needs manual intervention to solve..")
      Text("Click here to solve")

   }
}

@Composable
fun TasksSolving(otherViewModel: OtherViewModel) {
   val tasksToResolve by otherViewModel.tasksToResolve.collectAsStateWithLifecycle()

   Column {
      if (tasksToResolve.isEmpty()) {
         return@Column
      }

      val task = tasksToResolve.first()

      tasksToResolve.first()

      Row {
         Button(onClick = {
            otherViewModel.resolveTask(task, TaskResolutionOptions.KEEP_ORIGINAL)
         }) {
            Text("Keep Original")
         }

         Button(onClick = {
            otherViewModel.resolveTask(task, TaskResolutionOptions.KEEP_NEW)
         }) {
            Text("Override Original")
         }

         Button(onClick = {
            otherViewModel.resolveTask(task, TaskResolutionOptions.KEEP_BOTH)
         }) {
            Text("Keep Both")
         }
      }
   }
}