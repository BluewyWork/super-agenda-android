package com.example.superagenda.presentation.screens.tasksNotStarted.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.superagenda.domain.models.Task

@Composable
fun TaskCard(task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
    ) {
        Text(task.title)
        Text(task.description)
        Button(onClick = { /*TODO*/ }) {
            Icon(Icons.Default.Create, null)
        }
    }
}