package com.example.superagenda.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.superagenda.util.decodeBase64ToImage

@Composable
fun Image64Grid(
   images: List<String>,
   imageWidth: Dp,
   imageHeight: Dp,
   modifier: Modifier = Modifier,
   imageModifier: Modifier = Modifier,
   spacing: Dp = 8.dp,
   onImageClick: (String) -> Unit,
) {
   LazyVerticalGrid(
      columns = GridCells.Adaptive(imageWidth),
      modifier = modifier
         .fillMaxSize()
         .padding(spacing)
   ) {
      items(images.size) { index ->
         val image = images[index]
         val imageCurated = decodeBase64ToImage(image)

         if (imageCurated == null) {
            Text("Invalid image...", modifier = Modifier.padding(spacing))
         } else {
            Image(
               bitmap = imageCurated.asImageBitmap(),
               contentDescription = null,
               modifier = imageModifier
                  .padding(spacing)
                  .clickable { onImageClick(image) }
                  .width(imageWidth)
                  .height(imageHeight),
               contentScale = ContentScale.Crop
            )
         }
      }
   }
}