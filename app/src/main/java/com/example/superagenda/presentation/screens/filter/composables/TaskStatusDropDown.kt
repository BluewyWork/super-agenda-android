package com.example.superagenda.presentation.screens.filter.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.superagenda.domain.models.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskStatusDropDown(
   taskStatus: TaskStatus? = null,
   onStatusChange: (TaskStatus?) -> Unit
) {
   val context = LocalContext.current
   val taskStatuses = TaskStatus.values()

   var expanded by remember { mutableStateOf(false) }
   var selectedStatus by remember { mutableStateOf(taskStatus) }

   Column(modifier = Modifier.fillMaxWidth()) {
      ExposedDropdownMenuBox(
         expanded = expanded,
         onExpandedChange = { expanded = it },
         modifier = Modifier.fillMaxWidth()
      ) {
         TextField(
            value = selectedStatus?.name ?: "None",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
               .fillMaxWidth()
               .menuAnchor()
         )

         ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
         ) {
            DropdownMenuItem(
               text = { Text(text = "None") },
               onClick = {
                  selectedStatus = null
                  expanded = false
                  onStatusChange(null)
               }
            )
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
      }
   }
}