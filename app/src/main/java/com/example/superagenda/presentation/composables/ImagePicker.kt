package com.example.superagenda.presentation.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImagePicker(onImagePicked: (Bitmap?) -> Unit) {
   val context = LocalContext.current
   val scope = rememberCoroutineScope()
   val imagePickerLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.GetContent()
   ) { uri: Uri? ->
      scope.launch {
         val bitmap = uri?.let {
            loadBitmapFromUri(context, it)
         }
         onImagePicked(bitmap)
      }
   }

   Button(onClick = {
      imagePickerLauncher.launch("image/*")
   }) {
      Text("Pick Image")
   }
}

private suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
   return withContext(Dispatchers.IO) {
      try {
         if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
         } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
         }
      } catch (e: Exception) {
         e.printStackTrace()
         null
      }
   }
}