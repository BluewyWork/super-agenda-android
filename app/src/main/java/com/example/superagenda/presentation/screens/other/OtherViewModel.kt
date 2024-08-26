package com.example.superagenda.presentation.screens.other

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

   fun enqueuePopup(title: String, message: String) {
      _popupsQueue.value =
         popupsQueue.value?.plus(Pair(title, message)) ?: listOf(
            Pair(
               title,
               message
            )
         )
   }

   fun dismissPopup() {
      _popupsQueue.postValue(_popupsQueue.value?.drop(1))
   }

   fun waitForPopup(code: () -> Unit) {
      popupsQueue.observeForever { queue ->
         if (queue.isNullOrEmpty()) {
            code()
            popupsQueue.removeObserver { this }
         }
      }
   }

   fun onBackUpButtonPress() {
      viewModelScope.launch {
         val tasks = taskUseCase.retrieveTasksFromLocalDatabase()

         if (tasks == null) {
            enqueuePopup("ERROR", "Failed, no tasks to backup...")
            return@launch
         }

         if (taskUseCase.backupTasks(tasks)) {
            enqueuePopup("INFO", "Successfully backed up tasks!\nYou can find it under Download")
         } else {
            enqueuePopup("ERROR", "Failed to backup tasks...")
         }
      }
   }

   fun onImportButtonPress(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {
         Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
         val taskList = deserializeTasksFromJson(contentResolver, filePath)

         var codeSuccess = true

         for (task in taskList) {
            codeSuccess = taskUseCase.insertOrUpdateTaskAtLocalDatabase(task)
         }

         if (codeSuccess) {
            enqueuePopup("INFO", "Successfully imported tasks!")
         } else {
            enqueuePopup("ERROR", "Failure on importing some tasks...")
         }
      }
   }

   private fun printFileContents(contentResolver: ContentResolver, filePath: String) {
      val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
      val reader = BufferedReader(InputStreamReader(inputStream))
      val stringBuilder = StringBuilder()
      var line: String? = reader.readLine()
      while (line != null) {
         stringBuilder.append(line).append('\n')
         line = reader.readLine()
      }
      inputStream?.close()
      reader.close()
      val fileContent = stringBuilder.toString()
      Log.d("LOOK AT ME", "File Content:\n$fileContent")
   }


   private fun deserializeTasksFromJson(
      contentResolver: ContentResolver,
      filePath: String,
   ): List<Task> {
      val gson = Gson()
      val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
      val reader = BufferedReader(InputStreamReader(inputStream))

      val tasks: List<TaskModel> = gson.fromJson(reader, Array<TaskModel>::class.java).toList()
      reader.close()

      return tasks.map { it.toDomain() }
   }
}