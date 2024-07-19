package com.example.superagenda.presentation.screens.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
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

   private val _errorMessage = MutableLiveData<String?>()
   val errorMessage: LiveData<String?> = _errorMessage

   fun onError(message: String) {
      _errorMessage.postValue(message)
   }

   fun onErrorDismissed() {
      _errorMessage.postValue(null)
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
         val task = _title.value?.let {
            _description.value?.let { it1 ->
               _taskStatus.value?.let { it2 ->
                  _startDateTime.value?.let { it3 ->
                     _endDateTime.value?.let { it4 ->
                        Task(
                           _id = ObjectId(),
                           title = it,
                           description = it1,
                           status = it2,
                           startDateTime = it3,
                           endDateTime = it4
                        )
                     }
                  }
               }
            }
         }

         if (task == null) {
            return@launch
         }

         if (!taskUseCase.definitiveCreateOrUpdateTask(task)) {
            // do something here
            onError("Well, this is certainly super rare to happen...")
         }

//            if (!taskUseCase.createTask3(task)) {
//                onError("Failed sending task..\n\nMaybe check your internet connection?\n\nSynchronization will happen on the next successful action.")
//            }

         if (!taskUseCase.definitiveSynchronizeUpTaskList()) {
            // do something here
            onError("Failed sending task..\n\nMaybe check your internet connection?\n\nSynchronization will happen on the next successful action.")
         }

         onShow()
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