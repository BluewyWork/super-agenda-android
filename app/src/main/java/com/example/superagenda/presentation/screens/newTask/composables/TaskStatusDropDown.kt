package com.example.superagenda.presentation.screens.newTask.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.superagenda.domain.models.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskStatusDropDown(
   taskStatus: TaskStatus,
   onStatusChange: (TaskStatus) -> Unit,
) {
   val taskStatuses = TaskStatus.values()

   var expanded by remember(calculation = { mutableStateOf(false) })
   var selectedStatus by remember(calculation = { mutableStateOf(taskStatus) })

   Column(
      modifier = Modifier.fillMaxWidth(),
      content = {
         ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(),

            content = {
               OutlinedTextField(
                  value = selectedStatus.name,
                  onValueChange = {},
                  readOnly = true,
                  trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                  modifier = Modifier
                     .fillMaxWidth()
                     .menuAnchor()
               )

               ExposedDropdownMenu(
                  expanded = expanded,
                  onDismissRequest = { expanded = false },

                  content = {
                     taskStatuses.forEach { status ->
                        DropdownMenuItem(
                           text = { Text(text = status.name) },
                           onClick = {
                              selectedStatus = status
                              expanded = false
                              onStatusChange(status)
                           }
                        )
                     }
                  }
               )
            }
         )
      }
   )
}