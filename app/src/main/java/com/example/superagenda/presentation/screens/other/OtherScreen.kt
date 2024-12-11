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
import com.example.superagenda.presentation.screens.other.composables.CardTask
import com.example.superagenda.presentation.screens.other.composables.ImportTaskList
import com.example.superagenda.ui.theme.oneDarkCustomYellow

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

      Text("WARNING!", color = oneDarkCustomYellow)
      Text("Some tasks needs manual intervention to solve..", color = oneDarkCustomYellow)
   }
}

@Composable
fun TasksSolving(otherViewModel: OtherViewModel) {
   val tasksPairToResolve by otherViewModel.tasksToResolve.collectAsStateWithLifecycle()

   if (tasksPairToResolve.isEmpty()) {
      return
   }

   val pair = tasksPairToResolve.first()

   Column {
      Text("In Storage:")
      CardTask(pair.first)
      Text("New:")
      CardTask(pair.second)

      Row {
         Button(onClick = {
            otherViewModel.resolveTask(pair.second, TaskResolutionOptions.KEEP_ORIGINAL)
         }) {
            Text("Keep Original")
         }

         Spacer(Modifier.width(8.dp))

         Button(onClick = {
            otherViewModel.resolveTask(pair.second, TaskResolutionOptions.KEEP_NEW)
         }) {
            Text("Override Original")
         }

         Spacer(Modifier.width(8.dp))

         Button(onClick = {
            otherViewModel.resolveTask(pair.second, TaskResolutionOptions.KEEP_BOTH)
         }) {
            Text("Keep Both")
         }
      }
   }
}