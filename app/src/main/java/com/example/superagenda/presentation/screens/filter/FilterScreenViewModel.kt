package com.example.superagenda.presentation.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _filteredTaskList = MutableStateFlow<List<Task>>(emptyList())
   val filteredTaskList: StateFlow<List<Task>> = _filteredTaskList

   private val _title = MutableStateFlow<String>("")
   val title: StateFlow<String> = _title

   private val _taskStatus = MutableStateFlow<TaskStatus?>(null)
   val taskStatus: StateFlow<TaskStatus?> = _taskStatus

   private val _startDateTime = MutableStateFlow<LocalDateTime?>(null)
   val startDateTime: StateFlow<LocalDateTime?> = _startDateTime

   private val _endEstimatedDateTime = MutableStateFlow<LocalDateTime?>(null)
   val endEstimatedDateTime: StateFlow<LocalDateTime?> = _endEstimatedDateTime

   private val _popupsQueue = MutableStateFlow<List<Triple<String, String, String>>>(emptyList())
   val popupsQueue: StateFlow<List<Triple<String, String, String>>> = _popupsQueue

   private val _taskToEdit = MutableStateFlow<Task?>(null)
   val taskToEdit: StateFlow<Task?> = _taskToEdit

   // Getters & Setters

   fun setTaskToEdit(task: Task) {
      _taskToEdit.value = task
   }

   fun getTaskToEdit(): Task? {
      return _taskToEdit.value
   }

   fun onTitleChange(title: String) {
      _title.value = title
   }

   fun onTaskStatusChange(taskStatus: TaskStatus?) {
      _taskStatus.value = taskStatus
   }

   fun setStartDateTime(startDatetime: LocalDateTime?) {
      _startDateTime.value = startDatetime
   }

   fun setEndEstimatedTime(endDateTime: LocalDateTime?) {
      _endEstimatedDateTime.value = endDateTime
   }

   // Utilities

   fun enqueuePopup(title: String, message: String, error: String) {
      _popupsQueue.value =
         popupsQueue.value + Triple(title, message, error)
   }

   fun dismissPopup() {
      _popupsQueue.value = _popupsQueue.value.drop(1)
   }

   // Main

   fun onFilterPress() {
      viewModelScope.launch {
         val tasks = when (val result = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> {
               enqueuePopup("ERROR", "Failed to get tasks locally...", result.error.toString())
               return@launch
            }

            is Result.Success -> result.data
         }

         val filteredTasks = tasks
            .filter { task ->
               val start = _startDateTime.value ?: LocalDateTime.MIN
               val end = _endEstimatedDateTime.value ?: LocalDateTime.MAX
               task.startDateTime >= start && task.endEstimatedDateTime <= end
            }
            .filter { task ->
               val status = _taskStatus.value
               status == null || task.status == status
            }
            .filter { task ->
               val titleFilter = _title.value.trim()
               titleFilter.isEmpty() || task.title.contains(titleFilter, ignoreCase = true)
            }

         _filteredTaskList.value = filteredTasks
      }
   }
}
