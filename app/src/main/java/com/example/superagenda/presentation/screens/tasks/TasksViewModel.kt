package com.example.superagenda.presentation.screens.tasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
) : ViewModel() {
   private val _tasks = MutableStateFlow<List<Task>>(listOf())
   val tasks: StateFlow<List<Task>> = _tasks

   private val _taskToEdit = MutableStateFlow<Task?>(null)

   // Getters & Setters

   fun setTaskToEdit(task: Task) {
      _taskToEdit.value = task
   }

   fun getTaskToEdit(): Task? {
      return _taskToEdit.value
   }

   // Main

   fun refreshTasks() {
      viewModelScope.launch {
         when (val resultGetTasksAtDatabase = taskUseCase.getTasksAtDatabase()) {
            is Result.Error -> Log.e("LOOK AT ME", "@VIEWMODEL ${resultGetTasksAtDatabase.error}")
            is Result.Success -> _tasks.value = resultGetTasksAtDatabase.data
         }
      }
   }
}