package com.example.superagenda.presentation.screens.taskEdit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.example.superagenda.presentation.composables.ImageRow
import com.example.superagenda.presentation.composables.LocalDateTimePickerTextField
import com.example.superagenda.presentation.composables.PopupDialog
import com.example.superagenda.presentation.screens.navigation.WrapperNavigationViewModel
import com.example.superagenda.presentation.screens.taskEdit.composables.TaskStatusDropDown
import com.example.superagenda.util.decodeBase64ToImage

@Composable
fun TaskEditScreen(
   taskEditViewModel: TaskEditViewModel,
   navController: NavController,
   wrapperNavigationViewModel: WrapperNavigationViewModel,
) {
   val popupsQueue: List<Triple<String, String, String>> by taskEditViewModel.popupsQueue.collectAsStateWithLifecycle()

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
            taskEditViewModel.setEndEstimatedDateTime(it2)
         },

         modifier = Modifier.fillMaxWidth()
      )

      var showBigImage by remember { mutableStateOf("") }

      ImageRow(
         images,
         onImageClick = { imageClicked ->
            val bitmap = decodeBase64ToImage(imageClicked)

            if (bitmap != null) {
               showBigImage = imageClicked
            }
         }
      ) { imageNew ->
         taskEditViewModel.setImages(images + imageNew)
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

      Row(
         horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         Button(
            onClick = {
               taskEditViewModel.onDeleteButtonPress {
                  navController.navigateUp()
               }
            },
            modifier = Modifier
               .fillMaxWidth()
               .weight(.5f)
         ) {
            Text("Delete")
         }

         Button(
            onClick = { taskEditViewModel.onUpdateButtonPress(navController) },
            modifier = Modifier
               .fillMaxWidth()
               .weight(.5f)
         ) {
            Text("Update")
         }
      }
   }
}
