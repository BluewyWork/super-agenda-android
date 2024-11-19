package com.example.superagenda.presentation.screens.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _filteredTaskList = MutableLiveData<List<Task>>()
   val filteredTaskList: LiveData<List<Task>> = _filteredTaskList

   private val _title = MutableLiveData<String>()
   val title: LiveData<String> = _title

   private val _taskStatus = MutableLiveData<TaskStatus>()
   val taskStatus: LiveData<TaskStatus> = _taskStatus

   private val _startDateTime = MutableLiveData<LocalDateTime>()
   val startDateTime: LiveData<LocalDateTime> = _startDateTime

   private val _endDateTime = MutableLiveData<LocalDateTime>()
   val endDateTime: LiveData<LocalDateTime> = _endDateTime

   private val _popupsQueue = MutableLiveData<List<Pair<String, String>>>()
   val popupsQueue: LiveData<List<Pair<String, String>>> = _popupsQueue

   private val _taskToEdit = MutableLiveData<Task>()
   val taskToEdit: LiveData<Task> = _taskToEdit

   fun setTaskToEdit(task: Task) {
      _taskToEdit.postValue(task)
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

   fun onFilterPress() {
      viewModelScope.launch {
         val tasks = taskUseCase.getTasksAtDatabase()

         if (tasks == null) {
            enqueuePopup("ERROR", "Failed to retrieve tasks from local storage...")

            return@launch
         }

         val filteredTasks = tasks
            .filter { task ->
               val start = _startDateTime.value ?: LocalDateTime.MIN
               val end = _endDateTime.value ?: LocalDateTime.MAX
               task.startDateTime >= start && task.endDateTime <= end
            }
            .filter { task ->
               val status = _taskStatus.value
               status == null || task.status == status
            }
            .filter { task ->
               val titleFilter = _title.value?.trim().orEmpty()
               titleFilter.isEmpty() || task.title.contains(titleFilter, ignoreCase = true)
            }

         _filteredTaskList.postValue(filteredTasks)
      }
   }

   fun onTitleChange(title: String) {
      _title.postValue(title)
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
}
