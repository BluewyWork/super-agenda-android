package com.example.superagenda.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {
   Box {
      Card(
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
         shape = RoundedCornerShape(8.dp)
      ) {
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
