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
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
   private val task2UseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase,
   private val userUseCase: UserUseCase
) : ViewModel() {
   private val _title = MutableLiveData<String>()
   val title: LiveData<String> = _title

   private val _description = MutableLiveData<String>()
   val description: LiveData<String> = _description

   private val _taskStatus = MutableLiveData<TaskStatus>()
   val taskStatus: LiveData<TaskStatus> = _taskStatus

   private val _startDateTime = MutableLiveData<LocalDateTime>()
   val startDateTime: LiveData<LocalDateTime> = _startDateTime

   private val _endDateTime = MutableLiveData<LocalDateTime>()
   val endDateTime: LiveData<LocalDateTime> = _endDateTime

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

   fun onTitleChange(title: String) {
      _title.postValue(title)
   }

   fun onDescriptionChange(description: String) {
      _description.postValue(description)
   }

   fun onTaskStatusChange(taskStatus: TaskStatus) {
      _taskStatus.postValue(taskStatus)
   }

   fun onStartDateTimeChange(startDatetime: LocalDateTime) {
      _startDateTime.postValue(startDatetime)
   }

   fun onEndDateTimeChange(endDateTime: LocalDateTime) {
      _endDateTime.postValue(endDateTime)
   }

   fun onShow() {
      _title.postValue("")
      _description.postValue("")
      _taskStatus.postValue(TaskStatus.NotStarted)
      _startDateTime.postValue(LocalDateTime.now())
      _endDateTime.postValue(LocalDateTime.now())
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

   private suspend fun whenPopupsEmpty(code: () -> Unit) {
      while (popupsQueue.value?.isNotEmpty() == true) {
         delay(2000)
      }

      code()
   }

   fun onCreateButtonPress(navController: NavController) {
      viewModelScope.launch {
         val title = title.value
         if (title.isNullOrBlank()) {
            enqueuePopup("ERROR", "Title can not be empty...")
            return@launch
         }

         val description = description.value
         if (description.isNullOrBlank()) {
            enqueuePopup("ERROR", "Description can not be empty...")
            return@launch
         }

         val taskStatus = taskStatus.value ?: run {
            enqueuePopup("ERROR", "A status for the task must be selected...")
            return@launch
         }

         val startDatetime = startDateTime.value ?: run {
            enqueuePopup("ERROR", "Must have a start time...")
            return@launch
         }

         val endDateTime = endDateTime.value ?: run {
            enqueuePopup("ERROR", "Must have an end time...")
            return@launch
         }

         if (startDatetime >= endDateTime) {
            enqueuePopup(
               "ERROR",
               "The expiration date can't be before or the same as the start date"
            )
            return@launch
         }

         when (loginUseCase.isLoggedIn()) {
            is Result.Error -> {
               when(val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
                  is Result.Error -> {
                     enqueuePopup("ERROR", "Failed to get tasks locally...", resultGetTasksAtDatabase.error.toString())
                     return@launch
                  }

                  is Result.Success -> {
                     val tasksDatabase = resultGetTasksAtDatabase.data

                     if (tasksDatabase.size > 5) {
                        enqueuePopup("INFO", "You can only create up to 5 tasks with the free plan, consider upgrading to premium")
                        return@launch
                     }
                  }
               }
            }

            is Result.Success -> {
               when(val resultGetUseForProfileAtDatabase = userUseCase.getUserForProfileAtDatabase()) {
                  is Result.Error -> {
                     enqueuePopup("ERROR", "Failed to get user for profile locally...", resultGetUseForProfileAtDatabase.error.toString())
                  }

                  is Result.Success -> {
                     val userForProfile = resultGetUseForProfileAtDatabase.data

                     when(userForProfile.membership) {
                        Membership.FREE -> {
                           when(val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
                              is Result.Error -> {
                                 enqueuePopup("ERROR", "Failed to get tasks locally...", resultGetTasksAtDatabase.error.toString())
                                 return@launch
                              }

                              is Result.Success -> {
                                 val tasksDatabase = resultGetTasksAtDatabase.data

                                 if (tasksDatabase.size > 5) {
                                    enqueuePopup("INFO", "You can only create up to 5 tasks with the free plan, consider upgrading to premium")
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
            endDateTime = endDateTime
         )

         when (val resultUpsertTaskAtDatabase = task2UseCase.upsertTaskAtDatabase(task)) {
            is Result.Error ->
               enqueuePopup("ERROR", "Failed to upsert task locally...", resultUpsertTaskAtDatabase.error.toString())

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
                           enqueuePopup("ERROR", "Failed to create task at API...", resultCreateTaskAtApi.error.toString())

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