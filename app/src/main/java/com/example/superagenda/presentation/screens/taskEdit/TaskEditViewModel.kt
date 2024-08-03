package com.example.superagenda.presentation.screens.taskEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.presentation.screens.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
   private val globalVariables: GlobalVariables,
) : ViewModel() {
   private val _errorMessage = MutableLiveData<String?>()
   val errorMessage: LiveData<String?> = _errorMessage

   private val _taskToEdit2 = MutableLiveData<Task?>()
   val taskToEdit2: LiveData<Task?> = _taskToEdit2

   private val _title = MutableLiveData<String>()
   val title = _title

   private val _description = MutableLiveData<String>()
   val description = _description

   private val _taskStatus = MutableLiveData<TaskStatus>()
   val taskStatus = _taskStatus

   private val _startDateTime = MutableLiveData<LocalDateTime>()
   val startDateTime: LiveData<LocalDateTime> = _startDateTime

   private val _endDateTime = MutableLiveData<LocalDateTime>()
   val endDateTime: LiveData<LocalDateTime> = _endDateTime

   fun setTaskToEdit(task: Task?) {
      viewModelScope.launch {
         _taskToEdit2.postValue(task)
      }
   }

   fun onShow() {
      viewModelScope.launch {
         _title.postValue(taskToEdit2.value?.title)
         _description.postValue(taskToEdit2.value?.description)
         _taskStatus.postValue(taskToEdit2.value?.status)
         _startDateTime.postValue(taskToEdit2.value?.startDateTime)
         _endDateTime.postValue(taskToEdit2.value?.endDateTime)
      }
   }

   fun onError(message: String) {
      _errorMessage.postValue(message)
   }

   fun onErrorDismissed() {
      _errorMessage.postValue(null)
   }

   fun onUpdateButtonPress(navController: NavController) {
      viewModelScope.launch {

      }
   }

   fun onDeleteButtonPress(navController: NavController) {
      viewModelScope.launch {

      }
   }

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
}