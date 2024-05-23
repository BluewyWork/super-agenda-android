package com.example.superagenda.data

import android.util.Log
import com.example.superagenda.data.models.BsonDateTimeConverter
import com.example.superagenda.data.models.toData
import com.example.superagenda.data.models.toDomain
import com.example.superagenda.data.network.TaskApi
import com.example.superagenda.domain.models.Task
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.BsonDateTime
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

                Log.d("LOOK AT ME", "DOWN: $taskList")
                Log.d("LOOK AT ME", "DOWN 1: ${taskList[0].startDateTime.toString()}")

                val taskListDomain = taskList.map { it.toDomain() }

                taskListDomain
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                null
            }
        }
    }

    suspend fun updateTask(token: String, task: Task): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskModel = task.toData()

                Log.d("LOOK AT ME", "UP: $taskModel")

                val gson = GsonBuilder()
                    .registerTypeAdapter(BsonDateTime::class.java, BsonDateTimeConverter())
                    .create()

                val serializedJson = gson.toJson(taskModel)

                Log.d("LOOK AT ME", "S: $serializedJson")

                val apiResponse = taskApi.updateTask(token, taskModel)

                apiResponse.ok
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }

    suspend fun createTask(token: String, task: Task): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val taskModel = task.toData()

                val response = taskApi.createTask(token, taskModel)

                return@withContext response.ok
            } catch (e: Exception) {
                Log.e("LOOK AT ME", "${e.message}")

                false
            }
        }
    }
}