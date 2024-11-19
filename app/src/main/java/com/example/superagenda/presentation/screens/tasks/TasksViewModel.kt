package com.example.superagenda.presentation.screens.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
   private val task2UseCase: TaskUseCase,
) : ViewModel() {
   private val _tasksNotStarted = MutableLiveData<List<Task>?>()
   val tasksNotStarted: LiveData<List<Task>?> = _tasksNotStarted

   private val _tasksOngoing = MutableLiveData<List<Task>?>()
   val tasksOngoing: LiveData<List<Task>?> = _tasksOngoing

   private val _tasksCompleted = MutableLiveData<List<Task>?>()
   val tasksCompleted: LiveData<List<Task>?> = _tasksCompleted

   private val _taskToEdit = MutableLiveData<Task>()
   val taskToEdit: LiveData<Task> = _taskToEdit

   fun setTaskToEdit(task: Task) {
      _taskToEdit.postValue(task)
   }

   fun loadTasksNotStarted() {
      // okay -> empty list no tasks created
      // -> null error
      // local tasks has priority
      // cloud is backup
      // so if first time load
      // after that it is just pushing to mongodb

      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
            is Result.Error -> TODO()

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data
               val filtered = tasks.filter { it.status == TaskStatus.NotStarted }
               _tasksNotStarted.postValue(filtered)
            }
         }
      }
   }

   fun loadTasksOngoing() {
      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
            is Result.Error -> TODO()

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data
               val filtered = tasks.filter { it.status == TaskStatus.Ongoing }
               _tasksNotStarted.postValue(filtered)
            }
         }
      }
   }

   fun loadTasksCompleted() {
      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = task2UseCase.getTasksAtDatabase()) {
            is Result.Error -> TODO()

            is Result.Success -> {
               val tasks = resultGetTasksAtDatabase.data
               val filtered = tasks.filter { it.status == TaskStatus.Completed }
               _tasksNotStarted.postValue(filtered)
            }
         }
      }
   }
}