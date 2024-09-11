package com.example.superagenda.data.network

import com.example.superagenda.data.network.response.ApiResponse
import org.bson.types.ObjectId
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DeletedTaskApi {
   @GET(TaskEndpoints.GET_DELETED_TASK_LIST)
   suspend fun getDeletedTasks(
      @Header("Authorization") token: String,
   ) : ApiResponse<List<ObjectId>>

   @POST(TaskEndpoints.ADD_DELETED_TASK)
   suspend fun postDeletedTask(
      @Header("Authorization") token: String,
      @Body objectId: ObjectId
   )
}