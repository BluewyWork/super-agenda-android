package com.example.superagenda.presentation.screens.other

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.TheRestUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.Result
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OtherViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val authenticationUseCase: AuthenticationUseCase,
   private val theRestUseCase: TheRestUseCase,
) : ViewModel() {
   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   private val _tasksToResolve = MutableStateFlow<List<Task>>(emptyList())
   val tasksToResolve: StateFlow<List<Task>> = _tasksToResolve


   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   fun onBackUpButtonPressed() {
      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               _popups.value += Popup("ERROR", "Failed, no tasks to backup...")
            }

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data

               // TODO: Update this function to use AppResult
               if (taskUseCase.backupTasks(tasks)) {
                  _popups.value += Popup(
                     "INFO", "Successfully backed up tasks!\nYou can find it under Download"
                  )
               } else {
                  _popups.value += Popup("ERROR", "Failed to backup tasks...")
               }
            }
         }
      }
   }

   fun onImportPressed(contentResolver: ContentResolver, filePath: String) {
      viewModelScope.launch {
         Log.d("LOOK AT ME", "CHOSEN FILE: ${printFileContents(contentResolver, filePath)}")
         val tasksImported = deserializeTasksFromJson(contentResolver, filePath)

         if (tasksImported == null) {
            _popups.value += Popup("ERROR", "Failed, presumably corrupted or outdated file")
            return@launch
         }

         if (tasksImported.isEmpty()) {
            _popups.value += Popup("INFO", "No tasks, nothing to do...")
            return@launch
         }

         when (val resultGetLocalTasks = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               _popups.value += Popup("ERROR", "Failed to retrieve local tasks...")
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


   fun resolveTask(task: Task, taskResolutionOptions: TaskResolutionOptions) {
      viewModelScope.launch {
         when (taskResolutionOptions) {
            TaskResolutionOptions.KEEP_ORIGINAL -> {
               _tasksToResolve.update { it - task }
            }

            TaskResolutionOptions.KEEP_NEW -> {
               when (val resultUpsertTaskAtDatabase = taskUseCase.upsertTaskAtDatabase(task)) {
                  is Result.Error -> {
                     _popups.value += Popup(
                        "ERROR",
                        "Failed to update task locally...",
                        resultUpsertTaskAtDatabase.error.toString()
                     )
                  }

                  is Result.Success -> {
                     _popups.value += Popup("INFO", "Successfully updated task...")
                     _tasksToResolve.update { it - task }


                     when (val resultUpsertLastModifiedAtDatabase =
                        theRestUseCase.upsertLastModifiedAtDatabase(LocalDateTime.now())) {
                        is Result.Error -> _popups.value += Popup(
                           "ERROR",
                           "Failed to update last modified...",
                           resultUpsertLastModifiedAtDatabase.error.toString()
                        )

                        is Result.Success -> {
                           when (authenticationUseCase.isLoggedIn()) {
                              is Result.Error -> {}

                              is Result.Success -> {
                                 when (val resultUpdateTaskAtApi =
                                    taskUseCase.updateTaskAtApi(task)) {
                                    is Result.Error -> _popups.value += Popup(
                                       "ERROR",
                                       "Failed to update task at api...",
                                       resultUpdateTaskAtApi.error.toString()
                                    )

                                    is Result.Success -> _popups.value += Popup(
                                       "INFO", "Successfully updated task!"
                                    )
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            TaskResolutionOptions.KEEP_BOTH -> {
               val taskNew = Task(
                  ObjectId(),
                  task.title,
                  task.description,
                  task.status,
                  task.startDateTime,
                  task.endDateTime,
                  task.endEstimatedDateTime,
                  task.images
               )

               when (val resultUpsertTaskAtDatabase =
                  taskUseCase.upsertTaskAtDatabase(taskNew)) {
                  is Result.Error -> _popups.value += Popup(
                     "ERROR",
                     "Failed to upsert task locally...",
                     resultUpsertTaskAtDatabase.error.toString()
                  )

                  is Result.Success -> {
                     _tasksToResolve.update { it - task }
                     _popups.value += Popup("INFO", "Successfully upserted task locally!")


                     when (val resultUpsertLastModifiedAtDatabase =
                        theRestUseCase.upsertLastModifiedAtDatabase(LocalDateTime.now())) {
                        is Result.Error -> _popups.value += Popup(
                           "ERROR",
                           "Failed to update last modified...",
                           resultUpsertLastModifiedAtDatabase.error.toString()
                        )

                        is Result.Success -> {
                           when (authenticationUseCase.isLoggedIn()) {
                              is Result.Error -> {}

                              is Result.Success -> {
                                 when (val resultCreateTaskAtApi =
                                    taskUseCase.createTaskAtApi(taskNew)) {
                                    is Result.Error -> _popups.value += Popup(
                                       "ERROR",
                                       "Failed to create task at api...",
                                       resultCreateTaskAtApi.error.toString()
                                    )

                                    is Result.Success -> _popups.value += Popup(
                                       "INFO", "Successfully created task at api!"
                                    )
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
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

enum class TaskResolutionOptions {
   KEEP_ORIGINAL, KEEP_NEW, KEEP_BOTH
}

data class Popup(
   val title: String = "",
   val description: String = "",
   val error: String = "",
   val code: () -> Unit = {},
)
