package com.example.superagenda.presentation.screens.other.composables

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImportTaskList(
   modifier: Modifier = Modifier,
   onFileChosen: (ContentResolver, String) -> Unit,
) {
   var chosenFile by remember { mutableStateOf<String?>(null) }
   val context = LocalContext.current

   val fileChooserLauncher = rememberLauncherForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult()
   ) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
         val data: Intent? = result.data
         val filePath = data?.dataString
         filePath?.let { path ->
            chosenFile = path
            onFileChosen(context.contentResolver, path)
         }
      }
   }

   Column {
      Button(
         onClick = {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
               type = "application/json"
            }
            fileChooserLauncher.launch(intent)
         },
         modifier = modifier
      ) {
         Text("Import Backup File")
      }
   }
}