package com.example.superagenda.domain

import android.os.Environment
import android.util.Log
import com.example.superagenda.core.NotificationService
import com.example.superagenda.data.LoginRepository
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class TaskUseCase @Inject constructor(
   private val loginRepository: LoginRepository,
   private val taskRepository: TaskRepository,
) {
   suspend fun deleteTask(task: Task): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()
      if (token.isNullOrBlank()) {
         return false
      }

      Log.d("LOOK AT ME", "UUUWU: ${task._id}")

      return taskRepository.deleteTask(token, task._id.toHexString())
   }

   suspend fun synchronizeApiToLocalDatabase(): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()
      if (token.isNullOrBlank()) {
         return false
      }

      val remoteTaskList = taskRepository.retrieveTaskList(token)
      if (remoteTaskList.isNullOrEmpty()) {
         return false
      }

      taskRepository.clearTaskListFromLocalDatabase()
      return taskRepository.saveTaskListToLocalDatabase(remoteTaskList)
   }

   suspend fun retrieveTaskList2(): List<Task>? {
      val localTaskList = taskRepository.retrieveTaskListFromLocalDatabase()
      if (!localTaskList.isNullOrEmpty()) {
         return localTaskList
      }

      val token = loginRepository.retrieveTokenFromLocalStorage()
      if (token.isNullOrBlank()) {
         return null
      }

      val remoteTaskList = taskRepository.retrieveTaskList(token)
      if (remoteTaskList.isNullOrEmpty()) {
         return null
      }

      taskRepository.saveTaskListToLocalDatabase(remoteTaskList)
      return taskRepository.retrieveTaskListFromLocalDatabase()
   }


   suspend fun retrieveNotStartedTaskList2(): List<Task>? {
      val notStartedTaskList: MutableList<Task> = mutableListOf()

      for (task in retrieveTaskList2() ?: return null) {
         if (task.status == TaskStatus.NotStarted) {
            notStartedTaskList.add(task)
         }
      }

      return notStartedTaskList
   }


   suspend fun retrieveOnGoingTaskList2(): List<Task>? {
      val onGoingTaskList: MutableList<Task> = mutableListOf()

      for (task in retrieveTaskList2() ?: return null) {
         if (task.status == TaskStatus.Ongoing) {
            onGoingTaskList.add(task)
         }
      }

      return onGoingTaskList
   }


   suspend fun retrieveCompletedTaskList2(): List<Task>? {
      val completedTaskList: MutableList<Task> = mutableListOf()

      for (task in retrieveTaskList2() ?: return null) {
         if (task.status == TaskStatus.Completed) {
            completedTaskList.add(task)
         }
      }

      return completedTaskList
   }


   suspend fun updateTask2(task: Task): Boolean {
      return taskRepository.updateOrInsertTaskToLocalDatabase(task)
   }

   suspend fun createTask2(task: Task): Boolean {
      return taskRepository.updateOrInsertTaskToLocalDatabase(task)
   }

   suspend fun createTask3(task: Task): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      return taskRepository.createTask(token, task)
   }

   suspend fun synchronizeTaskListToApi(): Boolean {
      val taskList = taskRepository.retrieveTaskListFromLocalDatabase()

      if (taskList.isNullOrEmpty()) {
         return false
      }

      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      return taskRepository.updateTaskList(token, taskList)
   }

   suspend fun saveTaskListToLocalStorage() {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return
      }

      taskRepository.writeFileToLocalStorage(token)
   }

   suspend fun importTaskListFromLocalStorage(taskList: List<Task>) {
      taskRepository.defCreateOrUpdateTaskList(taskList)
   }

   suspend fun test() {
      return withContext(Dispatchers.IO) {
         val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

         Log.d("LOOK AT ME", "$directory")

         val fileName = "example.txt"
         val fileContent = "Hello, this is the content of the file."

         val file = File(directory, fileName)

         try {
            directory.mkdirs() // create directories if they don't exist
            file.createNewFile()
            file.writeText(fileContent)
            Log.d(
               "LOOK AT ME",
               "File '$fileName' created successfully in directory '$directory'."
            )
         } catch (e: Exception) {
            Log.e("LOOK AT ME", "An error occurred: ${e.message}")
         }
      }
   }

   suspend fun showTaskNotification(service: NotificationService) {
      val list = retrieveNotStartedTaskList2()

      val size = list ?: return

      val size2 = list.size

      service.showNotification("You have $size2 tasks to start!", "...")
   }


   suspend fun logoutTask() {
      taskRepository.cleanLogout()
   }

   //

   suspend fun definitiveCreateOrUpdateTask(task: Task): Boolean {
      return taskRepository.defCreateOrUpdateTask(task)
   }

   suspend fun definitiveDeleteTask(task_id: String): Boolean {
      return taskRepository.defDeleteTask(task_id)
   }

   // save to online as backup
   suspend fun definitiveSynchronizeUpTaskList(): Boolean {
      val localTaskList = taskRepository.retrieveTaskListFromLocalDatabase() ?: return false

      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      val remoteTaskList = taskRepository.retrieveTaskList(token)
      val remoteTaskListIDs = remoteTaskList?.map { it._id }?.toSet() ?: emptySet()

      for (localTask in localTaskList) {
         if (!remoteTaskListIDs.contains(localTask._id)) {
            taskRepository.createTask(token, localTask)
         } else {
            taskRepository.updateTask(token, localTask)
         }
      }

      return true
   }

   // probably only used when first time login
   suspend fun definitiveSynchronizeDownTaskList(): Boolean {
      val token = loginRepository.retrieveTokenFromLocalStorage()

      if (token.isNullOrBlank()) {
         return false
      }

      val remoteTaskList = taskRepository.retrieveTaskList(token) ?: return false

      return taskRepository.defCreateOrUpdateTaskList(remoteTaskList)
   }
}
