package com.example.superagenda.presentation.screens.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class FilterScreenViewModel @Inject constructor(
) : ViewModel()
{
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

   fun onError(message: String)
   {
      _errorMessage.postValue(message)
   }

   fun onErrorDismissed()
   {
      _errorMessage.postValue(null)
   }

   fun onShow()
   {
      viewModelScope.launch {

      }
   }

   fun onFilterPress(navController: NavController)
   {
      viewModelScope.launch {

      }
   }

   fun onTitleChange(title: String)
   {
      _title.postValue(title)
   }

   fun onTaskStatusChange(taskStatus: TaskStatus)
   {
      _taskStatus.postValue(taskStatus)
   }

   fun onStartDateTimeChange(startDatetime: LocalDateTime)
   {
      _startDateTime.postValue(startDatetime)
   }

   fun onEndDateTimeChange(endDateTime: LocalDateTime)
   {
      _endDateTime.postValue(endDateTime)
   }
}
