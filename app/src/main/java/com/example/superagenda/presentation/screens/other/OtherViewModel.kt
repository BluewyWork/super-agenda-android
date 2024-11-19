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
import com.example.superagenda.util.Result
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
   private val _showLoadingPopup = MutableLiveData<Boolean>()
   val showLoadingPopup: LiveData<Boolean> = _showLoadingPopup

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

   fun showLoadingPopup() {
      _showLoadingPopup.postValue(true)
   }

   fun dismissLoadingPopup() {
      _showLoadingPopup.postValue(false)
   }

   fun enqueuePopup(title: String, message: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value?.plus(Triple(title, message, error)) ?: listOf(
            Triple(
               title,
               message,
               error
            )
         )
   }

   fun dismissPopup() {
      _popupsQueue.postValue(_popupsQueue.value?.drop(1))
   }

   fun onBackUpButtonPress() {
      viewModelScope.launch {
         showLoadingPopup()
         when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               dismissLoadingPopup()
               enqueuePopup("ERROR", "Failed, no tasks to backup...")
            }

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data

               // TODO: Update this function to use AppResult
               if (taskUseCase.backupTasks(tasks)) {
                  dismissLoadingPopup()
                  enqueuePopup(
                     "INFO",
                     "Successfully backed up tasks!\nYou can find it under Download"
                  )
               } else {
                  dismissLoadingPopup()
                  enqueuePopup("ERROR", "Failed to backup tasks...")
               }
            }
         }
      }
   }

   fun onImportButtonPress(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {
         showLoadingPopup()

         Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
         val taskList = deserializeTasksFromJson(contentResolver, filePath)

         if (taskList == null) {
            dismissLoadingPopup()
            enqueuePopup("ERROR", "Failed, presumably corrupted or outdated file")
            return@launch
         }

         if (taskList.isEmpty()) {
            dismissLoadingPopup()
            enqueuePopup("INFO", "No tasks, nothing to do...")
            return@launch
         }

         var codeSuccess = true

         for (task in taskList) {
            codeSuccess =
               when (val resultUpsertTaskAtDatabase = taskUseCase.upsertTaskAtDatabase(task)) {
                  is Result.Error -> false
                  is Result.Success -> true
               }
         }

         var codeSuccess2 = true

         for (task in taskList) {
            codeSuccess2 = when (val resultUpdateTaskAtApi = taskUseCase.updateTaskAtAPI(task)) {
               is Result.Error -> {
                  enqueuePopup("ERROR", resultUpdateTaskAtApi.error.toString())
                  false
               }

               is Result.Success -> resultUpdateTaskAtApi.data
            }
         }

         if (codeSuccess) {
            dismissLoadingPopup()
            enqueuePopup("INFO", "Successfully imported tasks!")
         } else {
            dismissLoadingPopup()
            enqueuePopup("ERROR", "Failure on importing some tasks...")
         }

         if (codeSuccess2) {
            dismissLoadingPopup()
            enqueuePopup("INFO", "Successfully synced tasks!")
         } else {
            dismissLoadingPopup()
            enqueuePopup("ERROR", "Failed to sync tasks...")
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
   ): List<Task>? {
      try {
         val gson = Gson()
         val inputStream = contentResolver.openInputStream(Uri.parse(filePath))
         val reader = BufferedReader(InputStreamReader(inputStream))

         val tasks: List<TaskModel> = gson.fromJson(reader, Array<TaskModel>::class.java).toList()
         reader.close()

         return tasks.map { it.toDomain() }
      } catch (e: Exception) {
         Log.e("LOOK AT ME", "${e.message}")
         return null
      }
   }
}