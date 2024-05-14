package com.example.superagenda.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.superagenda.data.TaskRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.Task
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val taskRepository: TaskRepository
) {
    private suspend fun retrieveTaskList(): List<Task>? {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return null
        }

        val taskList = taskRepository.retrieveTaskList(token)

        return taskList
    }
    suspend fun retrieveNotStartedTaskList(): List<Task>? {
        return retrieveTaskList()
    }

    suspend fun retrieveOnGoingTaskList(): List<Task>? {
        return retrieveTaskList()
    }

    suspend fun retrieveCompletedTaskList(): List<Task>? {
        return retrieveTaskList()
    }

    suspend fun updateTask(task: Task): Boolean {
        return taskRepository.updateTask(task)
    }
}