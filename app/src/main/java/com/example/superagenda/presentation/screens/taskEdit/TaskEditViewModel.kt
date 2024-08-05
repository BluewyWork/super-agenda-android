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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase
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

   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

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

         val endDateTime = endDateTime.value ?: run {
            enqueuePopup("ERROR", "Must have an end time...")
            return@launch
         }

         val taskID = taskToEdit.value?._id

         if (taskID == null) {
            enqueuePopup("ERROR", "Wow, something changed it to null")
            return@launch
         }

         val task = Task(
            _id = taskID,
            title = title,
            description = description,
            status = taskStatus,
            startDateTime = startDatetime,
            endDateTime = endDateTime
         )

         if (!taskUseCase.insertOrUpdateTaskAtLocalDatabase(task)) {
            enqueuePopup("ERROR", "Task Update Failed...")
            return@launch
         } else {
            enqueuePopup("INFO", "Task Updated!")
         }

         if (!loginUseCase.isLoggedIn()) {
            return@launch
         }

         if (!taskUseCase.updateTaskAtAPI(task)) {
            enqueuePopup("ERROR", "API Sync Failed...")
            return@launch
         } else {
            enqueuePopup("INFO", "Task Synced!")
         }
      }
   }

   fun onDeleteButtonPress(navController: NavController) {
      viewModelScope.launch {

      }
   }
}