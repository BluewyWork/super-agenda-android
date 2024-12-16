package com.example.superagenda.presentation.screens.newTask

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.AuthenticationUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.TheRestUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.Membership
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
class NewTaskViewModel @Inject constructor(
   private val task2UseCase: TaskUseCase,
   private val authenticationUseCase: AuthenticationUseCase,
   private val userUseCase: UserUseCase,
   private val theRestUseCase: TheRestUseCase,
) : ViewModel() {
   private val _title = MutableStateFlow("")
   val title: StateFlow<String> = _title

   private val _description = MutableStateFlow("")
   val description: StateFlow<String> = _description

   private val _taskStatus = MutableStateFlow<TaskStatus>(TaskStatus.NotStarted)
   val taskStatus: StateFlow<TaskStatus> = _taskStatus

   private val _startDateTime = MutableStateFlow<LocalDateTime?>(null)
   val startDateTime: StateFlow<LocalDateTime?> = _startDateTime

   private val _endEstimatedDateTime = MutableStateFlow<LocalDateTime?>(null)
   val endEstimatedDateTime: StateFlow<LocalDateTime?> = _endEstimatedDateTime

   private val _images = MutableStateFlow<List<String>>(emptyList())
   val images: StateFlow<List<String>> = _images

   private val _popups = MutableStateFlow<List<Popup>>(emptyList())
   val popups: StateFlow<List<Popup>> = _popups

   fun onTitleChanged(title: String) {
      _title.value = title
   }

   fun onDescriptionChanged(description: String) {
      _description.value = description
   }

   fun onTaskStatusChanged(taskStatus: TaskStatus) {
      _taskStatus.value = taskStatus
   }

   fun onStartDateTimeChanged(startDatetime: LocalDateTime?) {
      _startDateTime.value = startDatetime
   }

   fun onEndEstimatedDateTimeChanged(endDateTime: LocalDateTime?) {
      _endEstimatedDateTime.value = endDateTime
   }

   fun onImagesChanged(images: List<String>) {
      _images.value = images
   }

   fun onPopupDismissed() {
      _popups.value = _popups.value.drop(1)
   }

   // Main

   fun onCreateButtonPress(onSuccess: () -> Unit) {
      viewModelScope.launch {
         val title = _title.value
         val description = _description.value
         val taskStatus = _taskStatus.value
         val startDatetime = _startDateTime.value
         val endEstimatedDateTime = _endEstimatedDateTime.value
         val images = _images.value

         if (title.isBlank()) {
            _popups.value += Popup("ERROR", "Title can not be empty...")
            return@launch
         }

         if (description.isBlank()) {
            _popups.value += Popup("ERROR", "Description can not be empty...")
            return@launch
         }

         if (startDatetime == null || endEstimatedDateTime == null) {
            _popups.value += Popup("ERROR", "Dates can't be empty...")
            return@launch
         }

         if (startDatetime >= endEstimatedDateTime) {
            _popups.value += Popup(
               "ERROR", "The expiration date can't be before or the same as the start date"
            )

            return@launch
         }

         val now = LocalDateTime.now()

         when (authenticationUseCase.isLoggedIn()) {
            is Result.Error -> {
               when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
                  is Result.Error -> {
                     _popups.value += Popup(
                        "ERROR",
                        "Failed to get tasks locally...",
                        resultGetTasksAtDatabase.error.toString()
                     )

                     return@launch
                  }

                  is Result.Success -> {
                     val tasksDatabase = resultGetTasksAtDatabase.data

                     if (tasksDatabase.size > 4) {
                        _popups.value += Popup(
                           "INFO",
                           "You can only create up to 5 tasks with the free plan, consider upgrading to premium"
                        )

                        return@launch
                     }
                  }
               }
            }

            is Result.Success -> {
               when (val resultGetUseForProfileAtDatabase =
                  userUseCase.getUserForProfileAtDatabase()) {
                  is Result.Error -> {
                     _popups.value += Popup(
                        "ERROR",
                        "Failed to get user for profile locally...",
                        resultGetUseForProfileAtDatabase.error.toString()
                     )
                  }

                  is Result.Success -> {
                     val userForProfile = resultGetUseForProfileAtDatabase.data

                     when (userForProfile.membership) {
                        Membership.FREE -> {
                           when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
                              is Result.Error -> {
                                 _popups.value += Popup(
                                    "ERROR",
                                    "Failed to get tasks locally...",
                                    resultGetTasksAtDatabase.error.toString()
                                 )

                                 return@launch
                              }

                              is Result.Success -> {
                                 val tasksDatabase = resultGetTasksAtDatabase.data

                                 if (tasksDatabase.size > 4) {
                                    _popups.value += Popup(
                                       "INFO",
                                       "You can only create up to 5 tasks with the free plan, consider upgrading to premium"
                                    )

                                    return@launch
                                 }
                              }
                           }
                        }

                        Membership.PREMIUM -> {
                           // proceed
                        }
                     }
                  }
               }
            }
         }

         val task = Task(
            id = ObjectId(),
            title = title,
            description = description,
            status = taskStatus,
            startDateTime = startDatetime,
            endEstimatedDateTime = endEstimatedDateTime,
            endDateTime = null,
            images = images
         )

         val resultLoggedIn = authenticationUseCase.isLoggedIn()

         when (val resultUpsertTaskAtDatabase = task2UseCase.upsertTaskAtDatabase(task)) {
            is Result.Error -> _popups.value += Popup(
               "ERROR",
               "Failed to upsert task locally...",
               resultUpsertTaskAtDatabase.error.toString()
            )

            is Result.Success -> {
               _popups.value += Popup("INFO", "Successfully created task locally!")

               when (val resultUpsertLastModifiedAtDatabase =
                  theRestUseCase.upsertLastModifiedAtDatabase(now)) {
                  is Result.Error -> {

                     _popups.value += Popup(
                        "ERROR",
                        "Failed to update last modified...",
                        resultUpsertLastModifiedAtDatabase.error.toString()
                     ) { onSuccess() }

                  }

                  is Result.Success -> {

                     when (resultLoggedIn) {
                        is Result.Error -> {
                           _popups.value += Popup(
                              "INFO", "Successfully update last modified!"
                           ) {
                              onSuccess()
                           }
                        }

                        is Result.Success -> {
                           _popups.value += Popup(
                              "INFO", "Successfully update last modified!"
                           )

                           when (val resultCreateTaskAtApi = task2UseCase.createTaskAtApi(task)) {
                              is Result.Error -> {
                                 _popups.value += Popup(
                                    "ERROR",
                                    "Failed to create task at API...",
                                    resultCreateTaskAtApi.error.toString()
                                 ) {
                                    onSuccess()
                                 }
                              }

                              is Result.Success -> {
                                 _popups.value += Popup(
                                    "INFO", "Successfully created task at API!"
                                 )

                                 when (val resultUpdateLastModifiedAtApi =
                                    theRestUseCase.updateLastModifiedAtApi(now)) {
                                    is Result.Error -> {
                                       _popups.value += Popup(
                                          "INFO", "Successfully created task at API!"
                                       )

                                       _popups.value += Popup(
                                          "ERROR",
                                          "Failed to update last modified at api...",
                                          resultUpdateLastModifiedAtApi.error.toString()
                                       ) {
                                          onSuccess()
                                       }
                                    }

                                    is Result.Success -> {
                                       _popups.value += Popup(
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

               _title.value = ""
               _description.value = ""
               _taskStatus.value = TaskStatus.NotStarted
               _startDateTime.value = null
               _endEstimatedDateTime.value = null
               _images.value = emptyList()
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