package com.example.superagenda.data.network

import com.example.superagenda.data.models.TaskModel
import com.example.superagenda.data.network.models.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApi {
   @GET(Endpoints.GET_TASK_LIST)
   suspend fun retrieveTaskList(@Header("Authorization") token: String): Response<ApiResponse<List<TaskModel>>>

   @PATCH(Endpoints.UPDATE_TASK)
   suspend fun updateTask(
      @Header("Authorization") token: String,
      @Body taskModel: TaskModel,
   ): Response<ApiResponse<Nothing>>

   @POST(Endpoints.NEW_TASK)
   suspend fun createTask(
      @Header("Authorization") token: String,
      @Body taskModel: TaskModel,
   ): Response<ApiResponse<Nothing>>

   @DELETE("${Endpoints.DELETE_TASK}/{task_id}")
   suspend fun deleteTask(
      @Header("Authorization") token: String,
      @Path("task_id") taskId: String,
   ): Response<ApiResponse<Nothing>>
}