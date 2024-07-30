package com.example.superagenda.presentation.screens.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
   private val taskUseCase: TaskUseCase,
   private val loginUseCase: LoginUseCase
) : ViewModel()
{
   private val _tasksNotStarted = MutableLiveData<List<Task>?>()
   val tasksNotStarted: LiveData<List<Task>?> = _tasksNotStarted

   private val _taskToEdit = MutableLiveData<Task?>()
   val taskToEdit: LiveData<Task?> = _taskToEdit

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
        _tasksNotStarted.postValue(emptyList())
     }
   }
}