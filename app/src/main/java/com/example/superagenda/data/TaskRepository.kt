package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.TaskApi
import com.example.superagenda.domain.models.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskApi: TaskApi
) {
    suspend fun retrieveTaskList(token: String): List<Task>? {
        return withContext(Dispatchers.IO) {
            try {
                val taskList = taskApi.retrieveTaskList(token).data

                if (taskList.isEmpty()) {
                    return@withContext null
                }

                val taskListDomain = taskList.map { it.toDomain() }

                taskListDomain
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }

    suspend fun updateTask(task: Task): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskModel = task.toData()

                val apiResponse = taskApi.updateTask(taskModel)

                apiResponse.ok
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }
}