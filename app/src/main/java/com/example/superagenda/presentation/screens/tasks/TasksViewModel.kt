package com.example.superagenda.presentation.screens.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.DeletedTaskUseCase
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.TaskUseCase
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
   private val task2UseCase: TaskUseCase,
   private val deletedTaskUseCase: DeletedTaskUseCase,
   private val loginUseCase: LoginUseCase
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
         val tasks = task2UseCase.retrieveTasksFromLocalDatabase() ?: return@launch

         val filtered = tasks.filter { it.status == TaskStatus.NotStarted }

         _tasksNotStarted.postValue(filtered)
      }
   }

   fun loadTasksOngoing() {
      viewModelScope.launch {
         val tasks = task2UseCase.retrieveTasksFromLocalDatabase() ?: return@launch

         val filtered = tasks.filter { it.status == TaskStatus.Ongoing }

         _tasksOngoing.postValue(filtered)
      }
   }

   fun loadTasksCompleted() {
      viewModelScope.launch {
         val tasks = task2UseCase.retrieveTasksFromLocalDatabase() ?: return@launch

         val filtered = tasks.filter { it.status == TaskStatus.Completed }

         _tasksCompleted.postValue(filtered)
      }
   }

   fun synchronizeTasks() {
      viewModelScope.launch {
         if (!loginUseCase.isLoggedIn()) {
            return@launch
         }

         val localTasks = task2UseCase.retrieveTasksFromLocalDatabase() ?: emptyList()
         val cloudTasks = task2UseCase.retrieveTaskAtApi() ?: emptyList()
         val cloudDeletedTasks = deletedTaskUseCase.retrieveDeletedTasksAtApi() ?: emptyList()

         val localTasksMap = localTasks.associateBy { it._id }
         val cloudTasksMap = cloudTasks.associateBy { it._id }
         val cloudDeletedTasksMap = cloudDeletedTasks.associateBy { it }

         val tasksToDeleteLocally = localTasks.filter { cloudTasksMap.containsKey(it._id) && cloudDeletedTasksMap.containsKey(it._id) }
         tasksToDeleteLocally.forEach { localTask ->
            task2UseCase.deleteTaskAtLocalDatabase(localTask._id)
         }

         val tasksToDeleteInCloud = cloudTasks.filter { !localTasksMap.containsKey(it._id) && cloudDeletedTasksMap.containsKey(it._id) }
         tasksToDeleteInCloud.forEach { cloudTask ->
            task2UseCase.deleteTaskAtAPI(cloudTask._id)
         }

         val updatedLocalTasks = task2UseCase.retrieveTasksFromLocalDatabase() ?: emptyList()
         val updatedCloudTasks = task2UseCase.retrieveTaskAtApi() ?: emptyList()

         val updatedLocalTasksMap = updatedLocalTasks.associateBy { it._id }
         val updatedCloudTasksMap = updatedCloudTasks.associateBy { it._id }

         updatedLocalTasks.forEach { localTask ->
            val cloudTask = updatedCloudTasksMap[localTask._id]
            if (cloudTask == null) {
               task2UseCase.createTaskAtAPI(localTask)
            } else if (localTask.lastModified > cloudTask.lastModified) {
               task2UseCase.updateTaskAtAPI(localTask)
            }
         }

         updatedCloudTasks.forEach { cloudTask ->
            val localTask = updatedLocalTasksMap[cloudTask._id]
            if (localTask == null) {
               task2UseCase.insertOrUpdateTaskAtLocalDatabase(cloudTask)
            } else if (cloudTask.lastModified > localTask.lastModified) {
               task2UseCase.insertOrUpdateTaskAtLocalDatabase(cloudTask)
            }
         }

         loadTasksNotStarted()
         loadTasksOngoing()
         loadTasksCompleted()
      }
   }
}