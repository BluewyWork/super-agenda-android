package com.example.superagenda.domain

import com.example.superagenda.data.TaskRepository
import com.example.superagenda.data.TokenRepository
import com.example.superagenda.domain.models.Task
import javax.inject.Inject

class TaskUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val taskRepository: TaskRepository
) {
    suspend fun retrieveNotStartedTaskList(): List<Task>? {
        val token = tokenRepository.retrieveTokenFromLocalStorage()

        if (token.isNullOrBlank()) {
            return null
        }

        val notStartedTaskList = taskRepository.retrieveTaskList(token)

        return notStartedTaskList
    }
}