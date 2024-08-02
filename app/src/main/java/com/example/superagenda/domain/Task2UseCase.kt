package com.example.superagenda.domain

import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.Task
import javax.inject.Inject

class Task2UseCase @Inject constructor(
   private val loginRepository: LoginRepository,
   private val taskRepository: TaskRepository,
)
{
   suspend fun retrieveTasks(): List<Task>?
   {
      // dao always returns a list
      // null only if there has been an exception
      val tasks = taskRepository.retrieveTasksFromLocalDatabase()

      return tasks
   }

   suspend fun createOrUpdateTask(task: Task): Boolean
   {
      return taskRepository.insertOrUpdateToLocalDatabase(task)
   }
}