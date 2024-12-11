package com.example.superagenda.presentation.screens.other.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import java.time.format.DateTimeFormatter

@Composable
fun CardTask(task: Task) {
   val color: CardColors = when (task.status) {
      TaskStatus.NotStarted -> CardDefaults.cardColors(containerColor = Color(30, 0, 0))
      TaskStatus.Ongoing -> CardDefaults.cardColors(containerColor = Color(30, 30, 0))
      TaskStatus.Completed -> CardDefaults.cardColors(containerColor = Color(0, 30, 0))
   }

   Card(
      colors = color,
      modifier = Modifier
         .fillMaxWidth()
         .padding(8.dp),
      shape = RoundedCornerShape(8.dp)
   ) {
      Box(
         Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
      ) {
         Column(
            modifier = Modifier
               .padding(8.dp)
               .fillMaxWidth(),

            verticalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            Text(
               text = task.title, maxLines = 1, overflow = TextOverflow.Ellipsis, softWrap = false,

               style = MaterialTheme.typography.headlineMedium.copy(
                  fontSize = 24.sp, fontWeight = FontWeight.Bold
               )
            )

            Text(
               text = task.description, maxLines = 5, overflow = TextOverflow.Ellipsis,

               style = MaterialTheme.typography.bodyMedium.copy(
                  fontSize = 16.sp
               )
            )

            if (task.images.isNotEmpty()) {

               val text: String = if (task.images.size == 1) {
                  "file"
               } else {
                  "files"
               }

               Text(
                  text = "${task.images.size} $text attached", fontSize = 12.sp
               )
            }
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            Text("Start: ${task.startDateTime.format(formatter)}")
            Text("End: ${task.endEstimatedDateTime.format(formatter)}")
         }
      }
   }
}