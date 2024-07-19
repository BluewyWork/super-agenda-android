package com.example.superagenda.presentation.screens.taskOverview

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
class TasksOverviewViewModel @Inject constructor(
   private val globalVariables: GlobalVariables,
   private val taskUseCase: TaskUseCase
) : ViewModel() {
   private val _tasksNotStarted = MutableLiveData<List<Task>?>()
   val tasksNotStarted: LiveData<List<Task>?> = _tasksNotStarted

   private val _tasksOngoing = MutableLiveData<List<Task>?>()
   val tasksOngoing: LiveData<List<Task>?> = _tasksOngoing

   private val _tasksCompleted = MutableLiveData<List<Task>?>()
   val taskCompleted: LiveData<List<Task>?> = _tasksCompleted

   fun onShow() {
      viewModelScope.launch {
         val tasksNotStarted = taskUseCase.retrieveNotStartedTaskList2()

//            if (tasksNotStarted == null) {
//                // do something here
//
//                return@launch
//            }


         val tasksOngoing = taskUseCase.retrieveOnGoingTaskList2()

//            if (tasksOngoing == null) {
//                // do something here
//
//                return@launch
//            }


         val tasksCompleted = taskUseCase.retrieveCompletedTaskList2()

//            if (tasksCompleted == null) {
//                // do something here
//
//                return@launch
//            }

         _tasksNotStarted.postValue(tasksNotStarted)
         _tasksOngoing.postValue(tasksOngoing)
         _tasksCompleted.postValue(tasksCompleted)
      }
   }


   fun onEditClick(task: Task) {
      globalVariables.setTaskToEdit(task)
   }
}