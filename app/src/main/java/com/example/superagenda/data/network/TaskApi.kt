package com.example.superagenda.data.network

import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.network.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TaskApi {
    @GET(Endpoints.GET_TASK_LIST)
    suspend fun retrieveTaskList(@Header("Authorization") token: String): ApiResponse<List<TaskModel>>

    @POST(Endpoints.UPDATE_TASK)
    suspend fun updateTask(@Body taskModel: TaskModel): ApiResponse<String>
}