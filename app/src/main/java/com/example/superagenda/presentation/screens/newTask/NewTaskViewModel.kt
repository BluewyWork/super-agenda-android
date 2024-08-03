package com.example.superagenda.presentation.screens.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.Task2UseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
   private val task2UseCase: Task2UseCase,
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

   private val _showPopup = MutableLiveData<Boolean>()
   val showPopup: LiveData<Boolean> = _showPopup

   private val _popupTitle = MutableLiveData<String>()
   val popupTitle: LiveData<String> = _popupTitle

   private val _popupMessage = MutableLiveData<String>()
   val popupMessage: LiveData<String> = _popupMessage

   fun showPopup(title: String, message: String) {
      _popupTitle.postValue(title)
      _popupMessage.postValue(message)
      _showPopup.postValue(true)
   }

   fun dismissPopup() {
      _showPopup.postValue(false)
   }

   fun onShow() {
      _title.postValue("")
      _description.postValue("")
      _taskStatus.postValue(TaskStatus.NotStarted)
      _startDateTime.postValue(LocalDateTime.now())
      _endDateTime.postValue(LocalDateTime.now())
   }

   fun onCreateButtonPress(navController: NavController) {
      viewModelScope.launch {
         // TODO: improve error handling
         // maybe popup?
         val title = title.value ?: return@launch
         val description = description.value ?: return@launch
         val taskStatus = taskStatus.value ?: return@launch
         val startDatetime = startDateTime.value ?: return@launch
         val endDateTime = endDateTime.value ?: return@launch

         val task = Task(
            _id = ObjectId(),
            title = title,
            description = description,
            status = taskStatus,
            startDateTime = startDatetime,
            endDateTime = endDateTime
         )

         if (!task2UseCase.insertOrUpdateTaskToLocalDatabase(task)) {
            showPopup("ERROR", "Failed to create task...")
         }

         showPopup("INFO", "Task has been created!")

         if (!loginUseCase.isLoggedIn()) {
            return@launch
         }

         if (!task2UseCase.createTaskAtAPI(task)) {
            showPopup("ERROR", "Failed to sync to api...")
         }
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