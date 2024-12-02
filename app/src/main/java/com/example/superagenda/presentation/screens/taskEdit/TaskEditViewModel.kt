package com.example.superagenda.presentation.screens.taskEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.util.Result
import com.example.superagenda.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase,
) : ViewModel() {
   private val _taskId = MutableStateFlow(ObjectId())

   private val _title = MutableStateFlow("")
   val title: StateFlow<String> = _title

   private val _description = MutableStateFlow("")
   val description: StateFlow<String> = _description

   private val _taskStatus = MutableStateFlow(TaskStatus.NotStarted)
   val taskStatus: StateFlow<TaskStatus> = _taskStatus

   private val _startDateTime = MutableStateFlow(LocalDateTime.now())
   val startDateTime: StateFlow<LocalDateTime> = _startDateTime

   private val _endEstimatedDateTime = MutableStateFlow(LocalDateTime.now())
   val endEstimatedDateTime: StateFlow<LocalDateTime> = _endEstimatedDateTime

   private val _endDateTime = MutableStateFlow(LocalDateTime.now())
   val endDateTime: StateFlow<LocalDateTime> = _endDateTime

   private val _images = MutableStateFlow<List<String>>(emptyList())
   val images: StateFlow<List<String>> = _images

   private val _popupsQueue = MutableLiveData<List<Triple<String, String, String>>>()
   val popupsQueue: LiveData<List<Triple<String, String, String>>> = _popupsQueue

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

   fun setStartDateTime(startDatetime: LocalDateTime) {
      _startDateTime.value = startDatetime
   }

   fun setEndEstimatedDateTime(endEstimatedDateTime: LocalDateTime) {
      _endEstimatedDateTime.value = endEstimatedDateTime
   }

   fun setEndDateTime(endDateTime: LocalDateTime) {
      _endEstimatedDateTime.value = endDateTime
   }

   fun setImages(images: List<String>) {
      _images.value = images
   }

   // Utilities

   fun enqueuePopup(title: String, description: String, error: String = "") {
      _popupsQueue.value =
         popupsQueue.value?.plus(Triple(title, description, error)) ?: listOf(
            Triple(
               title,
               description,
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

   fun onUpdateButtonPress(navController: NavController) {
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
            enqueuePopup("ERROR", "Title can not be empty...")
            return@launch
         }

         if (description.isBlank()) {
            enqueuePopup("ERROR", "Description can not be empty...")
            return@launch
         }

         if (taskStatus == TaskStatus.Completed) {
            endDateTime = LocalDateTime.now()
         }

         if (startDatetime >= endEstimatedDateTime) {
            enqueuePopup(
               "ERROR",
               "The expiration date can't be before or the same as the start date"
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
            is Result.Error ->
               enqueuePopup(
                  "ERROR",
                  "Failed to update task locally...",
                  resultUpsertTaskAtDatabase.error.toString()
               )

            is Result.Success -> {
               enqueuePopup("INFO", "Successfully updated task locally!")

               loginUseCase.isLoggedIn().onSuccess {
                  when (val resultUpdateTaskAtApi = taskUseCase.updateTaskAtAPI(task)) {
                     is Result.Error ->
                        enqueuePopup(
                           "ERROR",
                           "Failed to update task at API...",
                           resultUpdateTaskAtApi.error.toString()
                        )

                     is Result.Success -> {
                        enqueuePopup("INFO", "Successfully updated task at API!")
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

   fun onDeleteButtonPress(onSuccess: () -> Unit) {
      viewModelScope.launch {
         val taskId = _taskId.value

         when (val resultDeleteTaskAtDatabase = taskUseCase.deleteTasksAtDatabase()) {
            is Result.Error ->
               enqueuePopup(
                  "ERROR",
                  "Failed to delete task locally...",
                  resultDeleteTaskAtDatabase.error.toString()
               )

            is Result.Success -> {
               when (val re = loginUseCase.isLoggedIn()) {
                  is Result.Error -> {
                     onSuccess()
                  }

                  is Result.Success -> {
                     when (val resultDeleteTaskAtApi = taskUseCase.deleteTaskAtAPI(taskId)) {
                        is Result.Error -> {
                           enqueuePopup(
                              "ERROR",
                              "Failed to delete task at api...",
                              resultDeleteTaskAtApi.error.toString()
                           )

                           onSuccess()
                        }

                        is Result.Success -> {
                           enqueuePopup("INFO", "Successfully updated task at api!")
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