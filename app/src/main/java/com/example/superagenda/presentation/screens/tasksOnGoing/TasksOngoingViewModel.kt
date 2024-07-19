package com.example.superagenda.presentation.screens.tasksOnGoing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.presentation.screens.GlobalVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksOngoingViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val globalVariables: GlobalVariables
) : ViewModel() {
   private val _onGoingTaskList = MutableLiveData<List<Task>?>()
   val onGoingTaskList: LiveData<List<Task>?> = _onGoingTaskList

   fun onShow() {
      viewModelScope.launch {
         val onGoingTaskList = taskUseCase.retrieveOnGoingTaskList2()

         _onGoingTaskList.postValue(onGoingTaskList)
      }
   }

   fun onEditClick(task: Task) {
      globalVariables.setTaskToEdit(task)
   }
}