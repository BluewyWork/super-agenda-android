package com.example.superagenda.presentation.screens.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.UserUseCase
import com.example.superagenda.domain.models.Membership
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
   private val task2UseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase,
   private val userUseCase: UserUseCase,
) : ViewModel() {
   private val _title = MutableStateFlow("")
   val title: StateFlow<String> = _title

   private val _description = MutableStateFlow("")
   val description: StateFlow<String> = _description

   private val _taskStatus = MutableStateFlow<TaskStatus>(TaskStatus.NotStarted)
   val taskStatus: StateFlow<TaskStatus> = _taskStatus

   private val _startDateTime = MutableStateFlow(LocalDateTime.now())
   val startDateTime: StateFlow<LocalDateTime> = _startDateTime

   private val _endEstimatedDateTime = MutableStateFlow(LocalDateTime.now())
   val endEstimatedDateTime: StateFlow<LocalDateTime> = _endEstimatedDateTime

   private val _images = MutableStateFlow<List<String>>(emptyList())
   val images: StateFlow<List<String>> = _images

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

   // Getters & Setters

   fun setTitle(title: String) {
      _title.value = title
   }

   fun setDescription(description: String) {
      _description.value = description
   }

   fun setTaskStatus(taskStatus: TaskStatus) {
      _taskStatus.value = taskStatus
   }

   fun setStartDateTime(startDatetime: LocalDateTime) {
      _startDateTime.value = startDatetime
   }

   fun setEndEstimatedDateTime(endDateTime: LocalDateTime) {
      _endEstimatedDateTime.value = endDateTime
   }

   fun setImages(images: List<String>) {
      _images.value = images
   }

   // Utilities

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

   private suspend fun whenPopupsEmpty(code: () -> Unit) {
      while (popupsQueue.value?.isNotEmpty() == true) {
         delay(2000)
      }

      code()
   }

   // Main

   fun onCreateButtonPress(navController: NavController) {
      viewModelScope.launch {
         val title = _title.value
         val description = _description.value
         val taskStatus = _taskStatus.value
         val startDatetime = _startDateTime.value
         val endEstimatedDateTime = _endEstimatedDateTime.value
         val images = _images.value

         if (title.isBlank()) {
            enqueuePopup("ERROR", "Title can not be empty...")
            return@launch
         }

         if (description.isBlank()) {
            enqueuePopup("ERROR", "Description can not be empty...")
            return@launch
         }

         if (startDatetime >= endEstimatedDateTime) {
            enqueuePopup(
               "ERROR",
               "The expiration date can't be before or the same as the start date"
            )

            return@launch
         }

         when (loginUseCase.isLoggedIn()) {
            is Result.Error -> {
               when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
                  is Result.Error -> {
                     enqueuePopup(
                        "ERROR",
                        "Failed to get tasks locally...",
                        resultGetTasksAtDatabase.error.toString()
                     )
                     return@launch
                  }

                  is Result.Success -> {
                     val tasksDatabase = resultGetTasksAtDatabase.data

                     if (tasksDatabase.size > 5) {
                        enqueuePopup(
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
                     enqueuePopup(
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
                                 enqueuePopup(
                                    "ERROR",
                                    "Failed to get tasks locally...",
                                    resultGetTasksAtDatabase.error.toString()
                                 )
                                 return@launch
                              }

                              is Result.Success -> {
                                 val tasksDatabase = resultGetTasksAtDatabase.data

                                 if (tasksDatabase.size > 5) {
                                    enqueuePopup(
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
            endDateTime = LocalDateTime.now(),
            images = images
         )

         when (val resultUpsertTaskAtDatabase = task2UseCase.upsertTaskAtDatabase(task)) {
            is Result.Error ->
               enqueuePopup(
                  "ERROR",
                  "Failed to upsert task locally...",
                  resultUpsertTaskAtDatabase.error.toString()
               )

            is Result.Success -> {
               enqueuePopup("INFO", "Successfully created task locally!")

               // here because i don't want it to attempt if saving locally fails in the
               // first place
               when (val resultLoggedIn = loginUseCase.isLoggedIn()) {
                  is Result.Error -> {
                     // skip cause not logged in
                  }

                  is Result.Success -> {
                     when (val resultCreateTaskAtApi = task2UseCase.createTaskAtApi(task)) {
                        is Result.Error ->
                           enqueuePopup(
                              "ERROR",
                              "Failed to create task at API...",
                              resultCreateTaskAtApi.error.toString()
                           )

                        is Result.Success ->
                           enqueuePopup("INFO", "Successfully created task at API!")
                     }
                  }
               }

               whenPopupsEmpty {
                  navController.navigateUp()
               }
            }
         }
      }
   }
}