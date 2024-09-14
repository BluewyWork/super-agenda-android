package com.example.superagenda.presentation.screens.tasks.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LineWithText(
   text: String,
   lineColor: Color = Color.Gray,
   lineThickness: Dp = 1.dp,
   modifier: Modifier = Modifier,
) {
   Box(
      contentAlignment = Alignment.Center,
      modifier = modifier.fillMaxWidth()
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier.fillMaxWidth()
      ) {
         Canvas(modifier = Modifier
            .weight(1f)
            .height(lineThickness)) {
            drawLine(
               color = lineColor,
               start = Offset(0f, center.y),
               end = Offset(size.width, center.y),
               strokeWidth = lineThickness.toPx()
            )
         }
         Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
         )
         Canvas(modifier = Modifier
            .weight(1f)
            .height(lineThickness)) {
            drawLine(
               color = lineColor,
               start = Offset(0f, center.y),
               end = Offset(size.width, center.y),
               strokeWidth = lineThickness.toPx()
            )
         }
      }
   }
}
