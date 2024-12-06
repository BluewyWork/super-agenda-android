package com.example.superagenda.data.network

import com.example.superagenda.data.network.models.ApiResponse
import com.example.superagenda.data.network.models.LastModifiedResponse
import org.bson.BsonDateTime
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MiscApi {
   @GET(Endpoints.GET_LAST_MODIFIED)
   fun getLastModified(
      @Header("Authorization") token: String,
   ): ApiResponse<LastModifiedResponse>

   @PATCH(Endpoints.UPDATE_LAST_MODIFIED)
   fun updateLastModified(
      @Header("Authorization") token: String,
      @Body lastModified: BsonDateTime,
   ): ApiResponse<Map<Any, Any>>
}