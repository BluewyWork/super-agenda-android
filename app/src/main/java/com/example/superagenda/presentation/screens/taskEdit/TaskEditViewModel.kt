package com.example.superagenda.presentation.screens.taskEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.TheRestUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val authenticationUseCase: AuthenticationUseCase,
   private val theRestUseCase: TheRestUseCase,
) : ViewModel() {
   private val _taskId = MutableStateFlow(ObjectId())

   private val _title = MutableStateFlow("")
   val title: StateFlow<String> = _title

   private val _description = MutableStateFlow("")
   val description: StateFlow<String> = _description

   private val _taskStatus = MutableStateFlow(TaskStatus.NotStarted)
   val taskStatus: StateFlow<TaskStatus> = _taskStatus

   private val _startDateTime = MutableStateFlow<LocalDateTime?>(null)
   val startDateTime: StateFlow<LocalDateTime?> = _startDateTime

   private val _endEstimatedDateTime = MutableStateFlow<LocalDateTime?>(null)
   val endEstimatedDateTime: StateFlow<LocalDateTime?> = _endEstimatedDateTime

   private val _endDateTime = MutableStateFlow<LocalDateTime?>(null)
   val endDateTime: StateFlow<LocalDateTime?> = _endDateTime

   private val _images = MutableStateFlow<List<String>>(emptyList())
   val images: StateFlow<List<String>> = _images

   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   // Getter & Setters

   fun setTaskId(id: ObjectId) {
      _taskId.value = id
   }

   fun setTitle(title: String) {
      _title.value = title
   }

   fun setDescription(description: String) {
      _description.value = description
   }

   fun setTaskStatus(taskStatus: TaskStatus) {
      _taskStatus.value = taskStatus
   }

   fun setStartDateTime(startDatetime: LocalDateTime?) {
      _startDateTime.value = startDatetime
   }

   fun setEndEstimatedDateTime(endEstimatedDateTime: LocalDateTime?) {
      _endEstimatedDateTime.value = endEstimatedDateTime
   }

   fun setEndDateTime(endDateTime: LocalDateTime) {
      _endEstimatedDateTime.value = endDateTime
   }

   fun setImages(images: List<String>) {
      _images.value = images
   }

   // Utilities

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   // Main

   fun onUpdateButtonPress(onSuccess: () -> Unit) {
      viewModelScope.launch {
         val taskId = _taskId.value
         val title = title.value
         val description = description.value
         val taskStatus = taskStatus.value
         val startDatetime = startDateTime.value
         val endEstimatedDateTime = endEstimatedDateTime.value
         var endDateTime = endDateTime.value
         val images = _images.value

         if (title.isBlank()) {
            _popups.value += Popup("ERROR", "Title can not be empty...")
            return@launch
         }

         if (startDatetime == null || endEstimatedDateTime == null) {
            _popups.value += Popup("ERROR", "Dates can't be empty...")
            return@launch
         }

         if (description.isBlank()) {
            _popups.value += Popup("ERROR", "Description can not be empty...")
            return@launch
         }

         if (taskStatus == TaskStatus.Completed) {
            endDateTime = LocalDateTime.now()
         }

         if (startDatetime >= endEstimatedDateTime) {
            _popups.value += Popup(
               "ERROR", "The expiration date can't be before or the same as the start date"
            )
            return@launch
         }

         val task = Task(
            id = taskId,
            title = title,
            description = description,
            status = taskStatus,
            startDateTime = startDatetime,
            endEstimatedDateTime = endEstimatedDateTime,
            endDateTime = endDateTime,
            images = images
         )

         when (val resultUpsertTaskAtDatabase = taskUseCase.upsertTaskAtDatabase(task)) {
            is Result.Error -> _popups.value += Popup(
               "ERROR",
               "Failed to update task locally...",
               resultUpsertTaskAtDatabase.error.toString()
            )

            is Result.Success -> {
               _popups.value += Popup("INFO", "Successfully updated task locally!")

               when (val resultUpsertLastModifiedAtDatabase =
                  theRestUseCase.upsertLastModifiedAtDatabase(LocalDateTime.now())) {
                  is Result.Error -> _popups.value += Popup(
                     "ERROR",
                     "Failed to update last modified locally...",
                     resultUpsertLastModifiedAtDatabase.error.toString()
                  )

                  is Result.Success -> {

                     when (authenticationUseCase.isLoggedIn()) {
                        is Result.Error -> {
                           _popups.value += Popup(
                              "INFO", "Successfully updated last modified locally!"
                           ) { onSuccess() }
                        }

                        is Result.Success -> {
                           _popups.value += Popup(
                              "INFO", "Successfully updated last modified locally!"
                           )

                           when (val resultUpdateTaskAtApi = taskUseCase.updateTaskAtApi(task)) {
                              is Result.Error -> _popups.value += Popup(
                                 "ERROR",
                                 "Failed to update task at API...",
                                 resultUpdateTaskAtApi.error.toString()
                              ) {
                                 onSuccess()
                              }

                              is Result.Success -> {
                                 _popups.value += Popup("INFO", "Successfully updated task at API!")

                                 when (val resultUpdateLastModifiedAtApi =
                                    theRestUseCase.updateLastModifiedAtApi(LocalDateTime.now())) {
                                    is Result.Error -> _popups.value += Popup(
                                       "ERROR",
                                       "Failed to update last modified at api...",
                                       resultUpdateLastModifiedAtApi.error.toString()
                                    ) {
                                       onSuccess()
                                    }

                                    is Result.Success -> _popups.value += Popup(
                                       "INFO", "Successfully updated last modified at api!"
                                    ) {
                                       onSuccess()
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
   }

   fun onDeleteButtonPress(onSuccess: () -> Unit) {
      viewModelScope.launch {
         val taskId = _taskId.value

         when (val resultDeleteTaskAtDatabase = taskUseCase.deleteTaskAtDatabase(taskId)) {
            is Result.Error -> _popups.value += Popup(
               "ERROR",
               "Failed to delete task locally...",
               resultDeleteTaskAtDatabase.error.toString()
            )

            is Result.Success -> {
               _popups.value += Popup("INFO", "Successfully deleted task locally!")

               when (val result =
                  theRestUseCase.upsertLastModifiedAtDatabase(LocalDateTime.now())) {
                  is Result.Error -> _popups.value += Popup(
                     "ERROR", "Failed to update last modified locally...", result.error.toString()
                  )

                  is Result.Success -> {
                     when (authenticationUseCase.isLoggedIn()) {
                        is Result.Error -> {
                           _popups.value += Popup(
                              "INFO", "Successfully updated last modified locally!"
                           ) {
                              onSuccess()
                           }
                        }

                        is Result.Success -> {
                           _popups.value += Popup(
                              "INFO", "Successfully updated last modified locally!"
                           )

                           when (val resultDeleteTaskAtApi = taskUseCase.deleteTaskAtApi(taskId)) {
                              is Result.Error -> {
                                 _popups.value += Popup(
                                    "ERROR",
                                    "Failed to delete task at api...",
                                    resultDeleteTaskAtApi.error.toString()
                                 ) { onSuccess() }
                              }

                              is Result.Success -> {
                                 _popups.value += Popup("INFO", "Successfully updated task at api!")

                                 when (val resultUpdateLastModifiedAtApi =
                                    theRestUseCase.updateLastModifiedAtApi(LocalDateTime.now())) {
                                    is Result.Error -> _popups.value += Popup(
                                       "ERROR",
                                       "Failed to update last modified at api...",
                                       resultUpdateLastModifiedAtApi.error.toString()
                                    ) { onSuccess() }

                                    is Result.Success -> _popups.value += Popup(
                                       "INFO", "Successfully updated last modified at api!"
                                    ) { onSuccess() }
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
}

data class Popup(
   val title: String = "",
   val description: String = "",
   val error: String = "",
   val code: () -> Unit = {},
)