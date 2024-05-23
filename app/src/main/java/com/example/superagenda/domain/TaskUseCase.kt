package com.example.superagenda.domain

import com.example.superagenda.data.TaskRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.Task
import com.example.superagenda.domain.models.TaskStatus
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
}