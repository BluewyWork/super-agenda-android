package com.example.superagenda.domain

import android.os.Environment
import android.util.Log
import com.example.superagenda.core.NotificationService
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val taskRepository: TaskRepository,
) {
    private suspend fun retrieveTaskList(): List<Task>? {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return null
        }

        var taskList = taskRepository.retrieveTaskList(token)

        if (taskList != null) {
            taskRepository.clearTaskListFromLocalDatabase()
            taskRepository.saveTaskListToLocalDatabase(taskList)
        } else {
            taskList = taskRepository.retrieveTaskListFromLocalDatabase()
        }

        return taskList
    }

    suspend fun retrieveNotStartedTaskList(): List<Task>? {
        val notStartedTaskList: MutableList<Task> = mutableListOf()

        for (task in retrieveTaskList() ?: return null) {
            if (task.status == TaskStatus.NotStarted) {
                notStartedTaskList.add(task)
            }
        }

        return notStartedTaskList
    }

    suspend fun retrieveOnGoingTaskList(): List<Task>? {
        val onGoingTaskList: MutableList<Task> = mutableListOf()

        for (task in retrieveTaskList() ?: return null) {
            if (task.status == TaskStatus.Ongoing) {
                onGoingTaskList.add(task)
            }
        }

        return onGoingTaskList
    }

    suspend fun retrieveCompletedTaskList(): List<Task>? {
        val completedTaskList: MutableList<Task> = mutableListOf()

        for (task in retrieveTaskList() ?: return null) {
            if (task.status == TaskStatus.Completed) {
                completedTaskList.add(task)
            }
        }

        return completedTaskList
    }

    suspend fun updateTask(task: Task): Boolean {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return false
        }

        return taskRepository.updateTask(token, task)
    }

    suspend fun createTask(task: Task): Boolean {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return false
        }

        return taskRepository.createTask(token, task)
    }

    suspend fun saveTaskListToLocalStorage() {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return
        }

        taskRepository.writeFileToLocalStorage(token)
    }

    suspend fun importTaskListFromLocalStorage(taskList: List<Task>) {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return
        }

        for (task in taskList) {
            taskRepository.updateTask(token, task)
        }
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
        val list = retrieveNotStartedTaskList()

        val size = list ?: return

        val size2 = list.size

        service.showNotification("You have $size2 tasks to start!", "...")
    }
}
