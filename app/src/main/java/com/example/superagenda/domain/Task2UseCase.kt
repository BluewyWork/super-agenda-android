package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.Task
import javax.inject.Inject

class Task2UseCase @Inject constructor(
   private val loginRepository: LoginRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun retrieveTasksFromLocalDatabase(): List<Task>? {
      // dao always returns a list
      // null only if there has been an exception
      val tasks = taskRepository.retrieveTasksFromLocalDatabase()

      return tasks
   }

   suspend fun insertOrUpdateTaskToLocalDatabase(task: Task): Boolean {
      return taskRepository.insertOrUpdateTaskToLocalDatabase(task)
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
}