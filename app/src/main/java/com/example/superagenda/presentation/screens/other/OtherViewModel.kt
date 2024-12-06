package com.example.superagenda.presentation.screens.other

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.Result
import com.example.superagenda.util.onSuccess
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase,
) : ViewModel() {
   private val _popupsQueue = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
   val popupsQueue: StateFlow<List<Triple<String, String, String>>> = _popupsQueue

   private val _tasksToResolve = MutableStateFlow<List<Task>>(emptyList())
   val tasksToResolve: StateFlow<List<Task>> = _tasksToResolve

   // Utilities

   fun enqueuePopup(title: String, message: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value + Triple(title, message, error)
   }

   fun dismissPopup() {
      _popupsQueue.value = _popupsQueue.value.drop(1)
   }

   // Main

   fun onBackUpButtonPress() {
      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               enqueuePopup("ERROR", "Failed, no tasks to backup...")
            }

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data

               // TODO: Update this function to use AppResult
               if (taskUseCase.backupTasks(tasks)) {
                  enqueuePopup(
                     "INFO",
                     "Successfully backed up tasks!\nYou can find it under Download"
                  )
               } else {
                  enqueuePopup("ERROR", "Failed to backup tasks...")
               }
            }
         }
      }
   }

   fun onImportButtonPress(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {
         Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
         val taskList = deserializeTasksFromJson(contentResolver, filePath)

         if (taskList == null) {
            enqueuePopup("ERROR", "Failed, presumably corrupted or outdated file")
            return@launch
         }

         if (taskList.isEmpty()) {
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
            codeSuccess2 = when (val resultUpdateTaskAtApi = taskUseCase.updateTaskAtApi(task)) {
               is Result.Error -> {
                  enqueuePopup("ERROR", resultUpdateTaskAtApi.error.toString())
                  false
               }

               is Result.Success -> resultUpdateTaskAtApi.data
            }
         }

         if (codeSuccess) {
            enqueuePopup("INFO", "Successfully imported tasks!")
         } else {
            enqueuePopup("ERROR", "Failure on importing some tasks...")
         }

         if (codeSuccess2) {
            enqueuePopup("INFO", "Successfully synced tasks!")
         } else {
            enqueuePopup("ERROR", "Failed to sync tasks...")
         }
      }
   }

   fun onImport2Press(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {
         Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
         val tasksImported = deserializeTasksFromJson(contentResolver, filePath)

         if (tasksImported == null) {
            enqueuePopup("ERROR", "Failed, presumably corrupted or outdated file")
            return@launch
         }

         if (tasksImported.isEmpty()) {
            enqueuePopup("INFO", "No tasks, nothing to do...")
            return@launch
         }

         when (val resultGetLocalTasks = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               enqueuePopup("ERROR", "Failed to retrieve local tasks...")
               return@launch
            }

            is Result.Success -> {
               val tasksLocal = resultGetLocalTasks.data
               val differingTasks = mutableListOf<Task>()

               for (importedTask in tasksImported) {
                  val localTask = tasksLocal.find { it.id == importedTask.id }

                  if (localTask != null && localTask != importedTask) {
                     differingTasks.add(importedTask)
                  }
               }

               _tasksToResolve.value = differingTasks
            }
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

         val tasks: List<TaskModel> =
            gson.fromJson(reader, Array<TaskModel>::class.java).toList()
         reader.close()

         return tasks.map { it.toDomain() }
      } catch (e: Exception) {
         Log.e("LOOK AT ME", "${e.message}")
         return null
      }
   }

   fun resolveTask(task: Task, taskResolutionOptions: TaskResolutionOptions) {
      viewModelScope.launch {
         when (taskResolutionOptions) {
            TaskResolutionOptions.KEEP_ORIGINAL -> {
               _tasksToResolve.update { it - task }
            }

            TaskResolutionOptions.KEEP_NEW -> {
               when (val result = taskUseCase.upsertTaskAtDatabase(task)) {
                  is Result.Error -> {
                     enqueuePopup(
                        "ERROR",
                        "Failed to update task locally...",
                        result.error.toString()
                     )

                     return@launch
                  }

                  is Result.Success -> _tasksToResolve.update { it - task }
               }

               val isLoggedInResult = loginUseCase.isLoggedIn()

               isLoggedInResult.onSuccess {
                  when (val result = taskUseCase.updateTaskAtApi(task)) {
                     is Result.Error -> enqueuePopup(
                        "ERROR",
                        "Failed to update task at api...",
                        result.error.toString()
                     )

                     is Result.Success -> enqueuePopup("INFO", "Successfully updated task!")
                  }
               }
            }

            TaskResolutionOptions.KEEP_BOTH -> {
               val taskShadowed = Task(
                  ObjectId(),
                  task.title,
                  task.description,
                  task.status,
                  task.startDateTime,
                  task.endDateTime,
                  task.endEstimatedDateTime,
                  task.images
               )

               when (val result = taskUseCase.upsertTaskAtDatabase(taskShadowed)) {
                  is Result.Error -> TODO()
                  is Result.Success -> TODO()
               }
            }
         }
      }

   }
}

enum class TaskResolutionOptions {
   KEEP_ORIGINAL,
   KEEP_NEW,
   KEEP_BOTH
}