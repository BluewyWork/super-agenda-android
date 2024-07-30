package com.example.superagenda.presentation.screens.tasks.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyState(message: String, iconId: Int) {
   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Image(
         painter = painterResource(id = iconId),
         contentDescription = null,
         modifier = Modifier.size(100.dp)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
         text = message,
         fontSize = 20.sp,
         fontWeight = FontWeight.Bold,
         color = Color.Gray
      )
   }
}
