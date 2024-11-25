package com.example.superagenda.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.superagenda.util.decodeBase64ToImage

@Composable
fun Image64(base64Str: String) {
   val bitmap = decodeBase64ToImage(base64Str)

   if (bitmap != null) {
      Image(
         bitmap = bitmap.asImageBitmap(),
         contentDescription = null,
         contentScale = ContentScale.Fit,
         modifier = Modifier.size(200.dp)
      )
   } else {
      Text("Image cannot be loaded")
   }
}