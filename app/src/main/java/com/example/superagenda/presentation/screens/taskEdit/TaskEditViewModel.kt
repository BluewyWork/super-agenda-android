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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase,
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

   private val _taskToEdit = MutableLiveData<Task>()
   val taskToEdit: LiveData<Task> = _taskToEdit

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

   fun setTaskToEdit(task: Task) {
      _taskToEdit.postValue(task)
   }

   fun onShow(navController: NavController) {
      val task = taskToEdit.value

      if (task == null) {
         navController.navigateUp()
         return
      }

      _title.postValue(task.title)
      _description.postValue(task.description)
      _taskStatus.postValue(task.status)
      _startDateTime.postValue(task.startDateTime)
      _endDateTime.postValue(task.endDateTime)
   }

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

   fun onUpdateButtonPress(navController: NavController) {
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

         var endDateTime = endDateTime.value ?: run {
            enqueuePopup("ERROR", "Must have an end time...")
            return@launch
         }

         val taskID = taskToEdit.value?.id

         if (taskID == null) {
            enqueuePopup("ERROR", "Wow, something changed it to null")
            return@launch
         }

         if (taskStatus == TaskStatus.Completed) {
            endDateTime = LocalDateTime.now()
         }

         if (startDatetime >= endDateTime) {
            enqueuePopup(
               "ERROR",
               "The expiration date can't be before or the same as the start date"
            )
            return@launch
         }

         val task = Task(
            id = taskID,
            title = title,
            description = description,
            status = taskStatus,
            startDateTime = startDatetime,
            endDateTime = endDateTime
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
         val taskID = taskToEdit.value?.id

         if (taskID == null) {
            enqueuePopup("ERROR", "Hmmm, taskID null, how?...")
            return@launch
         }

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
                     when (val resultDeleteTaskAtApi = taskUseCase.deleteTaskAtAPI(taskID)) {
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