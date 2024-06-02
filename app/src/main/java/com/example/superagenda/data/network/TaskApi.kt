package com.example.superagenda.data.network

import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.network.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
    @GET(Endpoints.GET_TASK_LIST)
    suspend fun retrieveTaskList(@Header("Authorization") token: String): ApiResponse<List<TaskModel>>

    @POST(Endpoints.UPDATE_TASK)
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Body taskModel: TaskModel
    ): ApiResponse<Map<String, Any>>

    @POST(Endpoints.NEW_TASK)
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body taskModel: TaskModel
    ): ApiResponse<Map<String, Any>>

    @POST(Endpoints.UPDATE_TASK_LIST)
    suspend fun updateTaskList(
        @Header("Authorization") token: String,
        @Body taskList: List<TaskModel>
    ): ApiResponse<Map<String, Any>>

    @DELETE("${Endpoints.DELETE_TASK}/{task_id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("task_id") taskId: String
    ): ApiResponse<Map<String, Any>>
}