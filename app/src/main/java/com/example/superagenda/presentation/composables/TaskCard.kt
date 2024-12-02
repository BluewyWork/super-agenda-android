package com.example.superagenda.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import java.time.Duration
import java.time.LocalDateTime

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
   Box {

      Card(
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
         shape = RoundedCornerShape(8.dp)
      ) {
         Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
         ) {
//            task.image?.let { Image64(it, Modifier.size(200.dp)) }

            Column(
               modifier = Modifier
                  .padding(8.dp)
                  .fillMaxSize(),

               verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               Text(
                  text = task.title,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  softWrap = false,

                  style = MaterialTheme.typography.headlineMedium.copy(
                     fontSize = 24.sp,
                     fontWeight = FontWeight.Bold
                  )
               )

               Text(
                  text = task.description,
                  maxLines = 5,
                  overflow = TextOverflow.Ellipsis,

                  style = MaterialTheme.typography.bodyMedium.copy(
                     fontSize = 16.sp
                  )
               )

               var s: String = ""

               when (task.status) {
                  TaskStatus.NotStarted -> {
                     val difference = Duration.between(LocalDateTime.now(), task.startDateTime)
                     val days = difference.toDays()
                     s = if (days > 0) {
                        "starts in $days day/s"
                     } else {
                        val hours = difference.toHours()
                        val minutes = difference.toMinutes() % 60
                        when {
                           hours > 0 -> "starts in $hours hour/s"
                           minutes > 0 -> "starts in $minutes minute/s"
                           else -> "start time has passed"
                        }
                     }
                  }

                  TaskStatus.Ongoing -> {
                     val difference = Duration.between(LocalDateTime.now(), task.endDateTime)
                     val days = difference.toDays()
                     s = if (days > 0) {
                        "expires in $days day/s"
                     } else {
                        val hours = difference.toHours()
                        val minutes = difference.toMinutes() % 60
                        when {
                           hours > 0 -> "expires in $hours hour/s"
                           minutes > 0 -> "expires in $minutes minute/s"
                           else -> "expired"
                        }
                     }
                  }

                  TaskStatus.Completed -> {
                     val difference = Duration.between(task.endDateTime, LocalDateTime.now())
                     val days = difference.toDays()
                     s = if (days > 0) {
                        "was completed $days day/s ago"
                     } else {
                        val hours = difference.toHours()
                        val minutes = difference.toMinutes() % 60
                        when {
                           hours > 0 -> "was completed $hours hour/s ago"
                           minutes > 0 -> "was completed $minutes minute/s ago"
                           else -> "was completed just now"
                        }
                     }
                  }
               }

               Text(
                  text = s,
                  fontSize = 12.sp
               )
            }
         }
      }

      FloatingActionButton(
         onClick = onClick,
         modifier = Modifier
            .align(Alignment.BottomEnd)
            .offset(x = (-16).dp, y = (-16).dp),
      ) {
         Box(
            modifier = Modifier.padding(8.dp),
         ) {
            Icon(
               imageVector = Icons.Default.Create,
               contentDescription = null
            )
         }
      }
   }
}
