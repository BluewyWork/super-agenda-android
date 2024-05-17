package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.TaskApi
import com.example.superagenda.domain.models.Task
import com.google.gson.Gson
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

                Log.d("LOOK AT ME", "from api: $taskList")

                val taskListDomain = taskList.map { it.toDomain() }

                taskListDomain
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }

    suspend fun updateTask(token: String,task: Task, ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskModel = task.toData()

                val gson = Gson()
                val json = gson.toJson(task)
                Log.d("LOOK AT ME", "$json")

                Log.d("LOOK AT ME", "$taskModel")

                val apiResponse = taskApi.updateTask(token, taskModel)

                Log.d("LOOK AT ME", "${apiResponse.ok}")

                apiResponse.ok
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }
}