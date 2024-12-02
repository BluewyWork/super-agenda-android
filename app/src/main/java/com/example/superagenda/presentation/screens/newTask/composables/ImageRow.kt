package com.example.superagenda.presentation.screens.newTask.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.superagenda.presentation.composables.ImagePicker
import com.example.superagenda.presentation.screens.newTask.NewTaskViewModel
import com.example.superagenda.util.decodeBase64ToImage
import com.example.superagenda.util.encodeImageToBase64

@Composable
fun ImageRow(newTaskViewModel: NewTaskViewModel, images: List<String>) {
   LazyRow(
      modifier = Modifier
         .fillMaxHeight()
         .padding(8.dp)
   ) {
      item {
         ImagePicker(
            onImagePicked = {
               it?.let { it2 ->
                  val imageNew = encodeImageToBase64(it2)
                  val imagesNew = images + imageNew
                  newTaskViewModel.setImages(imagesNew)
               }
            }
         ) { onClick ->
            Button(
               onClick = onClick,

               modifier = Modifier
                  .width(100.dp)
                  .height(100.dp)
            ) {
               Icon(Icons.Default.Add, "Add Image")
            }
         }
      }

      items(images.size) { index ->
         val image = images[index]
         val imageCurated = decodeBase64ToImage(image)

         if (imageCurated == null) {
            Text("Invalid image...", modifier = Modifier.padding(8.dp))
         } else {
            Image(
               bitmap = imageCurated.asImageBitmap(),
               contentDescription = null,

               modifier = Modifier
                  .padding(8.dp)
                  .clickable { }
                  .width(100.dp)
                  .height(100.dp),

               contentScale = ContentScale.Crop
            )
         }
      }
   }
}