package com.example.superagenda.presentation.screens.newTask

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.superagenda.presentation.Destinations
import com.example.superagenda.presentation.composables.ImageRow
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.newTask.composables.TaskStatusDropDown
import com.example.superagenda.util.decodeBase64ToImage

@Composable
fun NewTaskScreen(
   newTaskViewModel: NewTaskViewModel,
   navController: NavController,
) {
   val popupsQueue: List<Triple<String, String, String>> by newTaskViewModel.popupsQueue.collectAsStateWithLifecycle()

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

      var showBigImage by remember { mutableStateOf("") }

      ImageRow(images,
         onImageClick = { imageClicked ->
            val x = decodeBase64ToImage(imageClicked)

            if (x != null) {
               showBigImage = imageClicked
            }
         }
      ) { imageNew ->
         newTaskViewModel.setImages(images + imageNew)
      }

      if (showBigImage.isNotEmpty()) {
         Dialog(
            onDismissRequest = { showBigImage = "" }
         ) {

            Box(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(16.dp),
               contentAlignment = Alignment.Center
            ) {
               Column(
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  Image(
                     bitmap = decodeBase64ToImage(showBigImage)!!.asImageBitmap(),
                     contentDescription = null,
                     modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                  )
                  Button(onClick = { showBigImage = "" }) {
                     Text("Close")
                  }
               }
            }
         }
      }

      Button(
         onClick = {
            newTaskViewModel.onCreateButtonPress {
               navController.navigate(Destinations.Tasks.route)
            }
         },

         modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
      ) {
         Text("Create")
      }
   }
}
