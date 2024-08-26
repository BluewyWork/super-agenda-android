package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.Task
import org.bson.types.ObjectId
import javax.inject.Inject

class TaskUseCase @Inject constructor(
   private val loginRepository: LoginRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun retrieveTasksFromLocalDatabase(): List<Task>? {
      // dao always returns a list
      // null only if there has been an exception
      val tasks = taskRepository.retrieveTasksFromLocalDatabase()

      return tasks
   }

   suspend fun insertOrUpdateTaskAtLocalDatabase(task: Task): Boolean {
      return taskRepository.insertOrUpdateTaskAtLocalDatabase(task)
   }

   suspend fun deleteTaskAtLocalDatabase(taskID: ObjectId): Boolean {
      return taskRepository.deleteTaskAtLocalDatabase(taskID)
   }

   suspend fun deleteAllTasksAtLocalDatabase(): Boolean {
      return taskRepository.deleteAllTasksAtLocalDatabase()
   }

   suspend fun retrieveTaskAtApi(): List<Task>? {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return null
      }

      return taskRepository.retrieveTasksAPI(token)
   }

   // honestly with this setup we can't tell what type of error we have here
   // maybe throws ... should be better because that is how its done in kotlin
   suspend fun createTaskAtAPI(task: Task): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      return taskRepository.createTaskAtAPI(task, token)
   }

   suspend fun updateTaskAtAPI(task: Task): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      return taskRepository.updateTaskAtAPI(task, token)
   }

   suspend fun deleteTaskAtAPI(taskID: ObjectId): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      // need to change to result type for more detailed errors
      if (token.isNullOrBlank()) {
         return false
      }

      return taskRepository.deleteTaskAtApi(taskID, token)
   }

   suspend fun backupTasks(tasks: List<Task>): Boolean {
      return taskRepository.backupTasksAtLocalStorage(tasks)
   }
}