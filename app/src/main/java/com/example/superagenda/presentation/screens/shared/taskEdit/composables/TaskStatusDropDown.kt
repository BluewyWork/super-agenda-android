package com.example.superagenda.presentation.screens.shared.taskEdit.composables

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.superagenda.domain.models.TaskStatus

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TaskStatusDropDown(taskStatus: TaskStatus) {
//    val context = LocalContext.current
//    val taskStatuses = arrayOf("Not Started", "On Going", "Completed")
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(taskStatuses[0]) }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(32.dp)
//    ) {
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = {
//                expanded = !expanded
//            }
//        ) {
//            TextField(
//                value = selectedText,
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                modifier = Modifier.menuAnchor()
//            )
//
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                taskStatuses.forEach { item ->
//                    DropdownMenuItem(
//                        text = { Text(text = item) },
//                        onClick = {
//                            selectedText = item
//                            expanded = false
//                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
//                        }
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskStatusDropDown(taskStatus: TaskStatus) {
    val context = LocalContext.current
    val taskStatuses = TaskStatus.values() // Get all values of TaskStatus enum
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(taskStatus) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedStatus.name, // Display the name of selected status
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                taskStatuses.forEach { status ->
                    DropdownMenuItem(
                                                text = { Text(text = status.name) },

                        onClick = {
                            selectedStatus = status
                            expanded = false
                            Toast.makeText(context, status.name, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}