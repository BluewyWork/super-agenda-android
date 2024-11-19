package com.example.superagenda.domain

import com.example.superagenda.data.AuthenticationRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.Task
import com.example.superagenda.util.AppResult
import com.example.superagenda.util.Result
import org.bson.types.ObjectId
import javax.inject.Inject

class TaskUseCase @Inject constructor(
   private val authenticationRepository: AuthenticationRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun getTasksAtDatabase(): AppResult<List<Task>> {
      return taskRepository.getTasksAtDatabase()
   }

   suspend fun upsertTaskAtDatabase(task: Task): AppResult<Unit> {
      return taskRepository.upsertTaskAtDatabase(task)
   }

   suspend fun deleteTaskAtDatabase(taskID: ObjectId): AppResult<Unit> {
      return taskRepository.deleteTaskAtDatabase(taskID)
   }

   suspend fun deleteTasksAtDatabase(): AppResult<Unit> {
      return taskRepository.deleteTasksAtDatabase()
   }

   suspend fun getTasksAtApi(): AppResult<List<Task>> {
      val token = when(val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

      return taskRepository.getTasksAtAPI(token)
   }

   suspend fun createTaskAtApi(task: Task): AppResult<Boolean> {
     val token = when( val result = authenticationRepository.getTokenAtDatabase()) {
        is Result.Error -> return Result.Error(result.error)
        is Result.Success -> result.data
     }

        return taskRepository.createTaskAtAPI(task, token)
   }

   suspend fun updateTaskAtAPI(task: Task): AppResult<Boolean> {
      val token = when( val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

      return taskRepository.updateTaskAtAPI(task, token)
   }

   suspend fun deleteTaskAtAPI(taskID: ObjectId): AppResult<Boolean> {
      val token = when( val result = authenticationRepository.getTokenAtDatabase()) {
         is Result.Error -> return Result.Error(result.error)
         is Result.Success -> result.data
      }

      return taskRepository.deleteTaskAtApi(taskID, token)
   }

   suspend fun backupTasks(tasks: List<Task>): Boolean {
      return taskRepository.backupTasksAtLocalStorage(tasks)
   }
}