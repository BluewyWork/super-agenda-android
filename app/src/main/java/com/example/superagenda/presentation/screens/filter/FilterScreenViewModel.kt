package com.example.superagenda.presentation.screens.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.screens.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val globalVariables: GlobalVariables
) : ViewModel() {
   private val _loadedTaskList = MutableLiveData<List<Task>>()
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

   private val _errorMessage = MutableLiveData<String?>()
   val errorMessage: LiveData<String?> = _errorMessage

   fun onError(message: String) {
      _errorMessage.postValue(message)
   }

   fun onErrorDismissed() {
      _errorMessage.postValue(null)
   }

   fun onShow() {
      viewModelScope.launch {
         val taskList: List<Task> = taskUseCase.retrieveTaskList2() ?: return@launch
         _loadedTaskList.postValue(taskList)
      }
   }

   fun onFilter(navController: NavController) {
      viewModelScope.launch {
         val title = _title.value
         val taskStatus = _taskStatus.value
         val startDateTime = _startDateTime.value
         val endDateTime = _endDateTime.value

         val filteredList = _loadedTaskList.value?.filter { task ->
            val matchesTitle =
               title.isNullOrEmpty() || task.title.contains(title, ignoreCase = true)
            val matchesStatus = taskStatus == null || task.status == taskStatus
            val matchesStartDateTime =
               startDateTime == null || task.startDateTime.isEqual(startDateTime) || task.startDateTime.isAfter(
                  startDateTime
               )
            val matchesEndDateTime =
               endDateTime == null || task.endDateTime.isEqual(endDateTime) || task.endDateTime.isBefore(
                  endDateTime
               )

            matchesTitle && matchesStatus && matchesStartDateTime && matchesEndDateTime
         } ?: emptyList()

         _filteredTaskList.postValue(filteredList)
      }
   }

   fun onTitleChange(title: String) {
      _title.postValue(title)
   }

   fun onTaskStatusChange(taskStatus: TaskStatus) {
      Log.d("LOOK AT ME", "ON task status change: $taskStatus")
      _taskStatus.postValue(taskStatus)
   }

   fun onStartDateTimeChange(startDatetime: LocalDateTime) {
      _startDateTime.postValue(startDatetime)
   }

   fun onEndDateTimeChange(endDateTime: LocalDateTime) {
      _endDateTime.postValue(endDateTime)
   }
}
