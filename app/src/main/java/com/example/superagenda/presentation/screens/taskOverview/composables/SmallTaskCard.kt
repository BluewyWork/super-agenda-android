package com.example.superagenda.presentation.screens.taskOverview.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.superagenda.domain.models.Task

@Composable
fun SmallTaskCard(task: Task, onClick: () -> Unit)
{
   Card(
      modifier = Modifier
         .fillMaxWidth()
         .height(150.dp)
         .padding(8.dp),
      shape = RoundedCornerShape(8.dp)
   ) {
      Column(
         modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
      ) {
         Text(
            text = task.title,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.Black
         )
         Text(
            text = task.description,
            modifier = Modifier.padding(bottom = 16.dp)
         )
         Button(
            onClick = onClick,
            modifier = Modifier.align(Alignment.End)
         ) {
            Icon(
               imageVector = Icons.Default.Create,
               contentDescription = null
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Edit")
         }
      }
   }
}
