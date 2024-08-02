package com.example.superagenda.presentation.screens.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
) : ViewModel()
{
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
      _title.postValue("")
      _description.postValue("")
      _taskStatus.postValue(TaskStatus.NotStarted)
      _startDateTime.postValue(LocalDateTime.now())
      _endDateTime.postValue(LocalDateTime.now())
   }

   fun onCreateButtonPress(navController: NavController)
   {
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

         if (!task2UseCase.createOrUpdateTask(task))
         {
            // do something if bad

         }
      }
   }

   fun onTitleChange(title: String)
   {
      _title.postValue(title)
   }

   fun onDescriptionChange(description: String)
   {
      _description.postValue(description)
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